package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.config.Ec2BootstrapProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class Ec2BootstrapService {

    private static final Logger log = LoggerFactory.getLogger(Ec2BootstrapService.class);

    private final Ec2BootstrapProperties bootstrapProperties;

    public Ec2BootstrapService(Ec2BootstrapProperties bootstrapProperties) {
        this.bootstrapProperties = bootstrapProperties;
    }

    /**
     * Generate User Data script for EC2 instance initialization.
     * Returns Base64 encoded script as required by AWS.
     */
    public String generateUserData(String nodeName, List<String> additionalPackages, List<String> additionalCommands) {
        if (!bootstrapProperties.isEnabled()) {
            return null;
        }

        StringBuilder script = new StringBuilder();
        script.append("#!/bin/bash\n");
        script.append("set -e\n\n");

        // Log file for debugging
        script.append("exec > >(tee /var/log/clusteradmin-bootstrap.log) 2>&1\n");
        script.append("echo \"Starting ClusterAdmin bootstrap at $(date)\"\n\n");

        // Set hostname
        script.append("# Set hostname\n");
        script.append("hostnamectl set-hostname ").append(sanitize(nodeName)).append("\n\n");

        // Update system
        script.append("# Update system packages\n");
        script.append("yum update -y || apt-get update -y\n\n");

        // Install configured packages
        List<String> packages = bootstrapProperties.getPackages();
        if (additionalPackages != null && !additionalPackages.isEmpty()) {
            packages = new java.util.ArrayList<>(packages);
            packages.addAll(additionalPackages);
        }

        if (!packages.isEmpty()) {
            script.append("# Install packages\n");
            script.append("echo \"Installing packages: ").append(String.join(", ", packages)).append("\"\n");

            // Try yum first (Amazon Linux, RHEL), then apt (Ubuntu, Debian)
            script.append("if command -v yum &> /dev/null; then\n");
            script.append("    yum install -y ").append(String.join(" ", packages)).append("\n");
            script.append("elif command -v apt-get &> /dev/null; then\n");
            script.append("    apt-get install -y ").append(String.join(" ", packages)).append("\n");
            script.append("fi\n\n");
        }

        // Run configured commands
        List<String> commands = bootstrapProperties.getCommands();
        if (additionalCommands != null && !additionalCommands.isEmpty()) {
            commands = new java.util.ArrayList<>(commands);
            commands.addAll(additionalCommands);
        }

        if (!commands.isEmpty()) {
            script.append("# Run custom commands\n");
            for (String command : commands) {
                script.append("echo \"Running: ").append(sanitize(command)).append("\"\n");
                script.append(command).append("\n");
            }
            script.append("\n");
        }

        // Signal completion
        script.append("# Bootstrap complete\n");
        script.append("echo \"ClusterAdmin bootstrap completed at $(date)\"\n");
        script.append("touch /var/log/clusteradmin-bootstrap-complete\n");

        String rawScript = script.toString();
        log.debug("Generated bootstrap script:\n{}", rawScript);

        // AWS requires User Data to be Base64 encoded
        return Base64.getEncoder().encodeToString(rawScript.getBytes());
    }

    /**
     * Generate a simple script with just packages (no config lookup)
     */
    public String generateUserDataForPackages(String nodeName, List<String> packages) {
        return generateUserData(nodeName, packages, null);
    }

    /**
     * Check if bootstrap is enabled
     */
    public boolean isEnabled() {
        return bootstrapProperties.isEnabled();
    }

    private String sanitize(String input) {
        if (input == null) return "";
        // Basic sanitization - remove potentially dangerous characters
        return input.replaceAll("[\"'`$\\\\]", "");
    }
}
