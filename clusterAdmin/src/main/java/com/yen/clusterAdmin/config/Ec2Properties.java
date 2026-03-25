package com.yen.clusterAdmin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "ec2.default")
public class Ec2Properties {

    private String ami;
    private String instanceType = "t3.medium";
    private String keyName;
    private String securityGroupId;
    private String subnetId;

    // Region-specific configurations
    private Map<String, String> regionAmis = new HashMap<>();
    private Map<String, String> regionSecurityGroups = new HashMap<>();
    private Map<String, String> regionSubnets = new HashMap<>();
    private Map<String, String> regionKeyNames = new HashMap<>();

    public Ec2Properties() {
        // Default Amazon Linux 2023 AMIs for common regions
        regionAmis.put("us-east-1", "ami-0c02fb55956c7d316");
        regionAmis.put("us-west-2", "ami-0b20a6f09484773af");
        regionAmis.put("eu-west-1", "ami-0694d931cee176e7d");
        regionAmis.put("ap-northeast-1", "ami-0d52744d6551d851e");
        regionAmis.put("ap-southeast-1", "ami-0df7a207adb9748c7");
    }

    // Getters for region-specific values (with fallback to default)
    public String getAmiForRegion(String region) {
        if (regionAmis.containsKey(region)) {
            return regionAmis.get(region);
        }
        return ami;
    }

    public String getSecurityGroupIdForRegion(String region) {
        if (regionSecurityGroups.containsKey(region)) {
            return regionSecurityGroups.get(region);
        }
        return securityGroupId;
    }

    public String getSubnetIdForRegion(String region) {
        if (regionSubnets.containsKey(region)) {
            return regionSubnets.get(region);
        }
        return subnetId;
    }

    public String getKeyNameForRegion(String region) {
        if (regionKeyNames.containsKey(region)) {
            return regionKeyNames.get(region);
        }
        return keyName;
    }

    // Basic getters/setters
    public String getAmi() { return ami; }
    public void setAmi(String ami) { this.ami = ami; }

    public String getInstanceType() { return instanceType; }
    public void setInstanceType(String instanceType) { this.instanceType = instanceType; }

    public String getKeyName() { return keyName; }
    public void setKeyName(String keyName) { this.keyName = keyName; }

    public String getSecurityGroupId() { return securityGroupId; }
    public void setSecurityGroupId(String securityGroupId) { this.securityGroupId = securityGroupId; }

    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }

    // Map getters/setters
    public Map<String, String> getRegionAmis() { return regionAmis; }
    public void setRegionAmis(Map<String, String> regionAmis) { this.regionAmis = regionAmis; }

    public Map<String, String> getRegionSecurityGroups() { return regionSecurityGroups; }
    public void setRegionSecurityGroups(Map<String, String> regionSecurityGroups) { this.regionSecurityGroups = regionSecurityGroups; }

    public Map<String, String> getRegionSubnets() { return regionSubnets; }
    public void setRegionSubnets(Map<String, String> regionSubnets) { this.regionSubnets = regionSubnets; }

    public Map<String, String> getRegionKeyNames() { return regionKeyNames; }
    public void setRegionKeyNames(Map<String, String> regionKeyNames) { this.regionKeyNames = regionKeyNames; }
}
