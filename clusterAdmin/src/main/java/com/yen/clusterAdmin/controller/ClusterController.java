package com.yen.clusterAdmin.controller;

import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cluster")
@Tag(name = "Cluster Operations", description = "APIs for cluster-wide operations")
public class ClusterController {

    private final NodeService nodeService;

    public ClusterController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/status")
    @Operation(summary = "Get cluster status", description = "Retrieve overall cluster status and metrics")
    public ResponseEntity<Map<String, Object>> getClusterStatus() {
        long totalNodes = nodeService.getTotalNodeCount();
        long runningNodes = nodeService.countByStatus(NodeStatus.RUNNING);
        long unhealthyNodes = nodeService.countByStatus(NodeStatus.UNHEALTHY);
        long stoppedNodes = nodeService.countByStatus(NodeStatus.STOPPED);
        long pendingNodes = nodeService.countByStatus(NodeStatus.PENDING);

        Map<String, Object> status = Map.of(
                "totalNodes", totalNodes,
                "runningNodes", runningNodes,
                "unhealthyNodes", unhealthyNodes,
                "stoppedNodes", stoppedNodes,
                "pendingNodes", pendingNodes,
                "lastUpdated", Instant.now()
        );

        return ResponseEntity.ok(status);
    }

    @GetMapping("/health")
    @Operation(summary = "Cluster health check", description = "Check the health of the cluster service")
    public ResponseEntity<Map<String, Object>> getClusterHealth() {
        long totalNodes = nodeService.getTotalNodeCount();
        long runningNodes = nodeService.countByStatus(NodeStatus.RUNNING);
        long unhealthyNodes = nodeService.countByStatus(NodeStatus.UNHEALTHY);

        String healthStatus;
        if (totalNodes == 0) {
            healthStatus = "EMPTY";
        } else if (unhealthyNodes > 0) {
            healthStatus = "DEGRADED";
        } else if (runningNodes == totalNodes) {
            healthStatus = "HEALTHY";
        } else {
            healthStatus = "PARTIAL";
        }

        Map<String, Object> health = Map.of(
                "status", healthStatus,
                "totalNodes", totalNodes,
                "healthyNodes", runningNodes,
                "unhealthyNodes", unhealthyNodes,
                "timestamp", Instant.now()
        );

        return ResponseEntity.ok(health);
    }
}
