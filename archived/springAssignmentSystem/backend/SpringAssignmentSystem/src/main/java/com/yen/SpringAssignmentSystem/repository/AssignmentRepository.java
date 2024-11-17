package com.yen.SpringAssignmentSystem.repository;

import com.yen.SpringAssignmentSystem.domain.Assignment;
import com.yen.SpringAssignmentSystem.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // TODO : check if this is necessary ?
    Set<Assignment> findByUser(User user);

    @Query(value = "select a.* from assignment a",
            nativeQuery = true)
    List<Assignment> findAllAssignments2();

    @Query(value = "select a.* from assignment a where (a.status = 'submitted' or a.status = 'resubmitted')",
            nativeQuery = true)
    Set<Assignment> findByCodeReviewer(User codeReviewer);

    @Query(value = "select a.* from assignment a join users u on a.user_id = u.id where u.cohort_start_date is not null",
            nativeQuery = true)
    List<Assignment> findAllActiveBootcampStudents();

    // jpa pass param to SQL syntax : https://blog.csdn.net/qq_37732395/article/details/107101405
    @Query(value = "select a.* from assignment a where a.status = :status",
            nativeQuery = true)
    // use List for query result count > 1 via Hibernate : https://blog.csdn.net/weixin_44155001/article/details/120782977
    List<Assignment> findByStatus(@Param("status") String status);

}
