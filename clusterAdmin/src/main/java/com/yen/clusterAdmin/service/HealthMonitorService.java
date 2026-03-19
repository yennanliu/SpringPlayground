package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.config.HealthMonitorProperties;
import com.yen.clusterAdmin.model.dto.ClusterHealthDTO;
import com.yen.clusterAdmin.model.dto.ClusterStatusDTO;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.repository.NodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.OptionalDouble;

@Service
public class HealthMonitorService {

    private static final Logger log = LoggerFactory.getLogger(HealthMonitorService.class);

    private final NodeRepository nodeRepository;
    private final Ec2Service ec2Service;
    private final HealthMonitorProperties healthProperties;

    @Value("${cluster.ec2.enabled:false}")
    private boolean ec2Enabled;

    @Value("${cluster.health.check.port:8080}")
    private int healthCheckPort;

    @Value("${cluster.health.check.path:/health}")
    private String healthCheckPath;

    public HealthMonitorService(NodeRepository nodeRepository, Ec2Service ec2Service,
                                 HealthMonitorProperties healthProperties) {
        this.nodeRepository = nodeRepository;
        this.ec2Service = ec2Service;
        this.healthProperties = healthProperties;
    }

    @Scheduled(fixedRateString = "${cluster.health.check.interval:30000}")
    @Transactional
    public void performHealthChecks() {
        if (!healthProperties.isEnabled()) {
            log.debug("Health checks are disabled");
            return;
        }

        log.debug("Starting scheduled health checks");

        List<Node> activeNodes = new java.util.ArrayList<>(nodeRepository.findByStatus(NodeStatus.RUNNING));
        activeNodes.addAll(nodeRepository.findByStatus(NodeStatus.UNHEALTHY));

        for (Node node : activeNodes) {
            checkNodeHealth(node);
        }

        // Also sync with EC2 if enabled
        if (ec2Enabled) {
            syncAllNodesWithEc2();
        }

        log.debug("Completed health checks for {} nodes", activeNodes.size());
    }

    @Transactional
    public void checkNodeHealth(Node node) {
        boolean isHealthy = performHealthCheck(node);

        if (isHealthy) {
            node.setFailedHealthChecks(0);
            node.setLastHeartbeat(Instant.now());

            if (node.getStatus() == NodeStatus.UNHEALTHY) {
                log.info("Node {} is healthy again", node.getName());
                node.setStatus(NodeStatus.RUNNING);
            }
        } else {
            int failedChecks = node.getFailedHealthChecks() + 1;
            node.setFailedHealthChecks(failedChecks);

            if (failedChecks >= healthProperties.getUnhealthyThreshold()
                    && node.getStatus() == NodeStatus.RUNNING) {
                log.warn("Node {} marked as UNHEALTHY after {} failed checks",
                        node.getName(), failedChecks);
                node.setStatus(NodeStatus.UNHEALTHY);
            }
        }

        nodeRepository.save(node);
    }

    private boolean performHealthCheck(Node node) {
        String targetIp = node.getPrivateIp();
        if (targetIp == null || targetIp.isEmpty()) {
            targetIp = node.getPublicIp();
        }

        if (targetIp == null || targetIp.isEmpty()) {
            log.debug("Node {} has no IP address for health check", node.getName());
            // If EC2 enabled, we rely on EC2 state; otherwise, simulate healthy
            return !ec2Enabled;
        }

        String healthUrl = String.format("http://%s:%d%s", targetIp, healthCheckPort, healthCheckPath);

        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(healthUrl).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout((int) healthProperties.getTimeout());
            connection.setReadTimeout((int) healthProperties.getTimeout());

            int responseCode = connection.getResponseCode();
            boolean healthy = responseCode >= 200 && responseCode < 300;

            log.debug("Health check for {} ({}): {} - {}", node.getName(), healthUrl,
                    responseCode, healthy ? "HEALTHY" : "UNHEALTHY");

            return healthy;

        } catch (IOException e) {
            log.debug("Health check failed for {} ({}): {}", node.getName(), healthUrl, e.getMessage());
            return false;
        }
    }

    @Transactional
    public void syncAllNodesWithEc2() {
        if (!ec2Enabled) {
            return;
        }

        List<Node> nodes = nodeRepository.findAll();
        for (Node node : nodes) {
            if (node.getInstanceId() != null && !node.getInstanceId().isEmpty()) {
                ec2Service.syncNodeFromEc2(node);
                nodeRepository.save(node);
            }
        }
    }

    public ClusterStatusDTO getClusterStatus() {
        long totalNodes = nodeRepository.count();
        long runningNodes = nodeRepository.countByStatus(NodeStatus.RUNNING);
        long pendingNodes = nodeRepository.countByStatus(NodeStatus.PENDING);
        long stoppedNodes = nodeRepository.countByStatus(NodeStatus.STOPPED);
        long unhealthyNodes = nodeRepository.countByStatus(NodeStatus.UNHEALTHY);
        long terminatedNodes = nodeRepository.countByStatus(NodeStatus.TERMINATED);

        // Calculate average metrics for running nodes
        List<Node> running = nodeRepository.findByStatus(NodeStatus.RUNNING);
        OptionalDouble avgCpu = running.stream()
                .filter(n -> n.getCpuUsage() != null)
                .mapToDouble(Node::getCpuUsage)
                .average();
        OptionalDouble avgMemory = running.stream()
                .filter(n -> n.getMemoryUsage() != null)
                .mapToDouble(Node::getMemoryUsage)
                .average();

        return ClusterStatusDTO.builder()
                .totalNodes(totalNodes)
                .runningNodes(runningNodes)
                .pendingNodes(pendingNodes)
                .stoppedNodes(stoppedNodes)
                .unhealthyNodes(unhealthyNodes)
                .terminatedNodes(terminatedNodes)
                .averageCpuUsage(avgCpu.isPresent() ? Math.round(avgCpu.getAsDouble() * 100.0) / 100.0 : null)
                .averageMemoryUsage(avgMemory.isPresent() ? Math.round(avgMemory.getAsDouble() * 100.0) / 100.0 : null)
                .lastUpdated(Instant.now())
                .build();
    }

    public ClusterHealthDTO getClusterHealth() {
        long totalNodes = nodeRepository.count();
        long runningNodes = nodeRepository.countByStatus(NodeStatus.RUNNING);
        long unhealthyCount = nodeRepository.countByStatus(NodeStatus.UNHEALTHY);

        String healthStatus = determineClusterHealthStatus(totalNodes, runningNodes, unhealthyCount);

        List<ClusterHealthDTO.NodeHealthSummary> unhealthyList = nodeRepository
                .findByStatus(NodeStatus.UNHEALTHY)
                .stream()
                .map(n -> new ClusterHealthDTO.NodeHealthSummary(
                        n.getId().toString(),
                        n.getName(),
                        n.getInstanceId(),
                        n.getLastHeartbeat(),
                        n.getFailedHealthChecks()
                ))
                .toList();

        return ClusterHealthDTO.builder()
                .status(healthStatus)
                .totalNodes(totalNodes)
                .healthyNodes(runningNodes)
                .unhealthyNodes(unhealthyCount)
                .unhealthyNodesList(unhealthyList)
                .timestamp(Instant.now())
                .build();
    }

    private String determineClusterHealthStatus(long total, long running, long unhealthy) {
        if (total == 0) {
            return "EMPTY";
        }
        if (unhealthy > 0) {
            // More than 50% unhealthy = UNHEALTHY, otherwise DEGRADED
            if (unhealthy > total / 2) {
                return "UNHEALTHY";
            }
            return "DEGRADED";
        }
        if (running == total) {
            return "HEALTHY";
        }
        return "PARTIAL"; // Some nodes pending/stopped
    }

    @Transactional
    public void updateNodeMetrics(Node node, Double cpuUsage, Double memoryUsage) {
        node.setCpuUsage(cpuUsage);
        node.setMemoryUsage(memoryUsage);
        node.setLastHeartbeat(Instant.now());
        nodeRepository.save(node);
    }
}
