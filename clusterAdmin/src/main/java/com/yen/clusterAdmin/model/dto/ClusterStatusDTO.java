package com.yen.clusterAdmin.model.dto;

import java.time.Instant;

public class ClusterStatusDTO {

    private long totalNodes;
    private long runningNodes;
    private long pendingNodes;
    private long stoppedNodes;
    private long unhealthyNodes;
    private long terminatedNodes;
    private Double averageCpuUsage;
    private Double averageMemoryUsage;
    private Instant lastUpdated;

    public ClusterStatusDTO() {
    }

    // Getters and Setters
    public long getTotalNodes() { return totalNodes; }
    public void setTotalNodes(long totalNodes) { this.totalNodes = totalNodes; }

    public long getRunningNodes() { return runningNodes; }
    public void setRunningNodes(long runningNodes) { this.runningNodes = runningNodes; }

    public long getPendingNodes() { return pendingNodes; }
    public void setPendingNodes(long pendingNodes) { this.pendingNodes = pendingNodes; }

    public long getStoppedNodes() { return stoppedNodes; }
    public void setStoppedNodes(long stoppedNodes) { this.stoppedNodes = stoppedNodes; }

    public long getUnhealthyNodes() { return unhealthyNodes; }
    public void setUnhealthyNodes(long unhealthyNodes) { this.unhealthyNodes = unhealthyNodes; }

    public long getTerminatedNodes() { return terminatedNodes; }
    public void setTerminatedNodes(long terminatedNodes) { this.terminatedNodes = terminatedNodes; }

    public Double getAverageCpuUsage() { return averageCpuUsage; }
    public void setAverageCpuUsage(Double averageCpuUsage) { this.averageCpuUsage = averageCpuUsage; }

    public Double getAverageMemoryUsage() { return averageMemoryUsage; }
    public void setAverageMemoryUsage(Double averageMemoryUsage) { this.averageMemoryUsage = averageMemoryUsage; }

    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ClusterStatusDTO dto = new ClusterStatusDTO();

        public Builder totalNodes(long totalNodes) { dto.totalNodes = totalNodes; return this; }
        public Builder runningNodes(long runningNodes) { dto.runningNodes = runningNodes; return this; }
        public Builder pendingNodes(long pendingNodes) { dto.pendingNodes = pendingNodes; return this; }
        public Builder stoppedNodes(long stoppedNodes) { dto.stoppedNodes = stoppedNodes; return this; }
        public Builder unhealthyNodes(long unhealthyNodes) { dto.unhealthyNodes = unhealthyNodes; return this; }
        public Builder terminatedNodes(long terminatedNodes) { dto.terminatedNodes = terminatedNodes; return this; }
        public Builder averageCpuUsage(Double averageCpuUsage) { dto.averageCpuUsage = averageCpuUsage; return this; }
        public Builder averageMemoryUsage(Double averageMemoryUsage) { dto.averageMemoryUsage = averageMemoryUsage; return this; }
        public Builder lastUpdated(Instant lastUpdated) { dto.lastUpdated = lastUpdated; return this; }

        public ClusterStatusDTO build() { return dto; }
    }
}
