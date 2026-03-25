package com.yen.clusterAdmin.model.dto;

import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;

import java.time.Instant;
import java.util.UUID;

public class NodeDTO {

    private UUID id;
    private String instanceId;
    private String name;
    private String region;
    private String privateIp;
    private String publicIp;
    private NodeStatus status;
    private String instanceType;
    private String availabilityZone;
    private Double cpuUsage;
    private Double memoryUsage;
    private Instant lastHeartbeat;
    private Instant createdAt;

    public NodeDTO() {
    }

    public NodeDTO(UUID id, String instanceId, String name, String region, String privateIp, String publicIp,
                   NodeStatus status, String instanceType, String availabilityZone,
                   Double cpuUsage, Double memoryUsage, Instant lastHeartbeat, Instant createdAt) {
        this.id = id;
        this.instanceId = instanceId;
        this.name = name;
        this.region = region;
        this.privateIp = privateIp;
        this.publicIp = publicIp;
        this.status = status;
        this.instanceType = instanceType;
        this.availabilityZone = availabilityZone;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.lastHeartbeat = lastHeartbeat;
        this.createdAt = createdAt;
    }

    public static NodeDTO fromEntity(Node node) {
        return new NodeDTO(
                node.getId(),
                node.getInstanceId(),
                node.getName(),
                node.getRegion(),
                node.getPrivateIp(),
                node.getPublicIp(),
                node.getStatus(),
                node.getInstanceType(),
                node.getAvailabilityZone(),
                node.getCpuUsage(),
                node.getMemoryUsage(),
                node.getLastHeartbeat(),
                node.getCreatedAt()
        );
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getPrivateIp() { return privateIp; }
    public void setPrivateIp(String privateIp) { this.privateIp = privateIp; }

    public String getPublicIp() { return publicIp; }
    public void setPublicIp(String publicIp) { this.publicIp = publicIp; }

    public NodeStatus getStatus() { return status; }
    public void setStatus(NodeStatus status) { this.status = status; }

    public String getInstanceType() { return instanceType; }
    public void setInstanceType(String instanceType) { this.instanceType = instanceType; }

    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }

    public Double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; }

    public Double getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(Double memoryUsage) { this.memoryUsage = memoryUsage; }

    public Instant getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(Instant lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
