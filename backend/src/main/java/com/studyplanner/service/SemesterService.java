package com.studyplanner.service;

import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.entity.Semester;
import com.studyplanner.mapper.SemesterMapper;
import com.studyplanner.repository.SemesterRepository;
import com.studyplanner.utils.UserRequestContext;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterService {

  private final SemesterRepository semesterRepository;

  public List<SemesterResponse> getSemesters(UUID studyPlanExternalId) {
    UUID userExternalId = UserRequestContext.getUserExternalId();
    return SemesterMapper.mapToResponseList(
        semesterRepository.findAllByUserAndStudyPlanExternalId(
            userExternalId, studyPlanExternalId));
  }

  public List<Semester> fetchSemestersByStudyPlanExternalId(UUID studyPlanExternalId) {
    return semesterRepository.findAllByStudyPlanExternalId(studyPlanExternalId);
  }

  @Transactional
public void recalculateAndSave(Semester semester) {
    semester.recalculateFinished();
    semesterRepository.save(semester);
}
}
