package com.studyplanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "planned_courses")
public class PlannedCourse extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "module_id")
  private Module module;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "semester_id")
  private Semester semester;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CourseStatus status;
}
