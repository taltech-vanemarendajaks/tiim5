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
  void initalizeCurriculum_freeElectivesModule_savedDirectlyWithoutLoadingCourses() {
    var oisModule =
        new OisModule(
            new Title("Optional courses", "Vabaainete moodul"),
            6,
            0,
            List.of(new OisModuleCourseResponse(UUID.randomUUID(), false)),
            null,
            A_MODULE_EXTERNAL_ID);

    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.empty());
    when(oisClient.getCurriculumVersionById(A_CURRICULUM_VERSION_UUID.toString()))
        .thenReturn(aCurriculumVersionResponse(List.of(oisModule)));
    when(curriculumRepository.save(any(Curriculum.class))).thenAnswer(inv -> inv.getArgument(0));

    curriculumService.initalizeCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    verify(moduleService).saveModule(any());
    verify(courseService, never()).getFromOisAndSaveCourse(any(), any());
    verify(oisClient, never()).getCoursesBatched(any());
  }

  @Test
  void initalizeCurriculum_suunamodul_aggregatesAllSubmoduleCoursesIntoSingleModule() {
    var courseUuid1 = UUID.randomUUID();
    var courseUuid2 = UUID.randomUUID();
    var versionUuid1 = UUID.randomUUID();
    var versionUuid2 = UUID.randomUUID();

    var submodule1 =
        new OisModule(
            new Title("Track sub 1", "Suuna alam 1"),
            6,
            0,
            List.of(new OisModuleCourseResponse(courseUuid1, true)),
            null,
            UUID.randomUUID());
    var submodule2 =
        new OisModule(
            new Title("Track sub 2", "Suuna alam 2"),
            6,
            0,
            List.of(new OisModuleCourseResponse(courseUuid2, true)),
            null,
            UUID.randomUUID());
    var suunamodul =
        new OisModule(
            new Title("Narrow field module", "Suunamoodul"),
            12,
            0,
            null,
            List.of(submodule1, submodule2),
            A_MODULE_EXTERNAL_ID);

    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.empty());
    when(oisClient.getCurriculumVersionById(A_CURRICULUM_VERSION_UUID.toString()))
        .thenReturn(aCurriculumVersionResponse(List.of(suunamodul)));
    when(oisClient.getCoursesBatched(List.of(courseUuid1)))
        .thenReturn(
            List.of(
                OisCourseResponse.builder()
                    .externalId(courseUuid1)
                    .latestVersion(versionUuid1)
                    .code("S1")
                    .credits(6.0)
                    .title(new Title("Track Course 1", "Suuna kursus 1"))
                    .build()));
    when(oisClient.getCoursesBatched(List.of(courseUuid2)))
        .thenReturn(
            List.of(
                OisCourseResponse.builder()
                    .externalId(courseUuid2)
                    .latestVersion(versionUuid2)
                    .code("S2")
                    .credits(6.0)
                    .title(new Title("Track Course 2", "Suuna kursus 2"))
                    .build()));
    when(courseService.getFromOisAndSaveCourse(courseUuid1, versionUuid1)).thenReturn(aCourse());
    when(courseService.getFromOisAndSaveCourse(courseUuid2, versionUuid2)).thenReturn(aCourse());
    when(curriculumRepository.save(any(Curriculum.class))).thenAnswer(inv -> inv.getArgument(0));

    curriculumService.initalizeCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    // All submodule courses are collected but only one Module entity is saved
    verify(moduleService, times(1)).saveModule(any());
    verify(courseService, times(2)).getFromOisAndSaveCourse(any(), any());
  }

  @Test
  void initalizeCurriculum_multipleSuunamodulOccurrences_allCoursesAggregatedIntoOneModule() {
    var courseUuid1 = UUID.randomUUID();
    var courseUuid2 = UUID.randomUUID();
    var versionUuid1 = UUID.randomUUID();
    var versionUuid2 = UUID.randomUUID();

    // Two top-level suunamodul entries (e.g. different specialization tracks)
    var suunamodul1 =
        new OisModule(
            new Title("Narrow field module", "Suunamoodul"),
            12,
            0,
            List.of(new OisModuleCourseResponse(courseUuid1, true)),
            null,
            A_MODULE_EXTERNAL_ID);
    var suunamodul2 =
        new OisModule(
            new Title("Narrow field module", "Suunamoodul"),
            12,
            0,
            List.of(new OisModuleCourseResponse(courseUuid2, true)),
            null,
            UUID.randomUUID());

    when(curriculumRepository.findByCurriculumVersionExternalId(A_CURRICULUM_VERSION_UUID))
        .thenReturn(Optional.empty());
    when(oisClient.getCurriculumVersionById(A_CURRICULUM_VERSION_UUID.toString()))
        .thenReturn(aCurriculumVersionResponse(List.of(suunamodul1, suunamodul2)));
    when(oisClient.getCoursesBatched(List.of(courseUuid1)))
        .thenReturn(
            List.of(
                OisCourseResponse.builder()
                    .externalId(courseUuid1)
                    .latestVersion(versionUuid1)
                    .code("T1")
                    .credits(6.0)
                    .title(new Title("Track 1", "Raja 1"))
                    .build()));
    when(oisClient.getCoursesBatched(List.of(courseUuid2)))
        .thenReturn(
            List.of(
                OisCourseResponse.builder()
                    .externalId(courseUuid2)
                    .latestVersion(versionUuid2)
                    .code("T2")
                    .credits(6.0)
                    .title(new Title("Track 2", "Raja 2"))
                    .build()));
    when(courseService.getFromOisAndSaveCourse(courseUuid1, versionUuid1)).thenReturn(aCourse());
    when(courseService.getFromOisAndSaveCourse(courseUuid2, versionUuid2)).thenReturn(aCourse());
    when(curriculumRepository.save(any(Curriculum.class))).thenAnswer(inv -> inv.getArgument(0));

    curriculumService.initalizeCurriculum(A_CURRICULUM_UUID, A_CURRICULUM_VERSION_UUID);

    // Both suunamodul occurrences collapse into one saved module
    verify(moduleService, times(1)).saveModule(any());
    verify(courseService, times(2)).getFromOisAndSaveCourse(any(), any());
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
