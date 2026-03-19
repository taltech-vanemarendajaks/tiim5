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
            WHERE s.studyPlan.user.externalId = :externalId
            ORDER BY s.id
        """)
  List<Semester> findAllByUserExternalId(@Param("externalId") UUID externalId);
}
