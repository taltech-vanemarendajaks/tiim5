package com.studyplanner.entity;

import jakarta.persistence.*;
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
@Table(name = "courses")
public class Course extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Description("Course UUID from õis API")
  private UUID courseExternalId;

  @Column(nullable = false)
  @Description("Course version UUID from õis API")
  private UUID courseVersionExternalId;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer credits;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private SemesterType semesterType;

  @ManyToMany(mappedBy = "courses")
  private List<Module> modules;
}
