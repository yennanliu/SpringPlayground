package com.yen.SpringAssignmentSystem.repository;

import com.yen.SpringAssignmentSystem.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    @Query("select c from Comment c "
            + " where c.assignment.id = :assignmentId")
    Set<Comment> findByAssignmentId(@Param("assignmentId") Long assignmentId);

}
