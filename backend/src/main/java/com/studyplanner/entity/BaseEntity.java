package com.studyplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public class BaseEntity implements Serializable {
  @Column(nullable = false)
  private UUID externalId;

  @Column(nullable = false)
  @CreatedDate
  private LocalDateTime creationDate;
}
