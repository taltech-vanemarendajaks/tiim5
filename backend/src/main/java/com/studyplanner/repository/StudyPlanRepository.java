package com.studyplanner.repository;

import com.studyplanner.entity.StudyPlan;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
  @Query(
      """
    SELECT sp FROM StudyPlan sp
        JOIN FETCH sp.user
        JOIN FETCH sp.curriculum
    WHERE sp.externalId = :externalId
    """)
  Optional<StudyPlan> findByExternalId(@Param("externalId") UUID externalId);

  @Query(
      "SELECT sp FROM StudyPlan sp JOIN FETCH sp.curriculum WHERE sp.user.externalId = :userExternalId")
  List<StudyPlan> findAllByUserExternalId(@Param("userExternalId") UUID userExternalId);
}
