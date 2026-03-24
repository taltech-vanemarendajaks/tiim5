package com.studyplanner.repository;

import com.studyplanner.entity.Semester;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

  @Query(
"""
    SELECT s
    FROM Semester s
        JOIN FETCH s.studyPlan sp
        JOIN FETCH sp.user
        LEFT JOIN FETCH s.plannedCourses pc
        LEFT JOIN FETCH pc.course
    WHERE sp.user.externalId = :userExternalId
      AND sp.externalId = :studyPlanExternalId
""")
  List<Semester> findAllByUserAndStudyPlanExternalId(
      @Param("userExternalId") UUID userExternalId,
      @Param("studyPlanExternalId") UUID studyPlanExternalId);
}
