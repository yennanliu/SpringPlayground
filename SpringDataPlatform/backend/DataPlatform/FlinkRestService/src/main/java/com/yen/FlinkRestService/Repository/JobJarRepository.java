package com.yen.FlinkRestService.Repository;

import com.yen.FlinkRestService.model.JobJar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobJarRepository extends JpaRepository<JobJar, Integer> {
}
