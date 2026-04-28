package com.studyplanner.entity;

import jakarta.persistence.*;
import java.util.*;
import jdk.jfr.Description;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "modules")
public class Module extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Description("Module UUID from õis API")
  private UUID moduleExternalId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private Integer requiredCredits;

  @Column(nullable = false)
  private Integer optionalCredits;

  @ManyToMany(mappedBy = "modules")
  private List<Curriculum> curriculums;

  @ManyToMany
  @JoinTable(
      name = "module_courses",
      joinColumns = @JoinColumn(name = "module_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
  private List<Course> courses = new ArrayList<>();
}
