package com.studyplanner.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getAllCourses_withDefaults_returnsOk() throws Exception {
    mockMvc.perform(get("/courses")).andExpect(status().isOk());
  }

  @Test
  void getAllCourses_withQueryParams_returnsOk() throws Exception {
    mockMvc
        .perform(
            get("/courses")
                .param("start", "1")
                .param("take", "300")
                .param("title", "Bakalaureusetöö")
                .param("code", "SORG.00.004"))
        .andExpect(status().isOk());
  }
}
