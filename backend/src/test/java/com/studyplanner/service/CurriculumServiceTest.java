package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.studyplanner.client.OisClient;
import com.studyplanner.client.dto.*;
import com.studyplanner.entity.Curriculum;
import com.studyplanner.repository.CurriculumRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurriculumServiceTest {

  private static final UUID A_CURRICULUM_UUID =
      UUID.fromString("147ffa4f-b56e-40f0-8ce0-266f77ff20c1");
  private static final UUID A_CURRICULUM_VERSION_UUID =
      UUID.fromString("c3d4a1b2-f6e5-9078-cdab-ef1234567890");

  @Mock private CurriculumRepository curriculumRepository;
  @Mock private OisClient oisClient;
  @Mock private ModuleService moduleService;
  @Mock private CourseService courseService;
  @InjectMocks private CurriculumService curriculumService;

  @Test
  void getCurriculumTest() {
    var curriculum = aCurriculum();
    var curriculumResponse = aCurriculumResponse();

    when(curriculumRepository.findByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(Optional.ofNullable(curriculum));

    var actual = curriculumService.getCurriculumByStudyPlan(A_STUDY_PLAN_EXTERNAL_ID);

    assertEquals(curriculumResponse, actual);
  }

  @Test
  void initalizeCurriculum_whenCurriculumAlreadyExists_returnsExistingWithoutReprocessing() {
    var existingCurriculum = aCurriculum();
    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.of(existingCurriculum));

    var result =
        curriculumService.initalizeCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    assertEquals(existingCurriculum, result);
    verify(oisClient, never()).getCurriculumVersionById(any());
    verify(curriculumRepository, never()).save(any());
  }

  @Test
  void initalizeCurriculum_moduleWithDirectCourses_savesAllCourses() {
    var courseUuid1 = UUID.randomUUID();
    var courseUuid2 = UUID.randomUUID();
    var versionUuid1 = UUID.randomUUID();
    var versionUuid2 = UUID.randomUUID();

    var directCourses =
        List.of(
            new OisModuleCourseResponse(courseUuid1, true),
            new OisModuleCourseResponse(courseUuid2, false));
    var oisModule =
        new OisModule(
            new Title("Module", "Moodul"), 12, 0, directCourses, null, A_MODULE_EXTERNAL_ID);

    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.empty());
    when(oisClient.getCurriculumVersionById(A_CURRICULUM_VERSION_UUID.toString()))
        .thenReturn(aCurriculumVersionResponse(List.of(oisModule)));
    when(oisClient.getCoursesBatched(List.of(courseUuid1, courseUuid2)))
        .thenReturn(
            List.of(
                OisCourseResponse.builder()
                    .externalId(courseUuid1)
                    .latestVersion(versionUuid1)
                    .code("1")
                    .credits(6.0)
                    .title(new Title("C1", "K1"))
                    .build(),
                OisCourseResponse.builder()
                    .externalId(courseUuid2)
                    .latestVersion(versionUuid2)
                    .code("2")
                    .credits(3.0)
                    .title(new Title("C2", "K2"))
                    .build()));
    when(courseService.getFromOisAndSaveCourse(courseUuid1, versionUuid1)).thenReturn(aCourse());
    when(courseService.getFromOisAndSaveCourse(courseUuid2, versionUuid2)).thenReturn(aCourse());
    when(curriculumRepository.save(any(Curriculum.class))).thenAnswer(inv -> inv.getArgument(0));

    curriculumService.initalizeCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    verify(courseService, times(2)).getFromOisAndSaveCourse(any(), any());
    verify(moduleService).saveModule(any());
  }

  @Test
  void initalizeCurriculum_moduleWithSubmodulesOnly_collectsCoursesFromSubmodules() {
    var courseUuid = UUID.randomUUID();
    var versionUuid = UUID.randomUUID();

    var submodule =
        new OisModule(
            new Title("Sub", "Alammoodul"),
            6,
            0,
            List.of(new OisModuleCourseResponse(courseUuid, true)),
            null,
            UUID.randomUUID());
    var oisModule =
        new OisModule(
            new Title("Module", "Moodul"), 12, 0, null, List.of(submodule), A_MODULE_EXTERNAL_ID);

    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.empty());
    when(oisClient.getCurriculumVersionById(A_CURRICULUM_VERSION_UUID.toString()))
        .thenReturn(aCurriculumVersionResponse(List.of(oisModule)));
    when(oisClient.getCoursesBatched(List.of(courseUuid)))
        .thenReturn(
            List.of(
                OisCourseResponse.builder()
                    .externalId(courseUuid)
                    .latestVersion(versionUuid)
                    .code("1")
                    .credits(6.0)
                    .title(new Title("C1", "K1"))
                    .build()));
    when(courseService.getFromOisAndSaveCourse(courseUuid, versionUuid)).thenReturn(aCourse());
    when(curriculumRepository.save(any(Curriculum.class))).thenAnswer(inv -> inv.getArgument(0));

    curriculumService.initalizeCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    verify(courseService).getFromOisAndSaveCourse(courseUuid, versionUuid);
    verify(moduleService).saveModule(any());
  }

  @Test
  void initalizeCurriculum_moduleWithDirectAndSubmoduleCourses_combinesAllCourses() {
    var directCourseUuid = UUID.randomUUID();
    var submoduleCourseUuid = UUID.randomUUID();
    var directVersionUuid = UUID.randomUUID();
    var submoduleVersionUuid = UUID.randomUUID();

    var submodule =
        new OisModule(
            new Title("Sub", "Alammoodul"),
            6,
            0,
            List.of(new OisModuleCourseResponse(submoduleCourseUuid, false)),
            null,
            UUID.randomUUID());
    var oisModule =
        new OisModule(
            new Title("Module", "Moodul"),
            18,
            0,
            List.of(new OisModuleCourseResponse(directCourseUuid, true)),
            List.of(submodule),
            A_MODULE_EXTERNAL_ID);

    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.empty());
    when(oisClient.getCurriculumVersionById(A_CURRICULUM_VERSION_UUID.toString()))
        .thenReturn(aCurriculumVersionResponse(List.of(oisModule)));
    when(oisClient.getCoursesBatched(List.of(directCourseUuid, submoduleCourseUuid)))
        .thenReturn(
            List.of(
                OisCourseResponse.builder()
                    .externalId(directCourseUuid)
                    .latestVersion(directVersionUuid)
                    .code("1")
                    .credits(6.0)
                    .title(new Title("D", "D"))
                    .build(),
                OisCourseResponse.builder()
                    .externalId(submoduleCourseUuid)
                    .latestVersion(submoduleVersionUuid)
                    .code("2")
                    .credits(3.0)
                    .title(new Title("S", "S"))
                    .build()));
    when(courseService.getFromOisAndSaveCourse(directCourseUuid, directVersionUuid))
        .thenReturn(aCourse());
    when(courseService.getFromOisAndSaveCourse(submoduleCourseUuid, submoduleVersionUuid))
        .thenReturn(aCourse());
    when(curriculumRepository.save(any(Curriculum.class))).thenAnswer(inv -> inv.getArgument(0));

    curriculumService.initalizeCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    verify(courseService, times(2)).getFromOisAndSaveCourse(any(), any());
    verify(moduleService).saveModule(any());
  }

  @Test
  void initalizeCurriculum_moduleWithNoCourses_skipsModule() {
    var oisModule =
        new OisModule(new Title("Empty", "Tühi"), 0, 0, null, null, A_MODULE_EXTERNAL_ID);

    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.empty());
    when(oisClient.getCurriculumVersionById(A_CURRICULUM_VERSION_UUID.toString()))
        .thenReturn(aCurriculumVersionResponse(List.of(oisModule)));
    when(curriculumRepository.save(any(Curriculum.class))).thenAnswer(inv -> inv.getArgument(0));

    curriculumService.initalizeCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    verify(courseService, never()).getFromOisAndSaveCourse(any(), any());
    verify(moduleService, never()).saveModule(any());
  }

  @Test
  void getVersionsForCurriculum_returnsMappedVersions() {
    var partialVersions =
        List.of(
            new OisCurriculumVersionPartialResponse(A_CURRICULUM_VERSION_UUID, 2024),
            new OisCurriculumVersionPartialResponse(UUID.randomUUID(), 2023));
    when(oisClient.getAllCurriculumVersions(A_CURRICULUM_UUID.toString()))
        .thenReturn(partialVersions);

    var result = curriculumService.getVersionsForCurriculum(A_CURRICULUM_UUID.toString());

    assertEquals(2, result.size());
    assertEquals(A_CURRICULUM_VERSION_UUID, result.get(0).externalVersionId());
    assertEquals(2024, result.get(0).year());
  }

  @Test
  void addNewCurriculum_whenCurriculumAlreadyExists_returnsResponseWithoutCreating() {
    var existingCurriculum = aCurriculum();
    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.of(existingCurriculum));

    var result = curriculumService.addNewCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    assertEquals(AN_EXTERNAL_ID, result.externalId());
    verify(curriculumRepository, never()).save(any());
  }

  private static OisCurriculumVersionResponse aCurriculumVersionResponse(List<OisModule> modules) {
    return new OisCurriculumVersionResponse(
        A_CURRICULUM_VERSION_UUID,
        new Title("Curriculum", "Õppekava"),
        240,
        new Classification(new OisStudyLevel("bachelor")),
        new ModulesWrapper(List.of(new Block(modules))));
  }
}
