package com.studyplanner.service;

import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.mapper.CurriculumMapper;
import com.studyplanner.repository.CurriculumRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurriculumService {

  private final CurriculumRepository curriculumRepository;

  public CurriculumResponse getCurriculumByStudyPlan(UUID studyPlanExternalId) {
    return CurriculumMapper.mapToResponse(
        curriculumRepository.findByStudyPlanExternalId(studyPlanExternalId));
  }
}
