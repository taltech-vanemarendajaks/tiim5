package com.studyplanner.repository;

import com.studyplanner.entity.Module;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModuleRepository extends JpaRepository<Module, Long> {
  @Query(
      """
            SELECT m FROM Module m
            JOIN FETCH m.courses co
            JOIN m.curriculums c
            WHERE c.externalId = :curriculumExternalId
            """)
  List<Module> findAllByCurriculumExternalId(
      @Param("curriculumExternalId") UUID curriculumExternalId);

  Optional<Module> findByTitleAndCurriculums_ExternalId(String title, UUID curriculumExternalId);
}
