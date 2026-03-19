package com.studyplanner.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import jdk.jfr.Description;
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
@Table(name = "curriculums")
public class Curriculum extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Description("Curriculum UUID from õis API")
  private UUID curriculumExternalId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private StudyLevel studyLevel;

  @Column(nullable = false)
  private Integer credits;

  @Column(nullable = false)
  private Integer completedCredits = 0;

  @Column(nullable = false)
  private LocalDateTime startDate;

  @OneToMany(mappedBy = "curriculum")
  private List<StudyPlan> studyPlans;

  @ManyToMany
  @JoinTable(
      name = "curriculum_modules",
      joinColumns = @JoinColumn(name = "curriculum_id"),
      inverseJoinColumns = @JoinColumn(name = "module_id"))
  private List<Module> modules;
}
