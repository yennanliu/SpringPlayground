package com.yen.clusterAdmin.controller;

import com.yen.clusterAdmin.model.dto.ClusterHealthDTO;
import com.yen.clusterAdmin.model.dto.ClusterStatusDTO;
import com.yen.clusterAdmin.service.HealthMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cluster")
@Tag(name = "Cluster Operations", description = "APIs for cluster-wide operations")
public class ClusterController {

    private final HealthMonitorService healthMonitorService;

    public ClusterController(HealthMonitorService healthMonitorService) {
        this.healthMonitorService = healthMonitorService;
    }

    @GetMapping("/status")
    @Operation(summary = "Get cluster status", description = "Retrieve overall cluster status and metrics")
    public ResponseEntity<ClusterStatusDTO> getClusterStatus() {
        ClusterStatusDTO status = healthMonitorService.getClusterStatus();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/health")
    @Operation(summary = "Cluster health check", description = "Check the health of the cluster service")
    public ResponseEntity<ClusterHealthDTO> getClusterHealth() {
        ClusterHealthDTO health = healthMonitorService.getClusterHealth();
        return ResponseEntity.ok(health);
    }

    @PostMapping("/sync")
    @Operation(summary = "Sync all nodes", description = "Force sync all nodes with EC2 state")
    public ResponseEntity<Map<String, String>> syncAllNodes() {
        healthMonitorService.syncAllNodesWithEc2();
        return ResponseEntity.ok(Map.of("message", "Sync completed"));
    }

    @PostMapping("/health-check")
    @Operation(summary = "Trigger health checks", description = "Manually trigger health checks for all nodes")
    public ResponseEntity<Map<String, String>> triggerHealthChecks() {
        healthMonitorService.performHealthChecks();
        return ResponseEntity.ok(Map.of("message", "Health checks completed"));
    }
}
