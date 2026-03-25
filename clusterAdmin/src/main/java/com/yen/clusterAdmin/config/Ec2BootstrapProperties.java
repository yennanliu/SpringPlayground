package com.yen.clusterAdmin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "ec2.bootstrap")
public class Ec2BootstrapProperties {

    // Packages to install on boot (e.g., "docker", "htop", "nginx")
    private List<String> packages = new ArrayList<>();

    // Custom commands to run after package installation
    private List<String> commands = new ArrayList<>();

    // Enable/disable bootstrap script
    private boolean enabled = true;

    public List<String> getPackages() { return packages; }
    public void setPackages(List<String> packages) { this.packages = packages; }

    public List<String> getCommands() { return commands; }
    public void setCommands(List<String> commands) { this.commands = commands; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
