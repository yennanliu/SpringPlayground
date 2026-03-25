package com.yen.clusterAdmin.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ModifyInstanceRequest {

    private String instanceType;

    private List<String> packages;

    private List<String> commands;

    private Integer volumeSizeGb;

    private String volumeId;

    // Getters and Setters
    public String getInstanceType() { return instanceType; }
    public void setInstanceType(String instanceType) { this.instanceType = instanceType; }

    public List<String> getPackages() { return packages; }
    public void setPackages(List<String> packages) { this.packages = packages; }

    public List<String> getCommands() { return commands; }
    public void setCommands(List<String> commands) { this.commands = commands; }

    public Integer getVolumeSizeGb() { return volumeSizeGb; }
    public void setVolumeSizeGb(Integer volumeSizeGb) { this.volumeSizeGb = volumeSizeGb; }

    public String getVolumeId() { return volumeId; }
    public void setVolumeId(String volumeId) { this.volumeId = volumeId; }
}
