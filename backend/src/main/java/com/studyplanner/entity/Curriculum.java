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
    private StudyLevel studyLevel;

    @Column(nullable = false)
    private Integer credits;

    @Column
    private Integer completedCredits;

    @OneToMany(mappedBy = "curriculum")
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "curriculum_module",
            joinColumns = @JoinColumn(name = "curriculum_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private List<Module> modules;
}
