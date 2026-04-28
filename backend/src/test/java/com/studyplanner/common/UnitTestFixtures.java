package com.studyplanner.common;

import com.studyplanner.client.dto.*;
import com.studyplanner.dto.*;
import com.studyplanner.entity.*;
import com.studyplanner.entity.Module;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UnitTestFixtures {
  public static final UUID A_USER_EXTERNAL_ID =
      UUID.fromString("669b65ca-34bf-4a29-9e5e-1daeedcd4d62");
  public static final UUID A_STUDY_PLAN_EXTERNAL_ID =
      UUID.fromString("02e0d994-905b-47b8-b58f-8f9dfe960816");
  public static final UUID A_MODULE_EXTERNAL_ID =
      UUID.fromString("ee1a8984-3cff-4dc3-9290-458c308378b4");
  public static final UUID A_SEMESTER_EXTERNAL_ID =
      UUID.fromString("f61138b4-7cb9-4b32-bb88-3ea719ed4291");
  public static final UUID AN_EXTERNAL_ID = UUID.fromString("147ffa4f-b56e-40f0-8ce0-266f77ff20c1");
  public static final LocalDateTime A_LOCAL_DATE_TIME = LocalDateTime.of(2024, 8, 13, 15, 30);
  public static final UUID A_COURSE_UUID = UUID.fromString("b99c0bb1-efd4-9b0a-857a-3dc7114e5c19");
  public static final UUID A_LATEST_VERSION_UUID =
      UUID.fromString("814c854e-0af7-fb0a-6c8c-5f255cc4e277");
  public static final String A_TITLE_EN = "Title";
  public static final String A_TITLE_ET = "Nimi";

  public static UserResponse aUserResponse() {
    return UserResponse.builder()
        .externalId(A_USER_EXTERNAL_ID)
        .name("User")
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }

  public static User aUser() {
    return User.builder()
        .externalId(A_USER_EXTERNAL_ID)
        .name("User")
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }

  public static SemesterResponse aSemesterResponse() {
    return SemesterResponse.builder()
        .externalId(A_SEMESTER_EXTERNAL_ID)
        .year(2026)
        .finished(false)
        .plannedCourses(List.of())
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }

  public static Semester aSemester() {
    return Semester.builder()
        .externalId(A_SEMESTER_EXTERNAL_ID)
        .year(2026)
        .finished(false)
        .plannedCourses(List.of())
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }

  public static CourseResponse aCourseResponse() {
    return CourseResponse.builder()
        .externalId(AN_EXTERNAL_ID)
        .versionExternalId(A_LATEST_VERSION_UUID)
        .code("1")
        .titleEn(A_TITLE_EN)
        .titleEt(A_TITLE_ET)
        .credits(6.0)
        .semesterType(SemesterType.SPRING)
        .build();
  }

  public static Course aCourse() {
    return Course.builder()
        .id(1L)
        .externalId(AN_EXTERNAL_ID)
        .courseVersionExternalId(A_LATEST_VERSION_UUID)
        .code("1")
        .titleEn(A_TITLE_EN)
        .titleEt(A_TITLE_ET)
        .credits(6.0)
        .semesterType(SemesterType.SPRING)
        .build();
  }

  public static PlannedCourseResponse aPlannedCourseResponse() {
    return PlannedCourseResponse.builder()
        .externalId(AN_EXTERNAL_ID)
        .course(aCourseResponse())
        .module(aModuleResponse())
        .courseStatus(CourseStatus.PLANNED)
        .build();
  }

  public static PlannedCourse aPlannedCourse() {
    return PlannedCourse.builder()
        .externalId(AN_EXTERNAL_ID)
        .course(aCourse())
        .module(aModule())
        .status(CourseStatus.PLANNED)
        .semester(aSemester())
        .build();
  }

  public static OisVersionResponse aClientVersionResponse() {
    return OisVersionResponse.builder()
        .target(
            new OisTargetResponse(
                new OisTargetResponse.Semester(OisSemesterCode.SPRING, "kevad", "spring")))
        .build();
  }

  public static OisCourseResponse aClientCourseResponse() {
    return OisCourseResponse.builder()
        .externalId(A_COURSE_UUID)
        .code("1")
        .title(new Title(A_TITLE_EN, A_TITLE_ET))
        .credits(6.0)
        .latestVersion(A_LATEST_VERSION_UUID)
        .build();
  }

  public static Curriculum aCurriculum() {
    return Curriculum.builder()
        .id(1L)
        .externalId(AN_EXTERNAL_ID)
        .title(A_TITLE_EN)
        .credits(120)
        .build();
  }

  public static Module aModule() {
    return Module.builder()
        .externalId(AN_EXTERNAL_ID)
        .moduleExternalId(A_MODULE_EXTERNAL_ID)
        .title(A_TITLE_EN)
        .requiredCredits(12)
        .optionalCredits(0)
        .courses(List.of(aCourse()))
        .curriculums(List.of(aCurriculum()))
        .build();
  }

  public static Module aModule(String title) {
    return Module.builder()
        .externalId(A_MODULE_EXTERNAL_ID)
        .moduleExternalId(AN_EXTERNAL_ID)
        .title(title)
        .requiredCredits(18)
        .optionalCredits(0)
        .courses(List.of(aCourse()))
        .curriculums(List.of(aCurriculum()))
        .build();
  }

  public static ModuleResponse aModuleResponse() {
    return ModuleResponse.builder()
        .externalId(AN_EXTERNAL_ID)
        .title(A_TITLE_EN)
        .requiredCredits(12)
        .optionalCredits(0)
        .build();
  }

  public static PlannedCourseRequest aPlannedCourseRequest(CourseStatus status) {
    return PlannedCourseRequest.builder()
        .courseVersionExternalId(A_LATEST_VERSION_UUID)
        .courseOisExternalId(A_COURSE_UUID)
        .semesterExternalId(A_SEMESTER_EXTERNAL_ID)
        .status(status)
        .build();
  }

  public static PlannedCourseRequest aPlannedCourseRequest() {
    return aPlannedCourseRequest(CourseStatus.PLANNED);
  }

  public static StudyPlan aStudyPlan() {
    return StudyPlan.builder()
        .externalId(A_STUDY_PLAN_EXTERNAL_ID)
        .completedCredits(36.0)
        .user(aUser())
        .curriculum(aCurriculum())
        .semesters(List.of(aSemester()))
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }

  public static OisCourseFullResponse aOisCourseFullResponse() {
    return OisCourseFullResponse.builder()
        .externalId(AN_EXTERNAL_ID)
        .code("123")
        .credits(6.0)
        .title(new Title(A_TITLE_EN, A_TITLE_ET))
        .latestVersion(A_LATEST_VERSION_UUID)
        .target(aClientVersionResponse().target())
        .build();
  }

  public static CurriculumResponse aCurriculumResponse() {
    return CurriculumResponse.builder()
        .externalId(AN_EXTERNAL_ID)
        .title(A_TITLE_EN)
        .credits(120)
        .build();
  }

  public static StudyPlanResponse aStudyPlanResponse() {
    return StudyPlanResponse.builder()
        .externalId(A_STUDY_PLAN_EXTERNAL_ID)
        .completedCredits(36.0)
        .curriculum(aCurriculumResponse())
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }
}
