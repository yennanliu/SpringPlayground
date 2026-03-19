package com.yen.clusterAdmin.model.dto;

import jakarta.validation.constraints.NotBlank;

public class HeartbeatRequest {

    @NotBlank(message = "Node ID or instance ID is required")
    private String nodeIdentifier;

    private Double cpuUsage;
    private Double memoryUsage;
    private Double diskUsage;
    private Long processCount;

    public HeartbeatRequest() {
    }

    public HeartbeatRequest(String nodeIdentifier, Double cpuUsage, Double memoryUsage,
                            Double diskUsage, Long processCount) {
        this.nodeIdentifier = nodeIdentifier;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
        this.processCount = processCount;
    }

    // Getters and Setters
    public String getNodeIdentifier() { return nodeIdentifier; }
    public void setNodeIdentifier(String nodeIdentifier) { this.nodeIdentifier = nodeIdentifier; }

    public Double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; }

    public Double getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(Double memoryUsage) { this.memoryUsage = memoryUsage; }

    public Double getDiskUsage() { return diskUsage; }
    public void setDiskUsage(Double diskUsage) { this.diskUsage = diskUsage; }

    public Long getProcessCount() { return processCount; }
    public void setProcessCount(Long processCount) { this.processCount = processCount; }
}
