package com.studyplanner.repository;

import com.studyplanner.entity.Course;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
  List<Course> findAllByCourseVersionExternalIdIn(List<UUID> versionIds);

  Optional<Course> findByCourseVersionExternalId(UUID courseVersionExternalId);

}
