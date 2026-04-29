package com.studyplanner.service;

import com.studyplanner.dto.PlannedCourseRequest;
import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.entity.*;
import com.studyplanner.entity.Module;
import com.studyplanner.exception.AccessDeniedException;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.mapper.PlannedCourseMapper;
import com.studyplanner.repository.*;
import com.studyplanner.utils.UserRequestContext;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannedCourseService {

  private final PlannedCourseRepository plannedCourseRepository;
  private final StudyPlanService studyPlanService;
  private final CourseService courseService;
  private final ModuleService moduleService;
  private final SemesterService semesterService;

  @Transactional
  public List<PlannedCourseResponse> setPlannedCourses(
      UUID studyPlanExternalId, List<PlannedCourseRequest> requests) {
    StudyPlan studyPlan = getAuthorizedStudyPlan(studyPlanExternalId);
    List<PlannedCourseRequest> uniqueRequests = validateRequests(requests);

    Map<UUID, Semester> semestersByExternalId =
        getAndValidateSemesters(studyPlanExternalId, uniqueRequests);
    Map<UUID, Course> coursesByVersionId = getOrFetchCourses(uniqueRequests);

    Module optionalSubjects =
        moduleService.fetchModuleByTitleAndCurriculumExternalId(
            "Vabaained", studyPlan.getCurriculum().getExternalId());
    Map<Long, List<Module>> modulesByCourseId = getModulesByCourseId(coursesByVersionId.values());

    List<PlannedCourse> plannedCourses = new ArrayList<>();
    for (PlannedCourseRequest request : uniqueRequests) {
      Course course = coursesByVersionId.get(request.courseVersionExternalId());
      Module module = resolveModule(course, studyPlan, modulesByCourseId, optionalSubjects);
      Semester semester = semestersByExternalId.get(request.semesterExternalId());

      PlannedCourse plannedCourse = PlannedCourseMapper.mapToPlannedCourse(request);
      plannedCourse.setCourse(course);
      plannedCourse.setModule(module);
      plannedCourse.setSemester(semester);
      plannedCourses.add(plannedCourse);
    }

    plannedCourseRepository.deleteByStudyPlanExternalId(studyPlanExternalId);
    plannedCourseRepository.saveAll(plannedCourses);

    semestersByExternalId.values().forEach(semesterService::recalculateAndSave);
    studyPlanService.recalculateAndSave(studyPlan, plannedCourses);

    return PlannedCourseMapper.mapToResponseList(plannedCourses);
  }

  private List<PlannedCourseRequest> validateRequests(List<PlannedCourseRequest> requests) {
    Set<String> seen = new HashSet<>();

    for (PlannedCourseRequest request : requests) {
      String key = request.courseVersionExternalId() + "_" + request.semesterExternalId();
      if (!seen.add(key)) {
        throw new IllegalArgumentException(
            String.format(
                "Course and semester combination is duplicated for course version: %s and semester: %s",
                request.courseVersionExternalId(), request.semesterExternalId()));
      }
    }

    return requests;
  }

  private StudyPlan getAuthorizedStudyPlan(UUID studyPlanExternalId) {
    StudyPlan studyPlan = studyPlanService.fetchStudyPlanById(studyPlanExternalId);

    UUID userExternalId = UserRequestContext.getUserExternalId();
    if (!studyPlan.getUser().getExternalId().equals(userExternalId)) {
      throw new AccessDeniedException(userExternalId.toString());
    }
    return studyPlan;
  }

  private Map<UUID, Semester> getAndValidateSemesters(
      UUID studyPlanExternalId, List<PlannedCourseRequest> requests) {

    Map<UUID, Semester> semestersByExternalId =
        semesterService.fetchSemestersByStudyPlanExternalId(studyPlanExternalId).stream()
            .collect(Collectors.toMap(Semester::getExternalId, semester -> semester));

    for (PlannedCourseRequest request : requests) {
      if (!semestersByExternalId.containsKey(request.semesterExternalId())) {
        throw new ResourceNotFoundException(
            "Semester " + request.semesterExternalId() + " does not belong to this study plan");
      }
    }

    return semestersByExternalId;
  }

  private Map<UUID, Course> getOrFetchCourses(List<PlannedCourseRequest> requests) {
    List<UUID> versionIds =
        requests.stream().map(PlannedCourseRequest::courseVersionExternalId).toList();

    Map<UUID, Course> coursesByVersionId =
        courseService.fetchCoursesByVersionExternalIds(versionIds).stream()
            .collect(Collectors.toMap(Course::getCourseVersionExternalId, course -> course));

    for (PlannedCourseRequest request : requests) {
      if (!coursesByVersionId.containsKey(request.courseVersionExternalId())) {
        Course course =
            courseService.getFromOisAndSaveCourse(
                request.courseExternalId(), request.courseVersionExternalId());
        coursesByVersionId.put(course.getCourseVersionExternalId(), course);
      }
    }

    return coursesByVersionId;
  }

  private Map<Long, List<Module>> getModulesByCourseId(Collection<Course> courses) {
    List<Long> courseIds = courses.stream().map(Course::getId).toList();
    List<Module> modules = moduleService.fetchModulesByCourseIds(courseIds);

    return modules.stream()
        .flatMap(
            module -> module.getCourses().stream().map(course -> Map.entry(course.getId(), module)))
        .collect(
            Collectors.groupingBy(
                Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
  }

  private Module resolveModule(
      Course course,
      StudyPlan studyPlan,
      Map<Long, List<Module>> modulesByCourseId,
      Module optionalSubjects) {

    List<Module> modules = modulesByCourseId.get(course.getId());

    if (modules != null) {
      return modules.stream()
          .filter(
              module ->
                  module.getCurriculums().stream()
                      .anyMatch(
                          curriculum ->
                              curriculum.getId().equals(studyPlan.getCurriculum().getId())))
          .findFirst()
          .orElse(optionalSubjects);
    }
    return optionalSubjects;
  }
}
