package com.yen.clusterAdmin.controller;

import com.yen.clusterAdmin.model.dto.HeartbeatRequest;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.repository.NodeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/worker")
@Tag(name = "Worker Communication", description = "APIs for worker nodes to communicate with admin")
public class WorkerController {

    private static final Logger log = LoggerFactory.getLogger(WorkerController.class);

    private final NodeRepository nodeRepository;

    public WorkerController(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @PostMapping("/heartbeat")
    @Operation(summary = "Report heartbeat", description = "Worker node reports its health and metrics")
    public ResponseEntity<Map<String, Object>> reportHeartbeat(@Valid @RequestBody HeartbeatRequest request) {
        log.debug("Received heartbeat from: {}", request.getNodeIdentifier());

        Optional<Node> nodeOpt = findNode(request.getNodeIdentifier());

        if (nodeOpt.isEmpty()) {
            log.warn("Unknown node reporting heartbeat: {}", request.getNodeIdentifier());
            return ResponseEntity.notFound().build();
        }

        Node node = nodeOpt.get();

        // Update metrics
        if (request.getCpuUsage() != null) {
            node.setCpuUsage(request.getCpuUsage());
        }
        if (request.getMemoryUsage() != null) {
            node.setMemoryUsage(request.getMemoryUsage());
        }

        // Reset health checks and update heartbeat
        node.setFailedHealthChecks(0);
        node.setLastHeartbeat(Instant.now());

        // If node was unhealthy, mark it as running
        if (node.getStatus() == NodeStatus.UNHEALTHY) {
            log.info("Node {} recovered via heartbeat", node.getName());
            node.setStatus(NodeStatus.RUNNING);
        }

        // If node was pending, mark it as running (first heartbeat)
        if (node.getStatus() == NodeStatus.PENDING) {
            log.info("Node {} is now running (first heartbeat)", node.getName());
            node.setStatus(NodeStatus.RUNNING);
        }

        nodeRepository.save(node);

        return ResponseEntity.ok(Map.of(
                "status", "acknowledged",
                "nodeId", node.getId().toString(),
                "nodeName", node.getName(),
                "timestamp", Instant.now()
        ));
    }

    @PostMapping("/register")
    @Operation(summary = "Register worker", description = "Worker node registers itself with the cluster")
    public ResponseEntity<Map<String, Object>> registerWorker(@RequestBody Map<String, String> request) {
        String instanceId = request.get("instanceId");
        String name = request.get("name");
        String privateIp = request.get("privateIp");
        String publicIp = request.get("publicIp");

        if (instanceId == null || instanceId.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "instanceId is required"));
        }

        // Check if already registered
        Optional<Node> existing = nodeRepository.findByInstanceId(instanceId);
        if (existing.isPresent()) {
            Node node = existing.get();
            node.setLastHeartbeat(Instant.now());
            if (privateIp != null) node.setPrivateIp(privateIp);
            if (publicIp != null) node.setPublicIp(publicIp);
            if (node.getStatus() == NodeStatus.PENDING) {
                node.setStatus(NodeStatus.RUNNING);
            }
            nodeRepository.save(node);

            log.info("Worker {} re-registered", instanceId);
            return ResponseEntity.ok(Map.of(
                    "status", "updated",
                    "nodeId", node.getId().toString(),
                    "message", "Node already registered, updated info"
            ));
        }

        // Create new node
        Node node = Node.builder()
                .instanceId(instanceId)
                .name(name != null ? name : "worker-" + instanceId.substring(instanceId.length() - 8))
                .privateIp(privateIp)
                .publicIp(publicIp)
                .status(NodeStatus.RUNNING)
                .build();

        node = nodeRepository.save(node);
        log.info("New worker registered: {} ({})", node.getName(), instanceId);

        return ResponseEntity.ok(Map.of(
                "status", "registered",
                "nodeId", node.getId().toString(),
                "nodeName", node.getName(),
                "message", "Successfully registered"
        ));
    }

    private Optional<Node> findNode(String identifier) {
        // Try as UUID first
        try {
            UUID uuid = UUID.fromString(identifier);
            return nodeRepository.findById(uuid);
        } catch (IllegalArgumentException e) {
            // Not a UUID, try as instance ID
            return nodeRepository.findByInstanceId(identifier);
        }
    }
}
