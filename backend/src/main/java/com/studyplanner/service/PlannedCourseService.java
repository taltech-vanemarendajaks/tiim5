package com.studyplanner.service;

import com.studyplanner.dto.CourseResponse;
import com.studyplanner.dto.PlannedCourseRequest;
import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.entity.*;
import com.studyplanner.entity.Module;
import com.studyplanner.exception.AccessDeniedException;
import com.studyplanner.exception.ResourceNotFoundException;
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
  private final CourseService courseService;

  @Transactional
  public List<PlannedCourseResponse> updatePlannedCourses(
      UUID studyPlanExternalId, List<PlannedCourseRequest> requests) {

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
            .collect(Collectors.toMap(Semester::getExternalId, s -> s));

    requests.forEach(
        request -> {
          if (!semestersByExternalId.containsKey(request.semesterExternalId())) {
            throw new ResourceNotFoundException(
                "Semester " + request.semesterExternalId() + " does not belong to this study plan");
          }
        });

    List<PlannedCourse> plannedCourses = new ArrayList<>();
    for (PlannedCourseRequest request : requests) {
      Course course =
          resolveCourse(request.courseVersionExternalId(), request.courseCode(), studyPlan);
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
      UUID courseVersionExternalId, String courseCode, StudyPlan studyPlan) {
    return courseRepository
        .findByCourseVersionExternalId(courseVersionExternalId)
        .orElseGet(
            () -> {
              CourseResponse response =
                  courseService
                      .getAllCourses(1, 1, null, courseCode)
                      .getFirst(); // fetch by versionId instead
              Course course = new Course();
              course.setExternalId(UUID.randomUUID());
              course.setCourseExternalId(response.externalId());
              course.setCourseVersionExternalId(response.versionExternalId());
              course.setTitleEn(response.titleEn());
              course.setTitleEt(response.titleEt());
              course.setCode(response.code());
              course.setCredits(response.credits());
              course.setSemesterType(response.semesterType());
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
