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

    // traversing modules for courseIDs
    oisCurriculumVersionResponse
        .modules()
        .blocks()
        .forEach(
            submodules -> {
              List<OisModule> moduleList = submodules.submodules();

              moduleList.forEach(
                  oisModule -> {
                    List<OisModuleCourseResponse> courseResponses = new ArrayList<>();

                    if (oisModule.OisModuleCourseResponse() != null) {
                      courseResponses.addAll(oisModule.OisModuleCourseResponse());
                    }

                    if (oisModule.submodules() != null) {
                      oisModule
                          .submodules()
                          .forEach(
                              submodule -> {
                                if (submodule.OisModuleCourseResponse() != null) {
                                  courseResponses.addAll(submodule.OisModuleCourseResponse());
                                }
                              });
                    }

                    if (courseResponses.isEmpty()) {
                      return;
                    }

                    Module module = ModuleMapper.OisModuleToModule(oisModule);

                    List<UUID> courseUuids =
                        courseResponses.stream().map(OisModuleCourseResponse::externalId).toList();

                    // Getting other course metadata from ois, but mainly latest_version_id-s.
                    List<OisCourseResponse> courses = oisClient.getCoursesBatched(courseUuids);

                    // Batched course response doesn't include semester info
                    // so we need to make another call for each individual course
                    courses.forEach(
                        courseResponse -> {
                          UUID externalId = courseResponse.externalId();
                          UUID versionId = courseResponse.latestVersion();

                          if (externalId != null && versionId != null) {
                            module
                                .getCourses()
                                .add(courseService.getFromOisAndSaveCourse(externalId, versionId));
                          }
                        });

                    moduleService.saveModule(module);
                    curriculum.getModules().add(module);
                  });
            });

    return curriculumRepository.save(curriculum);
  }

  public CurriculumResponse addNewCurriculum(UUID curriculumId, UUID curriculumVersionId) {
    Curriculum curriculum = this.initalizeCurriculum(curriculumId, curriculumVersionId);
    return CurriculumMapper.mapToResponse(curriculum);
  }
}
