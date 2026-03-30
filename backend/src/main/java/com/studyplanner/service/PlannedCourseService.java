package com.studyplanner.service;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

import com.studyplanner.dto.PlannedCourseRequest;
import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.entity.*;
import com.studyplanner.entity.Module;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.mapper.PlannedCourseMapper;
import com.studyplanner.repository.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannedCourseService {

  private final CurriculumRepository curriculumRepository;
  private final CourseRepository courseRepository;
  private final SemesterRepository semesterRepository;
  private final ModuleRepository moduleRepository;
  private final PlannedCourseRepository plannedCourseRepository;

  @Transactional
  public List<PlannedCourseResponse> updatePlannedCourses(
      UUID studyPlanExternalId, List<PlannedCourseRequest> requests) {

    List<PlannedCourseRequest> normalizedRequests =
        new ArrayList<>(
            requests.stream()
                .collect(
                    toMap(
                        r -> r.courseVersionExternalId() + ":" + r.semesterExternalId(),
                        identity(),
                        (existing, duplicate) -> existing,
                        LinkedHashMap::new))
                .values());

    Curriculum curriculum =
        curriculumRepository
            .findByStudyPlansExternalId(studyPlanExternalId)
            .orElseThrow(() -> new ResourceNotFoundException("Curriculum not found for studyPlanExternalId: " + studyPlanExternalId));

    List<UUID> courseVersionIds =
        normalizedRequests.stream().map(PlannedCourseRequest::courseVersionExternalId).toList();

    List<UUID> semesterIds =
        normalizedRequests.stream().map(PlannedCourseRequest::semesterExternalId).toList();

    Map<UUID, Course> courseMap =
        courseRepository.findAllByCourseVersionExternalIdIn(courseVersionIds).stream()
            .collect(toMap(Course::getCourseVersionExternalId, identity()));

    Map<UUID, Semester> semesterMap =
        semesterRepository.findAllByExternalIdIn(semesterIds).stream()
            .collect(toMap(Semester::getExternalId, identity()));

    Map<UUID, Module> moduleMap =
        moduleRepository.findAllByCurriculumExternalId(curriculum.getExternalId()).stream()
            .flatMap(m -> m.getCourses().stream().map(c -> Map.entry(c.getExternalId(), m)))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

    List<PlannedCourse> plannedCourses =
        normalizedRequests.stream()
            .map(request -> mapToEntity(request, courseMap, semesterMap, moduleMap, curriculum))
            .toList();

    plannedCourseRepository.deleteByStudyPlanExternalId(studyPlanExternalId);
    List<PlannedCourse> saved = plannedCourseRepository.saveAll(plannedCourses);

    return PlannedCourseMapper.mapToResponseList(saved);
  }

  private PlannedCourse mapToEntity(
      PlannedCourseRequest request,
      Map<UUID, Course> courseMap,
      Map<UUID, Semester> semesterMap,
      Map<UUID, Module> moduleMap, Curriculum curriculum) {

//    Course course =
//        Optional.ofNullable(courseMap.get(request.courseVersionExternalId()))
//            .orElseThrow(
//                () ->
//                    new ResourceNotFoundException(
//                        "Course not found: " + request.courseVersionExternalId()));
    //Save course to db if not found and map to "Vabaained" module?

    Course course = courseMap.computeIfAbsent(request.courseVersionExternalId(), id -> {
      Course fetched = oisApiService.fetchByCourseVersionExternalId(id);
      Course saved = courseRepository.save(fetched);

      Module module = moduleMap.get("vabaained");
      if (module != null) {
        module.getCourses().add(saved);
        moduleRepository.save(module);
        moduleMap.put(saved.getCourseExternalId(), module);
      }

      return saved;
    });

    Semester semester =
        Optional.ofNullable(semesterMap.get(request.semesterExternalId()))
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Semester not found for semesterExternalId: " + request.semesterExternalId()));

    Module module = moduleMap.get(course.getCourseExternalId());

    PlannedCourse plannedCourse = new PlannedCourse();
    plannedCourse.setExternalId(UUID.randomUUID());
    plannedCourse.setCreationDate(LocalDateTime.now());
    plannedCourse.setCourse(course);
    plannedCourse.setSemester(semester);
    plannedCourse.setModule(module);
    plannedCourse.setStatus(request.status() != null ? request.status() : CourseStatus.PLANNED);

    return plannedCourse;
  }
}
