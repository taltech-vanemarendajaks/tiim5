package com.studyplanner.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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
@Table(name = "study_plans")
public class StudyPlan extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String name;

  @Column(nullable = false)
  private Integer completedCredits = 0;

  @Column(nullable = false)
  private LocalDateTime startDate;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "curriculum_id", nullable = false)
  private Curriculum curriculum;

  @OneToMany(mappedBy = "studyPlan", cascade = CascadeType.ALL)
  private List<PlannedCourse> plannedCourses;

  @OneToMany(mappedBy = "studyPlan", cascade = CascadeType.ALL)
  private List<Semester> semesters;

  @Column private LocalDateTime updateDate;
}
