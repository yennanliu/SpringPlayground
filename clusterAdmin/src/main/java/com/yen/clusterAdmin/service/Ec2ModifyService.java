package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.exception.Ec2OperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.*;

import java.util.List;
import java.util.Map;

@Service
public class Ec2ModifyService {

    private static final Logger log = LoggerFactory.getLogger(Ec2ModifyService.class);

    private final Ec2ClientFactory ec2ClientFactory;
    private final SsmClientFactory ssmClientFactory;

    public Ec2ModifyService(Ec2ClientFactory ec2ClientFactory, SsmClientFactory ssmClientFactory) {
        this.ec2ClientFactory = ec2ClientFactory;
        this.ssmClientFactory = ssmClientFactory;
    }

    /**
     * Modify instance type (requires stop → modify → start)
     */
    public void modifyInstanceType(String instanceId, String newInstanceType, String region) {
        log.info("Modifying instance type: instanceId={}, newType={}, region={}",
                instanceId, newInstanceType, region);

        Ec2Client ec2Client = ec2ClientFactory.getClient(region);

        try {
            // 1. Check current state
            InstanceStateName currentState = getInstanceState(ec2Client, instanceId);
            boolean wasRunning = currentState == InstanceStateName.RUNNING;

            // 2. Stop instance if running
            if (wasRunning) {
                log.info("Stopping instance {} before modifying type...", instanceId);
                stopAndWait(ec2Client, instanceId);
            } else if (currentState != InstanceStateName.STOPPED) {
                throw new Ec2OperationException(
                        "Instance must be running or stopped to modify. Current state: " + currentState);
            }

            // 3. Modify instance type
            log.info("Modifying instance type to {}...", newInstanceType);
            ModifyInstanceAttributeRequest modifyRequest = ModifyInstanceAttributeRequest.builder()
                    .instanceId(instanceId)
                    .instanceType(AttributeValue.builder()
                            .value(newInstanceType)
                            .build())
                    .build();

            ec2Client.modifyInstanceAttribute(modifyRequest);
            log.info("Instance type modified successfully");

            // 4. Start instance if it was running before
            if (wasRunning) {
                log.info("Starting instance {} after modification...", instanceId);
                startAndWait(ec2Client, instanceId);
            }

            log.info("Instance type modification completed: instanceId={}, newType={}",
                    instanceId, newInstanceType);

        } catch (Ec2Exception e) {
            log.error("Failed to modify instance type: {}", e.getMessage());
            throw new Ec2OperationException("Failed to modify instance type: " + e.getMessage(), e);
        }
    }

    /**
     * Run shell commands on an EC2 instance via SSM
     */
    public String runCommand(String instanceId, List<String> commands, String region) {
        log.info("Running SSM command on instance: instanceId={}, commands={}, region={}",
                instanceId, commands, region);

        SsmClient ssmClient = ssmClientFactory.getClient(region);

        try {
            SendCommandRequest request = SendCommandRequest.builder()
                    .instanceIds(instanceId)
                    .documentName("AWS-RunShellScript")
                    .parameters(Map.of("commands", commands))
                    .timeoutSeconds(600) // 10 minutes timeout
                    .comment("Command from ClusterAdmin")
                    .build();

            SendCommandResponse response = ssmClient.sendCommand(request);
            String commandId = response.command().commandId();

            log.info("SSM command sent successfully: commandId={}", commandId);
            return commandId;

        } catch (SsmException e) {
            log.error("Failed to send SSM command: {}", e.getMessage());
            throw new Ec2OperationException("Failed to send SSM command: " + e.getMessage(), e);
        }
    }

    /**
     * Get system logs from an EC2 instance via SSM
     */
    public String getSystemLogs(String instanceId, int lines, String region) {
        log.info("Fetching system logs from instance: instanceId={}, lines={}, region={}",
                instanceId, lines, region);

        // Use journalctl (systemd) with fallback to syslog
        List<String> commands = List.of(
                "#!/bin/bash",
                "if command -v journalctl &> /dev/null; then",
                "    journalctl -n " + lines + " --no-pager",
                "elif [ -f /var/log/syslog ]; then",
                "    tail -n " + lines + " /var/log/syslog",
                "elif [ -f /var/log/messages ]; then",
                "    tail -n " + lines + " /var/log/messages",
                "else",
                "    echo 'No system log found'",
                "fi"
        );

        return runCommand(instanceId, commands, region);
    }

    /**
     * Install packages on an EC2 instance via SSM
     */
    public String installPackages(String instanceId, List<String> packages, String region) {
        log.info("Installing packages on instance: instanceId={}, packages={}, region={}",
                instanceId, packages, region);

        if (packages == null || packages.isEmpty()) {
            throw new IllegalArgumentException("Packages list cannot be empty");
        }

        // Build installation command (supports both yum and apt)
        String packagesStr = String.join(" ", packages);
        List<String> commands = List.of(
                "#!/bin/bash",
                "set -e",
                "export DEBIAN_FRONTEND=noninteractive",
                "if command -v yum &> /dev/null; then",
                "    sudo yum install -y " + packagesStr,
                "elif command -v apt-get &> /dev/null; then",
                "    sudo apt-get update && sudo apt-get install -y -o Dpkg::Options::='--force-confdef' -o Dpkg::Options::='--force-confold' " + packagesStr,
                "else",
                "    echo 'No supported package manager found'",
                "    exit 1",
                "fi",
                "echo 'Package installation completed successfully'"
        );

        return runCommand(instanceId, commands, region);
    }

    /**
     * Get the status of an SSM command
     */
    public CommandInvocationStatus getCommandStatus(String commandId, String instanceId, String region) {
        SsmClient ssmClient = ssmClientFactory.getClient(region);

        try {
            GetCommandInvocationRequest request = GetCommandInvocationRequest.builder()
                    .commandId(commandId)
                    .instanceId(instanceId)
                    .build();

            GetCommandInvocationResponse response = ssmClient.getCommandInvocation(request);
            return response.status();

        } catch (SsmException e) {
            log.error("Failed to get command status: {}", e.getMessage());
            throw new Ec2OperationException("Failed to get command status: " + e.getMessage(), e);
        }
    }

    /**
     * Get the output of an SSM command
     */
    public CommandResult getCommandResult(String commandId, String instanceId, String region) {
        SsmClient ssmClient = ssmClientFactory.getClient(region);

        try {
            GetCommandInvocationRequest request = GetCommandInvocationRequest.builder()
                    .commandId(commandId)
                    .instanceId(instanceId)
                    .build();

            GetCommandInvocationResponse response = ssmClient.getCommandInvocation(request);

            return new CommandResult(
                    response.statusAsString(),
                    response.standardOutputContent(),
                    response.standardErrorContent(),
                    response.responseCode()
            );

        } catch (SsmException e) {
            log.error("Failed to get command result: {}", e.getMessage());
            throw new Ec2OperationException("Failed to get command result: " + e.getMessage(), e);
        }
    }

    /**
     * Modify EBS volume size
     */
    public void modifyVolumeSize(String volumeId, int newSizeGb, String region) {
        log.info("Modifying volume size: volumeId={}, newSize={}GB, region={}",
                volumeId, newSizeGb, region);

        Ec2Client ec2Client = ec2ClientFactory.getClient(region);

        try {
            ModifyVolumeRequest request = ModifyVolumeRequest.builder()
                    .volumeId(volumeId)
                    .size(newSizeGb)
                    .build();

            ModifyVolumeResponse response = ec2Client.modifyVolume(request);
            log.info("Volume modification initiated: volumeId={}, newSize={}GB, state={}",
                    volumeId, newSizeGb, response.volumeModification().modificationStateAsString());

        } catch (Ec2Exception e) {
            log.error("Failed to modify volume size: {}", e.getMessage());
            throw new Ec2OperationException("Failed to modify volume size: " + e.getMessage(), e);
        }
    }

    /**
     * Get volumes attached to an instance
     */
    public List<Volume> getInstanceVolumes(String instanceId, String region) {
        Ec2Client ec2Client = ec2ClientFactory.getClient(region);

        try {
            DescribeVolumesRequest request = DescribeVolumesRequest.builder()
                    .filters(Filter.builder()
                            .name("attachment.instance-id")
                            .values(instanceId)
                            .build())
                    .build();

            DescribeVolumesResponse response = ec2Client.describeVolumes(request);
            return response.volumes();

        } catch (Ec2Exception e) {
            log.error("Failed to get instance volumes: {}", e.getMessage());
            throw new Ec2OperationException("Failed to get instance volumes: " + e.getMessage(), e);
        }
    }

    // Helper methods

    private InstanceStateName getInstanceState(Ec2Client ec2Client, String instanceId) {
        DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        DescribeInstancesResponse response = ec2Client.describeInstances(request);
        return response.reservations().stream()
                .flatMap(r -> r.instances().stream())
                .findFirst()
                .map(i -> i.state().name())
                .orElseThrow(() -> new Ec2OperationException("Instance not found: " + instanceId));
    }

    private void stopAndWait(Ec2Client ec2Client, String instanceId) {
        StopInstancesRequest stopRequest = StopInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2Client.stopInstances(stopRequest);

        // Wait for instance to stop
        waitForState(ec2Client, instanceId, InstanceStateName.STOPPED, 120);
    }

    private void startAndWait(Ec2Client ec2Client, String instanceId) {
        StartInstancesRequest startRequest = StartInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2Client.startInstances(startRequest);

        // Wait for instance to start
        waitForState(ec2Client, instanceId, InstanceStateName.RUNNING, 120);
    }

    private void waitForState(Ec2Client ec2Client, String instanceId, InstanceStateName targetState, int timeoutSeconds) {
        log.debug("Waiting for instance {} to reach state {}...", instanceId, targetState);

        long startTime = System.currentTimeMillis();
        long timeoutMs = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            InstanceStateName currentState = getInstanceState(ec2Client, instanceId);
            if (currentState == targetState) {
                log.debug("Instance {} reached state {}", instanceId, targetState);
                return;
            }

            try {
                Thread.sleep(5000); // Poll every 5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new Ec2OperationException("Wait interrupted");
            }
        }

        throw new Ec2OperationException(
                "Timeout waiting for instance " + instanceId + " to reach state " + targetState);
    }

    /**
     * Result of an SSM command
     */
    public record CommandResult(
            String status,
            String output,
            String error,
            int responseCode
    ) {}
}
