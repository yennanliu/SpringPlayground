package com.yen.clusterAdmin.model.dto;

import java.time.Instant;
import java.util.List;

public class ClusterHealthDTO {

    private String status; // HEALTHY, DEGRADED, UNHEALTHY, EMPTY
    private long totalNodes;
    private long healthyNodes;
    private long unhealthyNodes;
    private List<NodeHealthSummary> unhealthyNodesList;
    private Instant timestamp;

    public ClusterHealthDTO() {
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getTotalNodes() { return totalNodes; }
    public void setTotalNodes(long totalNodes) { this.totalNodes = totalNodes; }

    public long getHealthyNodes() { return healthyNodes; }
    public void setHealthyNodes(long healthyNodes) { this.healthyNodes = healthyNodes; }

    public long getUnhealthyNodes() { return unhealthyNodes; }
    public void setUnhealthyNodes(long unhealthyNodes) { this.unhealthyNodes = unhealthyNodes; }

    public List<NodeHealthSummary> getUnhealthyNodesList() { return unhealthyNodesList; }
    public void setUnhealthyNodesList(List<NodeHealthSummary> unhealthyNodesList) { this.unhealthyNodesList = unhealthyNodesList; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ClusterHealthDTO dto = new ClusterHealthDTO();

        public Builder status(String status) { dto.status = status; return this; }
        public Builder totalNodes(long totalNodes) { dto.totalNodes = totalNodes; return this; }
        public Builder healthyNodes(long healthyNodes) { dto.healthyNodes = healthyNodes; return this; }
        public Builder unhealthyNodes(long unhealthyNodes) { dto.unhealthyNodes = unhealthyNodes; return this; }
        public Builder unhealthyNodesList(List<NodeHealthSummary> list) { dto.unhealthyNodesList = list; return this; }
        public Builder timestamp(Instant timestamp) { dto.timestamp = timestamp; return this; }

        public ClusterHealthDTO build() { return dto; }
    }

    // Nested class for unhealthy node summary
    public static class NodeHealthSummary {
        private String id;
        private String name;
        private String instanceId;
        private Instant lastHeartbeat;
        private int failedChecks;

        public NodeHealthSummary() {
        }

        public NodeHealthSummary(String id, String name, String instanceId, Instant lastHeartbeat, int failedChecks) {
            this.id = id;
            this.name = name;
            this.instanceId = instanceId;
            this.lastHeartbeat = lastHeartbeat;
            this.failedChecks = failedChecks;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }

        public Instant getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(Instant lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }

        public int getFailedChecks() { return failedChecks; }
        public void setFailedChecks(int failedChecks) { this.failedChecks = failedChecks; }
    }
}
