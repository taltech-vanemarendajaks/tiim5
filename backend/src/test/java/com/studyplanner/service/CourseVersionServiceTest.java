package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.AN_EXTERNAL_ID;
import static com.studyplanner.common.UnitTestFixtures.aCourseVersionResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.studyplanner.client.ApiClient;
import com.studyplanner.client.dto.CourseVersionResponse;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CourseVersionServiceTest {

  @Mock private ApiClient apiClient;

  @InjectMocks private CourseVersionService courseVersionService;

  @Test
  void getCourseVersionsTest() {
    var courseVersionResponse = List.of(aCourseVersionResponse());

    when(apiClient.getCourseVersions(AN_EXTERNAL_ID)).thenReturn(courseVersionResponse);

    List<CourseVersionResponse> actual = courseVersionService.getCourseVersions(AN_EXTERNAL_ID);

    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).target().semester().code()).isEqualTo("spring");
  }
}
