package com.studyplanner.repository;

import com.studyplanner.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByExternalId(UUID externalId);
}
