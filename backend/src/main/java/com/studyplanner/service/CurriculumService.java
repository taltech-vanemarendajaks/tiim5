package com.studyplanner.service;

import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.mapper.CurriculumMapper;
import com.studyplanner.repository.CurriculumRepository;
import com.studyplanner.utils.UserRequestContext;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurriculumService {

  private final CurriculumRepository curriculumRepository;

  public CurriculumResponse getCurriculum() {
    UUID userExternalId = UserRequestContext.getUserExternalId();
    return CurriculumMapper.mapToResponse(
        curriculumRepository.findByUserExternalId(userExternalId));
  }
}
