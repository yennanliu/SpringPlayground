package com.yen.FlinkRestService.Repository;

import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.JobJar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    // TODO: fix below
    //Job updateJob(Job job);
}
