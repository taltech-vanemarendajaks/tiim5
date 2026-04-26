package com.studyplanner.repository;

import com.studyplanner.entity.Curriculum;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
  @Query(
      "SELECT c FROM Curriculum c JOIN c.studyPlans sp WHERE sp.externalId = :studyPlanExternalId")
  Curriculum findByStudyPlanExternalId(@Param("studyPlanExternalId") UUID studyPlanExternalId);
}
