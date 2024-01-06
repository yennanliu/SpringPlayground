package com.yen.FlinkRestService.Repository;

import com.yen.FlinkRestService.model.SqlJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SqlJobRepository extends JpaRepository<SqlJob, Integer> {
}
