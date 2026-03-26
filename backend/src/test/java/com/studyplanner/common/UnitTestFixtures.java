package com.studyplanner.common;

import com.studyplanner.client.dto.CourseResponse;
import com.studyplanner.client.dto.CourseVersionResponse;
import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.dto.UserResponse;
import com.studyplanner.entity.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UnitTestFixtures {
  public static final UUID A_USER_EXTERNAL_ID =
      UUID.fromString("669b65ca-34bf-4a29-9e5e-1daeedcd4d62");
  public static final UUID A_STUDY_PLAN_EXTERNAL_ID =
      UUID.fromString("02e0d994-905b-47b8-b58f-8f9dfe960816");
  public static final UUID AN_EXTERNAL_ID = UUID.fromString("147ffa4f-b56e-40f0-8ce0-266f77ff20c1");
  public static final LocalDateTime A_LOCAL_DATE_TIME = LocalDateTime.of(2024, 8, 13, 15, 30);
  public static final UUID A_COURSE_UUID = UUID.fromString("b99c0bb1-efd4-9b0a-857a-3dc7114e5c19");

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
        .externalId(AN_EXTERNAL_ID)
        .year(2026)
        .finished(false)
        .plannedCourses(List.of())
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }

  public static Semester aSemester() {
    return Semester.builder()
        .externalId(AN_EXTERNAL_ID)
        .year(2026)
        .finished(false)
        .plannedCourses(List.of())
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }

  public static CourseResponse aCourseResponse() {
    return CourseResponse.builder()
        .externalId(AN_EXTERNAL_ID)
        .code("1")
        .title(new CourseResponse.Title("Course", "Kursus"))
        .credits(6.0)
        .semesters(List.of(SemesterType.SPRING))
        .build();
  }

  public static Course aCourse() {
    return Course.builder()
        .externalId(AN_EXTERNAL_ID)
        .code("1")
        .titleEn("Course")
        .titleEt("Kursus")
        .credits(6.0)
        .semesterType(List.of(SemesterType.SPRING))
        .build();
  }

  public static PlannedCourseResponse aPlannedCourseResponse() {
    return PlannedCourseResponse.builder()
        .externalId(AN_EXTERNAL_ID)
        .course(aCourseResponse())
        .build();
  }

  public static PlannedCourse aPlannedCourse() {
    return PlannedCourse.builder().externalId(AN_EXTERNAL_ID).course(aCourse()).build();
  }

  public static CourseVersionResponse aCourseVersionResponse() {
    return CourseVersionResponse.builder()
        .target(
            new CourseVersionResponse.Target(
                new CourseVersionResponse.Target.Semester("spring", "kevad", "spring")))
        .build();
  }
}
