package com.studyplanner.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InitialController.class)
class InitialControllerTest {
  @Autowired MockMvc mockMvc;

  @Test
  void hello_defaultsToWorld() throws Exception {
    mockMvc
        .perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello World!"));
  }

  @Test
  void hello_usesProvidedName() throws Exception {
    mockMvc
        .perform(get("/hello").param("name", "StudyPlanner"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello StudyPlanner!"));
  }
}
