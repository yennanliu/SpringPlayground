package com.yen.SpringAssignmentSystem.repository;

import com.yen.SpringAssignmentSystem.domain.Assignment;
import com.yen.SpringAssignmentSystem.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // TODO : check is this necessary ?
    Set<Assignment> findByUser(User user);

    @Query(value = "select a.* from assignment a where (a.status = 'submitted' or a.status = 'resubmitted')",
            nativeQuery = true)
    Set<Assignment> findByCodeReviewer(User codeReviewer);

    @Query(value = "select a.* from assignment a join users u on a.user_id = u.id where u.cohort_start_date is not null",
            nativeQuery = true)
    List<Assignment> findAllActiveBootcampStudents();

}
