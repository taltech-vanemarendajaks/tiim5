package com.studyplanner.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InitialControllerTest {
  @LocalServerPort private int port;

  @Autowired private RestTestClient client;

  @Test
  void helloEndpointWorks() {
    client
        .get()
        .uri("http://localhost:" + port + "/hello")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("Hello World!");
  }
}
