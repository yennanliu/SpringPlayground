package com.yen.clusterAdmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Ec2NetworkService {

    private static final Logger log = LoggerFactory.getLogger(Ec2NetworkService.class);
    private static final String SECURITY_GROUP_NAME = "ClusterAdmin-SG";
    private static final String SECURITY_GROUP_DESC = "Security group for ClusterAdmin managed instances";

    private final Ec2ClientFactory ec2ClientFactory;

    // Cache for region network config to avoid repeated API calls
    private final Map<String, RegionNetworkConfig> networkConfigCache = new ConcurrentHashMap<>();

    public Ec2NetworkService(Ec2ClientFactory ec2ClientFactory) {
        this.ec2ClientFactory = ec2ClientFactory;
    }

    public RegionNetworkConfig getOrCreateNetworkConfig(String region) {
        return networkConfigCache.computeIfAbsent(region, this::createNetworkConfig);
    }

    private RegionNetworkConfig createNetworkConfig(String region) {
        log.info("Setting up network configuration for region: {}", region);
        Ec2Client ec2Client = ec2ClientFactory.getClient(region);

        // Step 1: Get or create default VPC
        String vpcId = getOrCreateDefaultVpc(ec2Client, region);

        // Step 2: Get a subnet from the VPC
        String subnetId = getFirstSubnet(ec2Client, vpcId, region);

        // Step 3: Get or create security group
        String securityGroupId = getOrCreateSecurityGroup(ec2Client, vpcId, region);

        RegionNetworkConfig config = new RegionNetworkConfig(vpcId, subnetId, securityGroupId);
        log.info("Network configuration for region {}: vpcId={}, subnetId={}, securityGroupId={}",
                region, vpcId, subnetId, securityGroupId);

        return config;
    }

    private String getOrCreateDefaultVpc(Ec2Client ec2Client, String region) {
        // Try to find existing default VPC
        try {
            DescribeVpcsRequest request = DescribeVpcsRequest.builder()
                    .filters(Filter.builder().name("isDefault").values("true").build())
                    .build();

            DescribeVpcsResponse response = ec2Client.describeVpcs(request);

            if (!response.vpcs().isEmpty()) {
                String vpcId = response.vpcs().get(0).vpcId();
                log.debug("Found existing default VPC in {}: {}", region, vpcId);
                return vpcId;
            }
        } catch (Ec2Exception e) {
            log.warn("Error checking for default VPC in {}: {}", region, e.getMessage());
        }

        // Create default VPC if none exists
        log.info("No default VPC found in {}, creating one...", region);
        try {
            CreateDefaultVpcResponse response = ec2Client.createDefaultVpc(CreateDefaultVpcRequest.builder().build());
            String vpcId = response.vpc().vpcId();
            log.info("Created default VPC in {}: {}", region, vpcId);
            return vpcId;
        } catch (Ec2Exception e) {
            log.error("Failed to create default VPC in {}: {}", region, e.getMessage());
            throw new RuntimeException("Failed to create default VPC in " + region + ": " + e.getMessage(), e);
        }
    }

    private String getFirstSubnet(Ec2Client ec2Client, String vpcId, String region) {
        try {
            DescribeSubnetsRequest request = DescribeSubnetsRequest.builder()
                    .filters(Filter.builder().name("vpc-id").values(vpcId).build())
                    .build();

            DescribeSubnetsResponse response = ec2Client.describeSubnets(request);

            if (!response.subnets().isEmpty()) {
                // Prefer subnets that are default for their AZ
                Optional<Subnet> defaultSubnet = response.subnets().stream()
                        .filter(Subnet::defaultForAz)
                        .findFirst();

                String subnetId = defaultSubnet.orElse(response.subnets().get(0)).subnetId();
                log.debug("Using subnet in {}: {}", region, subnetId);
                return subnetId;
            }
        } catch (Ec2Exception e) {
            log.error("Failed to get subnets in {}: {}", region, e.getMessage());
        }

        throw new RuntimeException("No subnets found in VPC " + vpcId + " in region " + region);
    }

    private String getOrCreateSecurityGroup(Ec2Client ec2Client, String vpcId, String region) {
        // Try to find existing ClusterAdmin security group
        try {
            DescribeSecurityGroupsRequest request = DescribeSecurityGroupsRequest.builder()
                    .filters(
                            Filter.builder().name("group-name").values(SECURITY_GROUP_NAME).build(),
                            Filter.builder().name("vpc-id").values(vpcId).build()
                    )
                    .build();

            DescribeSecurityGroupsResponse response = ec2Client.describeSecurityGroups(request);

            if (!response.securityGroups().isEmpty()) {
                String sgId = response.securityGroups().get(0).groupId();
                log.debug("Found existing security group in {}: {}", region, sgId);
                return sgId;
            }
        } catch (Ec2Exception e) {
            log.debug("Security group not found, will create: {}", e.getMessage());
        }

        // Create security group
        log.info("Creating security group {} in {}", SECURITY_GROUP_NAME, region);
        try {
            CreateSecurityGroupRequest createRequest = CreateSecurityGroupRequest.builder()
                    .groupName(SECURITY_GROUP_NAME)
                    .description(SECURITY_GROUP_DESC)
                    .vpcId(vpcId)
                    .build();

            CreateSecurityGroupResponse createResponse = ec2Client.createSecurityGroup(createRequest);
            String sgId = createResponse.groupId();

            // Add inbound rules: SSH (22) and application port (8080)
            addSecurityGroupRules(ec2Client, sgId);

            // Tag the security group
            ec2Client.createTags(CreateTagsRequest.builder()
                    .resources(sgId)
                    .tags(
                            Tag.builder().key("Name").value(SECURITY_GROUP_NAME).build(),
                            Tag.builder().key("ManagedBy").value("ClusterAdmin").build()
                    )
                    .build());

            log.info("Created security group in {}: {}", region, sgId);
            return sgId;

        } catch (Ec2Exception e) {
            log.error("Failed to create security group in {}: {}", region, e.getMessage());
            throw new RuntimeException("Failed to create security group in " + region + ": " + e.getMessage(), e);
        }
    }

    private void addSecurityGroupRules(Ec2Client ec2Client, String securityGroupId) {
        try {
            AuthorizeSecurityGroupIngressRequest request = AuthorizeSecurityGroupIngressRequest.builder()
                    .groupId(securityGroupId)
                    .ipPermissions(
                            // SSH access
                            IpPermission.builder()
                                    .ipProtocol("tcp")
                                    .fromPort(22)
                                    .toPort(22)
                                    .ipRanges(IpRange.builder().cidrIp("0.0.0.0/0").description("SSH access").build())
                                    .build(),
                            // Application port
                            IpPermission.builder()
                                    .ipProtocol("tcp")
                                    .fromPort(8080)
                                    .toPort(8080)
                                    .ipRanges(IpRange.builder().cidrIp("0.0.0.0/0").description("Application port").build())
                                    .build()
                    )
                    .build();

            ec2Client.authorizeSecurityGroupIngress(request);
            log.debug("Added inbound rules to security group: {}", securityGroupId);

        } catch (Ec2Exception e) {
            // Rules might already exist
            log.warn("Could not add security group rules (may already exist): {}", e.getMessage());
        }
    }

    public void clearCache() {
        networkConfigCache.clear();
    }

    public void clearCache(String region) {
        networkConfigCache.remove(region);
    }

    public record RegionNetworkConfig(String vpcId, String subnetId, String securityGroupId) {}
}
