package com.studyplanner.entity;

import jakarta.persistence.*;
import java.util.List;
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
@Table(name = "semesters")
public class Semester extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Integer year;

  @Column(nullable = false)
  private Boolean finished;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private SemesterType semesterType;

  @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PlannedCourse> plannedCourses;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "study_plan_id")
  private StudyPlan studyPlan;
}
