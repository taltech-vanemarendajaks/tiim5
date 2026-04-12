package com.studyplanner.repository;

import com.studyplanner.entity.Module;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModuleRepository extends JpaRepository<Module, Long> {
  Optional<Module> findByTitleAndCurriculums_ExternalId(String title, UUID curriculumExternalId);

  @Query(
      """
    SELECT DISTINCT m
    FROM Module m
        LEFT JOIN FETCH m.curriculums
        LEFT JOIN m.courses c
    WHERE c.id IN :courseIds
    """)
  List<Module> findModulesWithCurriculums(@Param("courseIds") List<Long> courseIds);
}
