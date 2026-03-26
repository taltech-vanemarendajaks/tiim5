package com.studyplanner.controller;

import static com.studyplanner.common.UnitTestFixtures.aCourseResponse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.studyplanner.service.CourseService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CourseService courseService;

  @Test
  void getAllCourses_returnsListOfCourses() throws Exception {

    when(courseService.getAllCourses()).thenReturn(List.of(aCourseResponse()));

    mockMvc
        .perform(get("/api/v1/courses"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").value("1"))
        .andExpect(jsonPath("$[0].semesters[0]").value("SPRING"));
  }
}
