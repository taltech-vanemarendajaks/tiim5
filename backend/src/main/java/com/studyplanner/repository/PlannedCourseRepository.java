package com.studyplanner.repository;

import com.studyplanner.entity.PlannedCourse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PlannedCourseRepository extends JpaRepository<PlannedCourse, Long> {
  @Modifying
  @Query(
      """
    DELETE FROM PlannedCourse pc
    WHERE pc.semester.studyPlan.externalId = :studyPlanExternalId
    """)
  void deleteByStudyPlanExternalId(UUID studyPlanExternalId);

  @Query(
      """
    SELECT pc FROM PlannedCourse pc
    JOIN FETCH pc.semester s
    JOIN FETCH s.plannedCourses
    WHERE pc.externalId = :externalId
    """)
  Optional<PlannedCourse> findByExternalIdWithSemesterAndCourses(UUID externalId);
}
