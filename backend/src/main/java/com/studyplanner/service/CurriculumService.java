package com.studyplanner.service;

import com.studyplanner.client.*;
import com.studyplanner.client.dto.*;
import com.studyplanner.dto.*;
import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.entity.*;
import com.studyplanner.entity.Module;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.mapper.*;
import com.studyplanner.mapper.CurriculumMapper;
import com.studyplanner.repository.CurriculumRepository;
import jakarta.transaction.*;
import java.util.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurriculumService {

  private final CurriculumRepository curriculumRepository;
  private final OisClient oisClient;
  private final ModuleService moduleService;
  private final CourseService courseService;

  public List<CurriculumResponse> getAllCurriculums(
      int start, int take, String q, String study_level) {
    List<OisCurriculumResponse> curriculumResponseFromOis =
        oisClient.getAllCurriculums(start, take, q, study_level.toLowerCase());
    return curriculumResponseFromOis.stream().map(CurriculumMapper::mapOisToResponse).toList();
  }

  public List<CurriculumVersionResponse> getVersionsForCurriculum(String curriculumVersionId) {
    List<OisCurriculumVersionPartialResponse> curriculumResponseFromOis =
        oisClient.getAllCurriculumVersions(curriculumVersionId);
    System.out.println(curriculumResponseFromOis);
    return curriculumResponseFromOis.stream()
        .map(
            curriculumVersion ->
                CurriculumVersionResponse.builder()
                    .externalVersionId(curriculumVersion.curriculumVersionId())
                    .year(curriculumVersion.versionYear())
                    .build())
        .toList();
  }

  public CurriculumResponse getCurriculumByStudyPlan(UUID studyPlanExternalId) {
    return CurriculumMapper.mapToResponse(
        curriculumRepository
            .findByStudyPlanExternalId(studyPlanExternalId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "No curriculum found for study plan external id " + studyPlanExternalId)));
  }

  @Transactional
  public Curriculum initalizeCurriculum(UUID curriculumId, UUID curriculumVersionId) {
    Optional<Curriculum> existing =
        curriculumRepository.findByCurriculumVersionExternalId(curriculumVersionId);
    if (existing.isPresent()) {
      return existing.get();
    }

    OisCurriculumVersionResponse oisCurriculumVersionResponse =
        oisClient.getCurriculumVersionById(curriculumVersionId.toString());

    Curriculum curriculum =
        CurriculumMapper.mapOisCurriculumVersionToCurriculum(oisCurriculumVersionResponse);
    curriculum.setCurriculumExternalId(curriculumId);

    AtomicReference<Module> suunamoduulRef = new AtomicReference<>();

    oisCurriculumVersionResponse
        .modules()
        .blocks()
        .forEach(
            block ->
                block
                    .submodules()
                    .forEach(
                        topLevelModule ->
                            collectAndSaveModules(topLevelModule, curriculum, suunamoduulRef)));

    Module suunamodul = suunamoduulRef.get();
    if (suunamodul != null) {
      moduleService.saveModule(suunamodul);
      curriculum.getModules().add(suunamodul);
    }

    return curriculumRepository.save(curriculum);
  }

  private static final String FREE_ELECTIVES_ET = "Vabaainete moodul";
  private static final String FREE_ELECTIVES_EN = "Optional courses";
  private static final String SUUNAMODUL_ET = "Suunamoodul";
  private static final String SUUNAMODUL_EN = "Narrow field module";

  private boolean isFreeElectives(OisModule oisModule) {
    if (oisModule.title() == null) return false;
    return FREE_ELECTIVES_ET.equals(oisModule.title().et())
        || FREE_ELECTIVES_EN.equals(oisModule.title().en());
  }

  private boolean isSuunamodul(OisModule oisModule) {
    if (oisModule.title() == null) return false;
    return SUUNAMODUL_ET.equals(oisModule.title().et())
        || SUUNAMODUL_EN.equals(oisModule.title().en());
  }

  private void collectAndSaveModules(
      OisModule oisModule, Curriculum curriculum, AtomicReference<Module> suunamoduulRef) {

    if (isFreeElectives(oisModule)) {
      Module module = ModuleMapper.OisModuleToModule(oisModule);
      moduleService.saveModule(module);
      curriculum.getModules().add(module);
      return;
    }

    if (isSuunamodul(oisModule)) {
      suunamoduulRef.compareAndSet(null, ModuleMapper.OisModuleToModule(oisModule));
      collectAllCoursesIntoModule(oisModule, suunamoduulRef.get());
      return;
    }

    List<OisModuleCourseResponse> directCourses =
        oisModule.OisModuleCourseResponse() != null
            ? oisModule.OisModuleCourseResponse()
            : List.of();

    if (!directCourses.isEmpty()) {
      Module module = ModuleMapper.OisModuleToModule(oisModule);

      List<UUID> courseUuids =
          directCourses.stream().map(OisModuleCourseResponse::externalId).toList();

      // Getting other course metadata from OIS, mainly latest_version_id-s.
      List<OisCourseResponse> courses = oisClient.getCoursesBatched(courseUuids);

      // Batched response doesn't include semester info; fetch each course individually.
      courses.forEach(
          courseResponse -> {
            UUID externalId = courseResponse.externalId();
            UUID versionId = courseResponse.latestVersion();
            if (externalId != null && versionId != null) {
              module.getCourses().add(courseService.getFromOisAndSaveCourse(externalId, versionId));
            }
          });

      moduleService.saveModule(module);
      curriculum.getModules().add(module);
    }

    if (oisModule.submodules() != null) {
      oisModule.submodules().forEach(sub -> collectAndSaveModules(sub, curriculum, suunamoduulRef));
    }
  }

  private void collectAllCoursesIntoModule(OisModule oisModule, Module target) {
    List<OisModuleCourseResponse> directCourses =
        oisModule.OisModuleCourseResponse() != null
            ? oisModule.OisModuleCourseResponse()
            : List.of();

    if (!directCourses.isEmpty()) {
      List<UUID> courseUuids =
          directCourses.stream().map(OisModuleCourseResponse::externalId).toList();

      List<OisCourseResponse> courses = oisClient.getCoursesBatched(courseUuids);
      courses.forEach(
          courseResponse -> {
            UUID externalId = courseResponse.externalId();
            UUID versionId = courseResponse.latestVersion();
            if (externalId != null && versionId != null) {
              target.getCourses().add(courseService.getFromOisAndSaveCourse(externalId, versionId));
            }
          });
    }

    if (oisModule.submodules() != null) {
      oisModule.submodules().forEach(sub -> collectAllCoursesIntoModule(sub, target));
    }
  }

  public CurriculumResponse addNewCurriculum(UUID curriculumId, UUID curriculumVersionId) {
    Curriculum curriculum = this.initalizeCurriculum(curriculumId, curriculumVersionId);
    return CurriculumMapper.mapToResponse(curriculum);
  }
}
