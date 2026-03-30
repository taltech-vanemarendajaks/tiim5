package com.studyplanner.repository;

import com.studyplanner.entity.Curriculum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
  Optional<Curriculum> findByStudyPlansExternalId(UUID studyPlanExternalId);
}
