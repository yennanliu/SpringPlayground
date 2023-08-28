package com.yen.SpringAssignmentSystem.repository;

// https://youtu.be/1Mn1AFs8eDo?si=SOzk5A6bwo-vlAki&t=193

import com.yen.SpringAssignmentSystem.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 *  Syntax:
 *   your_repo extends JpaRepository<Entity_class, ID_type>;
 *
 *   springAssignmentSystem/backend/SpringAssignmentSystem/src/main/java/com/yen/SpringAssignmentSystem/domain/User.java
 *
 *   https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/back-end/src/main/java/com/coderscampus/AssignmentSubmissionApp/repository/UserRepository.java
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM users u INNER JOIN authority a on u.id = a.user_id " +
            "WHERE u.cohort_start_date is null", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);
}
