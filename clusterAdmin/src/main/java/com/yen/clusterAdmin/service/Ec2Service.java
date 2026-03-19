package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.config.Ec2Properties;
import com.yen.clusterAdmin.exception.Ec2OperationException;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class Ec2Service {

    private static final Logger log = LoggerFactory.getLogger(Ec2Service.class);

    private final Ec2Client ec2Client;
    private final Ec2Properties ec2Properties;

    public Ec2Service(Ec2Client ec2Client, Ec2Properties ec2Properties) {
        this.ec2Client = ec2Client;
        this.ec2Properties = ec2Properties;
    }

    public String launchInstance(String name, String instanceType, Map<String, String> tags) {
        log.info("Launching EC2 instance: name={}, type={}", name, instanceType);

        try {
            String resolvedInstanceType = instanceType != null ? instanceType : ec2Properties.getInstanceType();

            RunInstancesRequest.Builder requestBuilder = RunInstancesRequest.builder()
                    .imageId(ec2Properties.getAmi())
                    .instanceType(InstanceType.fromValue(resolvedInstanceType))
                    .minCount(1)
                    .maxCount(1);

            if (ec2Properties.getKeyName() != null && !ec2Properties.getKeyName().isEmpty()) {
                requestBuilder.keyName(ec2Properties.getKeyName());
            }

            if (ec2Properties.getSecurityGroupId() != null && !ec2Properties.getSecurityGroupId().isEmpty()) {
                requestBuilder.securityGroupIds(ec2Properties.getSecurityGroupId());
            }

            if (ec2Properties.getSubnetId() != null && !ec2Properties.getSubnetId().isEmpty()) {
                requestBuilder.subnetId(ec2Properties.getSubnetId());
            }

            RunInstancesResponse response = ec2Client.runInstances(requestBuilder.build());

            if (response.instances().isEmpty()) {
                throw new Ec2OperationException("No instances returned from RunInstances");
            }

            String instanceId = response.instances().get(0).instanceId();
            log.info("Launched EC2 instance: {}", instanceId);

            // Tag the instance
            Tag nameTag = Tag.builder().key("Name").value(name).build();
            Tag managedByTag = Tag.builder().key("ManagedBy").value("ClusterAdmin").build();

            CreateTagsRequest tagsRequest = CreateTagsRequest.builder()
                    .resources(instanceId)
                    .tags(nameTag, managedByTag)
                    .build();

            if (tags != null && !tags.isEmpty()) {
                List<Tag> customTags = tags.entrySet().stream()
                        .map(e -> Tag.builder().key(e.getKey()).value(e.getValue()).build())
                        .toList();
                tagsRequest = tagsRequest.toBuilder()
                        .tags(nameTag, managedByTag)
                        .tags(customTags)
                        .build();
            }

            ec2Client.createTags(tagsRequest);

            return instanceId;

        } catch (Ec2Exception e) {
            log.error("Failed to launch EC2 instance: {}", e.getMessage());
            throw new Ec2OperationException("Failed to launch EC2 instance: " + e.getMessage(), e);
        }
    }

    public void startInstance(String instanceId) {
        log.info("Starting EC2 instance: {}", instanceId);

        try {
            StartInstancesRequest request = StartInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            ec2Client.startInstances(request);
            log.info("Started EC2 instance: {}", instanceId);

        } catch (Ec2Exception e) {
            log.error("Failed to start EC2 instance {}: {}", instanceId, e.getMessage());
            throw new Ec2OperationException("Failed to start EC2 instance: " + e.getMessage(), e);
        }
    }

    public void stopInstance(String instanceId) {
        log.info("Stopping EC2 instance: {}", instanceId);

        try {
            StopInstancesRequest request = StopInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            ec2Client.stopInstances(request);
            log.info("Stopped EC2 instance: {}", instanceId);

        } catch (Ec2Exception e) {
            log.error("Failed to stop EC2 instance {}: {}", instanceId, e.getMessage());
            throw new Ec2OperationException("Failed to stop EC2 instance: " + e.getMessage(), e);
        }
    }

    public void terminateInstance(String instanceId) {
        log.info("Terminating EC2 instance: {}", instanceId);

        try {
            TerminateInstancesRequest request = TerminateInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            ec2Client.terminateInstances(request);
            log.info("Terminated EC2 instance: {}", instanceId);

        } catch (Ec2Exception e) {
            log.error("Failed to terminate EC2 instance {}: {}", instanceId, e.getMessage());
            throw new Ec2OperationException("Failed to terminate EC2 instance: " + e.getMessage(), e);
        }
    }

    public Optional<Instance> describeInstance(String instanceId) {
        log.debug("Describing EC2 instance: {}", instanceId);

        try {
            DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            DescribeInstancesResponse response = ec2Client.describeInstances(request);

            return response.reservations().stream()
                    .flatMap(r -> r.instances().stream())
                    .findFirst();

        } catch (Ec2Exception e) {
            log.error("Failed to describe EC2 instance {}: {}", instanceId, e.getMessage());
            return Optional.empty();
        }
    }

    public void syncNodeFromEc2(Node node) {
        if (node.getInstanceId() == null || node.getInstanceId().isEmpty()) {
            return;
        }

        describeInstance(node.getInstanceId()).ifPresent(instance -> {
            node.setPrivateIp(instance.privateIpAddress());
            node.setPublicIp(instance.publicIpAddress());
            node.setInstanceType(instance.instanceType().toString());
            node.setAvailabilityZone(instance.placement().availabilityZone());
            node.setStatus(mapEc2StateToNodeStatus(instance.state()));
        });
    }

    public NodeStatus mapEc2StateToNodeStatus(InstanceState state) {
        if (state == null) {
            return NodeStatus.PENDING;
        }

        return switch (state.name()) {
            case PENDING -> NodeStatus.PENDING;
            case RUNNING -> NodeStatus.RUNNING;
            case STOPPING, STOPPED -> NodeStatus.STOPPED;
            case SHUTTING_DOWN, TERMINATED -> NodeStatus.TERMINATED;
            default -> NodeStatus.UNHEALTHY;
        };
    }

    public List<Instance> listManagedInstances() {
        log.debug("Listing managed EC2 instances");

        try {
            Filter managedFilter = Filter.builder()
                    .name("tag:ManagedBy")
                    .values("ClusterAdmin")
                    .build();

            DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                    .filters(managedFilter)
                    .build();

            DescribeInstancesResponse response = ec2Client.describeInstances(request);

            return response.reservations().stream()
                    .flatMap(r -> r.instances().stream())
                    .toList();

        } catch (Ec2Exception e) {
            log.error("Failed to list managed instances: {}", e.getMessage());
            throw new Ec2OperationException("Failed to list managed instances: " + e.getMessage(), e);
        }
    }
}
