package com.yen.clusterAdmin.model.entity;

import com.yen.clusterAdmin.model.enums.NodeStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "nodes")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String instanceId;

    @Column(nullable = false)
    private String name;

    private String privateIp;

    private String publicIp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NodeStatus status;

    private String instanceType;

    private String availabilityZone;

    private Double cpuUsage;

    private Double memoryUsage;

    private Instant lastHeartbeat;

    private Integer failedHealthChecks = 0;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    public Node() {
    }

    public Node(UUID id, String instanceId, String name, String privateIp, String publicIp,
                NodeStatus status, String instanceType, String availabilityZone,
                Double cpuUsage, Double memoryUsage, Instant lastHeartbeat,
                Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.instanceId = instanceId;
        this.name = name;
        this.privateIp = privateIp;
        this.publicIp = publicIp;
        this.status = status;
        this.instanceType = instanceType;
        this.availabilityZone = availabilityZone;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.lastHeartbeat = lastHeartbeat;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) {
            status = NodeStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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

    public Integer getFailedHealthChecks() { return failedHealthChecks != null ? failedHealthChecks : 0; }
    public void setFailedHealthChecks(Integer failedHealthChecks) { this.failedHealthChecks = failedHealthChecks; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String instanceId;
        private String name;
        private String privateIp;
        private String publicIp;
        private NodeStatus status;
        private String instanceType;
        private String availabilityZone;
        private Double cpuUsage;
        private Double memoryUsage;
        private Instant lastHeartbeat;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder instanceId(String instanceId) { this.instanceId = instanceId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder privateIp(String privateIp) { this.privateIp = privateIp; return this; }
        public Builder publicIp(String publicIp) { this.publicIp = publicIp; return this; }
        public Builder status(NodeStatus status) { this.status = status; return this; }
        public Builder instanceType(String instanceType) { this.instanceType = instanceType; return this; }
        public Builder availabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; return this; }
        public Builder cpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; return this; }
        public Builder memoryUsage(Double memoryUsage) { this.memoryUsage = memoryUsage; return this; }
        public Builder lastHeartbeat(Instant lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Node build() {
            return new Node(id, instanceId, name, privateIp, publicIp, status, instanceType,
                    availabilityZone, cpuUsage, memoryUsage, lastHeartbeat, createdAt, updatedAt);
        }
    }
}
