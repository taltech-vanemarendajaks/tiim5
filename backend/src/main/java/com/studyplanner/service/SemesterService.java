package com.studyplanner.service;

import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.mapper.SemesterMapper;
import com.studyplanner.repository.SemesterRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterService {

  private final SemesterRepository semesterRepository;

  public List<SemesterResponse> getUserSemesters(UUID userExternalId) {
    return SemesterMapper.mapToResponseList(
        semesterRepository.findAllByUserExternalId(userExternalId));
  }
}
