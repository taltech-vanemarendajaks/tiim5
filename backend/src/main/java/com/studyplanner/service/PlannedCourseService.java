package com.studyplanner.service;

import com.studyplanner.client.OisClient;
import com.studyplanner.client.dto.OisCourseFullResponse;
import com.studyplanner.dto.PlannedCourseRequest;
import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.entity.*;
import com.studyplanner.entity.Module;
import com.studyplanner.exception.AccessDeniedException;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.mapper.CourseMapper;
import com.studyplanner.mapper.PlannedCourseMapper;
import com.studyplanner.repository.*;
import com.studyplanner.utils.UserRequestContext;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannedCourseService {

  private final StudyPlanRepository studyPlanRepository;
  private final CourseRepository courseRepository;
  private final ModuleRepository moduleRepository;
  private final PlannedCourseRepository plannedCourseRepository;
  private final SemesterRepository semesterRepository;
  private final OisClient oisClient;

  @Transactional
  public List<PlannedCourseResponse> updatePlannedCourses(
      UUID studyPlanExternalId, List<PlannedCourseRequest> requests) {
    List<PlannedCourseRequest> uniqueRequests =
        requests.stream()
            .collect(
                Collectors.collectingAndThen(
                    Collectors.toCollection(
                        () ->
                            new TreeSet<>(
                                Comparator.comparing(PlannedCourseRequest::courseVersionExternalId)
                                    .thenComparing(PlannedCourseRequest::semesterExternalId))),
                    ArrayList::new));

    StudyPlan studyPlan =
        studyPlanRepository
            .findByExternalId(studyPlanExternalId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "No study plan found for external id " + studyPlanExternalId));

    UUID userExternalId = UserRequestContext.getUserExternalId();
    if (!studyPlan.getUser().getExternalId().equals(userExternalId)) {
      throw new AccessDeniedException(userExternalId.toString());
    }

    Map<UUID, Semester> semestersByExternalId =
        semesterRepository.findAllByStudyPlanExternalId(studyPlanExternalId).stream()
            .collect(Collectors.toMap(Semester::getExternalId, semester -> semester));

    uniqueRequests.forEach(
        request -> {
          if (!semestersByExternalId.containsKey(request.semesterExternalId())) {
            throw new ResourceNotFoundException(
                "Semester " + request.semesterExternalId() + " does not belong to this study plan");
          }
        });

    List<PlannedCourse> plannedCourses = new ArrayList<>();
    for (PlannedCourseRequest request : uniqueRequests) {
      Course course =
          resolveCourse(request.courseExternalId(), request.courseVersionExternalId(), studyPlan);
      Module module = resolveModule(course, studyPlan);
      Semester semester = semestersByExternalId.get(request.semesterExternalId());

      PlannedCourse plannedCourse = new PlannedCourse();
      plannedCourse.setExternalId(UUID.randomUUID());
      plannedCourse.setCourse(course);
      plannedCourse.setModule(module);
      plannedCourse.setSemester(semester);
      plannedCourse.setStatus(request.status() != null ? request.status() : CourseStatus.PLANNED);
      plannedCourse.setCreationDate(LocalDateTime.now());
      plannedCourses.add(plannedCourse);
    }

    plannedCourseRepository.deleteByStudyPlanExternalId(studyPlanExternalId);
    plannedCourseRepository.saveAll(plannedCourses);

    return PlannedCourseMapper.mapToResponseList(plannedCourses);
  }

  private Course resolveCourse(
      UUID courseExternalId, UUID courseVersionExternalId, StudyPlan studyPlan) {
    return courseRepository
        .findByCourseVersionExternalId(courseVersionExternalId)
        .orElseGet(
            () -> {
              OisCourseFullResponse response =
                  oisClient.getCourseByVersionExternalId(courseExternalId, courseVersionExternalId);
              Course course = new Course();
              course.setExternalId(UUID.randomUUID());
              course.setCourseExternalId(response.courseResponse().externalId());
              course.setCourseVersionExternalId(response.courseResponse().latestVersion());
              course.setTitleEn(response.courseResponse().title().en());
              course.setTitleEt(response.courseResponse().title().et());
              course.setCode(response.courseResponse().code());
              course.setCredits(response.courseResponse().credits());
              course.setSemesterType(CourseMapper.mapSemesterType(response.versionResponse()));
              course.setCreationDate(LocalDateTime.now());
              Course saved = courseRepository.save(course);

              Module optionalSubjects = findOptionalSubjectsModule(studyPlan);
              optionalSubjects.getCourses().add(saved);
              moduleRepository.save(optionalSubjects);

              return saved;
            });
  }

  private Module resolveModule(Course course, StudyPlan studyPlan) {
    return moduleRepository.findByCourseId(course.getId()).stream()
        .filter(
            module ->
                module.getCurriculums().stream()
                    .anyMatch(
                        curriculum -> curriculum.getId().equals(studyPlan.getCurriculum().getId())))
        .findFirst()
        .orElseGet(() -> findOptionalSubjectsModule(studyPlan));
  }

  private Module findOptionalSubjectsModule(StudyPlan studyPlan) {
    return moduleRepository
        .findByTitleAndCurriculums_ExternalId(
            "Vabaained", studyPlan.getCurriculum().getExternalId())
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Optional subjects module not found for curriculum "
                        + studyPlan.getCurriculum().getExternalId()));
  }
}
