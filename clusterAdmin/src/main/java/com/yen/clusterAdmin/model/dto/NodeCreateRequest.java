package com.yen.clusterAdmin.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public class NodeCreateRequest {

    @NotBlank(message = "Node name is required")
    private String name;

    private String region;

    private String instanceType;

    private String availabilityZone;

    private Map<String, String> tags;

    // Packages to install on the instance (e.g., ["docker", "htop", "nginx"])
    private List<String> packages;

    public NodeCreateRequest() {
    }

    public NodeCreateRequest(String name, String region, String instanceType, String availabilityZone, Map<String, String> tags, List<String> packages) {
        this.name = name;
        this.region = region;
        this.instanceType = instanceType;
        this.availabilityZone = availabilityZone;
        this.tags = tags;
        this.packages = packages;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getInstanceType() { return instanceType; }
    public void setInstanceType(String instanceType) { this.instanceType = instanceType; }

    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public List<String> getPackages() { return packages; }
    public void setPackages(List<String> packages) { this.packages = packages; }
}
