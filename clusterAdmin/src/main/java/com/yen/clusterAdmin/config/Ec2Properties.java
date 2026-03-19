package com.yen.clusterAdmin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ec2.default")
public class Ec2Properties {

    private String ami;
    private String instanceType = "t3.medium";
    private String keyName;
    private String securityGroupId;
    private String subnetId;

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
}
