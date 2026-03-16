package com.yen.FlinkRestService.Repository;

import com.yen.FlinkRestService.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    Optional<Job> findByJobId(String jobId);
}
