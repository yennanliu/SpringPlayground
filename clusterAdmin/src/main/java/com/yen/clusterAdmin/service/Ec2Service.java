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

    private final Ec2ClientFactory ec2ClientFactory;
    private final Ec2Properties ec2Properties;
    private final Ec2NetworkService ec2NetworkService;

    public Ec2Service(Ec2ClientFactory ec2ClientFactory, Ec2Properties ec2Properties, Ec2NetworkService ec2NetworkService) {
        this.ec2ClientFactory = ec2ClientFactory;
        this.ec2Properties = ec2Properties;
        this.ec2NetworkService = ec2NetworkService;
    }

    public String launchInstance(String name, String instanceType, Map<String, String> tags, String region) {
        String targetRegion = resolveRegion(region);
        String resolvedInstanceType = instanceType != null ? instanceType : ec2Properties.getInstanceType();
        String resolvedAmi = ec2Properties.getAmiForRegion(targetRegion);

        log.info("Launching EC2 instance: region={}, name={}, type={}, ami={}",
                targetRegion, name, resolvedInstanceType, resolvedAmi);

        long startTime = System.currentTimeMillis();

        try {
            Ec2Client ec2Client = ec2ClientFactory.getClient(targetRegion);

            RunInstancesRequest.Builder requestBuilder = RunInstancesRequest.builder()
                    .imageId(resolvedAmi)
                    .instanceType(InstanceType.fromValue(resolvedInstanceType))
                    .minCount(1)
                    .maxCount(1);

            // Get key name (optional - no auto-creation for key pairs)
            String keyName = ec2Properties.getKeyNameForRegion(targetRegion);
            if (keyName != null && !keyName.isEmpty()) {
                requestBuilder.keyName(keyName);
                log.debug("Using key name: {}", keyName);
            }

            // Get security group - auto-create if not specified
            String securityGroupId = ec2Properties.getSecurityGroupIdForRegion(targetRegion);
            if (securityGroupId == null || securityGroupId.isEmpty()) {
                log.info("No security group configured for region {}, auto-provisioning network resources...", targetRegion);
                Ec2NetworkService.RegionNetworkConfig networkConfig = ec2NetworkService.getOrCreateNetworkConfig(targetRegion);
                securityGroupId = networkConfig.securityGroupId();

                // Also use auto-provisioned subnet if none specified
                String subnetId = ec2Properties.getSubnetIdForRegion(targetRegion);
                if (subnetId == null || subnetId.isEmpty()) {
                    subnetId = networkConfig.subnetId();
                    requestBuilder.subnetId(subnetId);
                    log.debug("Using auto-provisioned subnet: {}", subnetId);
                }
            }

            requestBuilder.securityGroupIds(securityGroupId);
            log.debug("Using security group: {}", securityGroupId);

            // Get subnet if explicitly configured (and not already set above)
            String subnetId = ec2Properties.getSubnetIdForRegion(targetRegion);
            if (subnetId != null && !subnetId.isEmpty()) {
                requestBuilder.subnetId(subnetId);
                log.debug("Using configured subnet: {}", subnetId);
            }

            RunInstancesResponse response = ec2Client.runInstances(requestBuilder.build());

            if (response.instances().isEmpty()) {
                throw new Ec2OperationException("No instances returned from RunInstances");
            }

            String instanceId = response.instances().get(0).instanceId();
            long duration = System.currentTimeMillis() - startTime;

            log.info("EC2 instance launched successfully: region={}, instanceId={}, requestId={}, duration={}ms",
                    targetRegion, instanceId, response.responseMetadata().requestId(), duration);

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
            long duration = System.currentTimeMillis() - startTime;
            log.error("Failed to launch EC2 instance: region={}, name={}, error={}, duration={}ms",
                    targetRegion, name, e.getMessage(), duration);
            throw new Ec2OperationException("Failed to launch EC2 instance: " + e.getMessage(), e);
        }
    }

    public void startInstance(String instanceId, String region) {
        String targetRegion = resolveRegion(region);
        log.info("Starting EC2 instance: region={}, instanceId={}", targetRegion, instanceId);

        long startTime = System.currentTimeMillis();

        try {
            Ec2Client ec2Client = ec2ClientFactory.getClient(targetRegion);

            StartInstancesRequest request = StartInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            StartInstancesResponse response = ec2Client.startInstances(request);
            long duration = System.currentTimeMillis() - startTime;

            log.info("EC2 instance started successfully: region={}, instanceId={}, requestId={}, duration={}ms",
                    targetRegion, instanceId, response.responseMetadata().requestId(), duration);

        } catch (Ec2Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Failed to start EC2 instance: region={}, instanceId={}, error={}, duration={}ms",
                    targetRegion, instanceId, e.getMessage(), duration);
            throw new Ec2OperationException("Failed to start EC2 instance: " + e.getMessage(), e);
        }
    }

    public void stopInstance(String instanceId, String region) {
        String targetRegion = resolveRegion(region);
        log.info("Stopping EC2 instance: region={}, instanceId={}", targetRegion, instanceId);

        long startTime = System.currentTimeMillis();

        try {
            Ec2Client ec2Client = ec2ClientFactory.getClient(targetRegion);

            StopInstancesRequest request = StopInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            StopInstancesResponse response = ec2Client.stopInstances(request);
            long duration = System.currentTimeMillis() - startTime;

            log.info("EC2 instance stopped successfully: region={}, instanceId={}, requestId={}, duration={}ms",
                    targetRegion, instanceId, response.responseMetadata().requestId(), duration);

        } catch (Ec2Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Failed to stop EC2 instance: region={}, instanceId={}, error={}, duration={}ms",
                    targetRegion, instanceId, e.getMessage(), duration);
            throw new Ec2OperationException("Failed to stop EC2 instance: " + e.getMessage(), e);
        }
    }

    public void terminateInstance(String instanceId, String region) {
        String targetRegion = resolveRegion(region);
        log.info("Terminating EC2 instance: region={}, instanceId={}", targetRegion, instanceId);

        long startTime = System.currentTimeMillis();

        try {
            Ec2Client ec2Client = ec2ClientFactory.getClient(targetRegion);

            TerminateInstancesRequest request = TerminateInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            TerminateInstancesResponse response = ec2Client.terminateInstances(request);
            long duration = System.currentTimeMillis() - startTime;

            log.info("EC2 instance terminated successfully: region={}, instanceId={}, requestId={}, duration={}ms",
                    targetRegion, instanceId, response.responseMetadata().requestId(), duration);

        } catch (Ec2Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Failed to terminate EC2 instance: region={}, instanceId={}, error={}, duration={}ms",
                    targetRegion, instanceId, e.getMessage(), duration);
            throw new Ec2OperationException("Failed to terminate EC2 instance: " + e.getMessage(), e);
        }
    }

    public Optional<Instance> describeInstance(String instanceId, String region) {
        String targetRegion = resolveRegion(region);
        log.debug("Describing EC2 instance: region={}, instanceId={}", targetRegion, instanceId);

        try {
            Ec2Client ec2Client = ec2ClientFactory.getClient(targetRegion);

            DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            DescribeInstancesResponse response = ec2Client.describeInstances(request);

            return response.reservations().stream()
                    .flatMap(r -> r.instances().stream())
                    .findFirst();

        } catch (Ec2Exception e) {
            log.error("Failed to describe EC2 instance: region={}, instanceId={}, error={}",
                    targetRegion, instanceId, e.getMessage());
            return Optional.empty();
        }
    }

    public void syncNodeFromEc2(Node node) {
        if (node.getInstanceId() == null || node.getInstanceId().isEmpty()) {
            return;
        }

        String region = node.getRegion();
        describeInstance(node.getInstanceId(), region).ifPresent(instance -> {
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

    public List<Instance> listManagedInstances(String region) {
        String targetRegion = resolveRegion(region);
        log.debug("Listing managed EC2 instances: region={}", targetRegion);

        try {
            Ec2Client ec2Client = ec2ClientFactory.getClient(targetRegion);

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
            log.error("Failed to list managed instances: region={}, error={}", targetRegion, e.getMessage());
            throw new Ec2OperationException("Failed to list managed instances: " + e.getMessage(), e);
        }
    }

    private String resolveRegion(String region) {
        return (region != null && !region.isEmpty()) ? region : ec2ClientFactory.getDefaultRegion();
    }
}
