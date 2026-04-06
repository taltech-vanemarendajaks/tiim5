package com.studyplanner.repository;

import com.studyplanner.entity.Module;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModuleRepository extends JpaRepository<Module, Long> {
  Optional<Module> findByTitleAndCurriculums_ExternalId(String title, UUID curriculumExternalId);

  @Query("SELECT m FROM Module m JOIN m.courses c WHERE c.id = :courseId")
  List<Module> findByCourseId(Long courseId);
}
