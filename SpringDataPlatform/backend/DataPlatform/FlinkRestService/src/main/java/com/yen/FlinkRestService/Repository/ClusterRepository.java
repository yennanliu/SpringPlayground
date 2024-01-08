package com.yen.FlinkRestService.Repository;

import com.yen.FlinkRestService.model.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClusterRepository extends JpaRepository<Cluster, Integer> {
}
