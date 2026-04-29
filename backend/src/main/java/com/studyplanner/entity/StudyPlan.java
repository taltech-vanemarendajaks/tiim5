package com.studyplanner.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
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
  private Double completedCredits = 0.0;

  @Column(nullable = false)
  private LocalDateTime startDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "curriculum_id", nullable = false)
  private Curriculum curriculum;

  @OneToMany(mappedBy = "studyPlan", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Semester> semesters;

  @Column private LocalDateTime updateDate;

  public void recalculateCompletedCredits(Collection<PlannedCourse> plannedCourses) {
    if (plannedCourses == null || plannedCourses.isEmpty()) {
      this.completedCredits = 0.0;
      return;
    }
    this.completedCredits =
        plannedCourses.stream()
            .filter(plannedCourse -> plannedCourse.getStatus() == CourseStatus.COMPLETED)
            .mapToDouble(plannedCourse -> plannedCourse.getCourse().getCredits())
            .sum();
  }
}
