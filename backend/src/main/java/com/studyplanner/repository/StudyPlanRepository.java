package com.studyplanner.repository;

import com.studyplanner.entity.StudyPlan;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
  Optional<StudyPlan> findByExternalId(UUID studyPlanExternalId);
}
