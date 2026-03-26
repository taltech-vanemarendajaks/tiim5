package com.studyplanner.controller;

import static com.studyplanner.common.UnitTestFixtures.AN_EXTERNAL_ID;
import static com.studyplanner.common.UnitTestFixtures.aCourseVersionResponse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.studyplanner.service.CourseVersionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseVersionController.class)
public class CourseVersionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CourseVersionService courseVersionService;

  @Test
  void getAllCourseVersions_returnsListOfCourseVersions() throws Exception {

    when(courseVersionService.getCourseVersions(AN_EXTERNAL_ID))
        .thenReturn(List.of(aCourseVersionResponse()));

    mockMvc
        .perform(get("/api/v1/courses/{courseUuid}/versions", AN_EXTERNAL_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].target.semester.code").value("spring"));
  }
}
