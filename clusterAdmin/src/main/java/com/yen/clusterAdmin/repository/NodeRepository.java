package com.yen.clusterAdmin.repository;

import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<Node, UUID> {

    Optional<Node> findByInstanceId(String instanceId);

    List<Node> findByStatus(NodeStatus status);

    List<Node> findByAvailabilityZone(String availabilityZone);

    long countByStatus(NodeStatus status);

    boolean existsByInstanceId(String instanceId);
}
