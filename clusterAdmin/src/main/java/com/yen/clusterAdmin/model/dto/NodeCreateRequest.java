package com.yen.clusterAdmin.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public class NodeCreateRequest {

    @NotBlank(message = "Node name is required")
    private String name;

    private String instanceType;

    private String availabilityZone;

    private Map<String, String> tags;

    public NodeCreateRequest() {
    }

    public NodeCreateRequest(String name, String instanceType, String availabilityZone, Map<String, String> tags) {
        this.name = name;
        this.instanceType = instanceType;
        this.availabilityZone = availabilityZone;
        this.tags = tags;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInstanceType() { return instanceType; }
    public void setInstanceType(String instanceType) { this.instanceType = instanceType; }

    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }
}
