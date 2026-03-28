package com.yen.clusterAdmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class Ec2IamService {

    private static final Logger log = LoggerFactory.getLogger(Ec2IamService.class);

    private static final String ROLE_NAME = "ClusterAdmin-EC2-SSM-Role";
    private static final String INSTANCE_PROFILE_NAME = "ClusterAdmin-EC2-SSM-Profile";
    private static final String SSM_MANAGED_POLICY_ARN = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore";

    private static final String ASSUME_ROLE_POLICY = """
            {
                "Version": "2012-10-17",
                "Statement": [
                    {
                        "Effect": "Allow",
                        "Principal": {
                            "Service": "ec2.amazonaws.com"
                        },
                        "Action": "sts:AssumeRole"
                    }
                ]
            }
            """;

    private final IamClient iamClient;

    // Cache the instance profile name once created/verified
    private final AtomicReference<String> cachedInstanceProfileName = new AtomicReference<>();

    public Ec2IamService() {
        this.iamClient = IamClient.builder().build();
    }

    /**
     * Get or create an instance profile with SSM permissions for EC2 instances.
     * The instance profile is global (not region-specific) in IAM.
     *
     * @return the instance profile name to use when launching EC2 instances
     */
    public String getOrCreateSsmInstanceProfile() {
        String cached = cachedInstanceProfileName.get();
        if (cached != null) {
            return cached;
        }

        synchronized (this) {
            // Double-check after acquiring lock
            cached = cachedInstanceProfileName.get();
            if (cached != null) {
                return cached;
            }

            log.info("Setting up IAM instance profile for SSM access");

            // Step 1: Get or create the IAM role
            String roleArn = getOrCreateRole();

            // Step 2: Attach SSM managed policy to the role
            attachSsmPolicy();

            // Step 3: Get or create instance profile and add the role
            String profileName = getOrCreateInstanceProfile(roleArn);

            cachedInstanceProfileName.set(profileName);
            log.info("IAM instance profile ready: {}", profileName);

            return profileName;
        }
    }

    private String getOrCreateRole() {
        // Check if role exists
        try {
            GetRoleRequest getRoleRequest = GetRoleRequest.builder()
                    .roleName(ROLE_NAME)
                    .build();

            GetRoleResponse response = iamClient.getRole(getRoleRequest);
            log.debug("Found existing IAM role: {}", ROLE_NAME);
            return response.role().arn();

        } catch (NoSuchEntityException e) {
            // Role doesn't exist, create it
            log.info("Creating IAM role: {}", ROLE_NAME);
        }

        try {
            CreateRoleRequest createRoleRequest = CreateRoleRequest.builder()
                    .roleName(ROLE_NAME)
                    .assumeRolePolicyDocument(ASSUME_ROLE_POLICY)
                    .description("IAM role for ClusterAdmin EC2 instances to access SSM")
                    .tags(
                            software.amazon.awssdk.services.iam.model.Tag.builder()
                                    .key("ManagedBy").value("ClusterAdmin").build()
                    )
                    .build();

            CreateRoleResponse response = iamClient.createRole(createRoleRequest);
            log.info("Created IAM role: {}", response.role().arn());
            return response.role().arn();

        } catch (IamException e) {
            log.error("Failed to create IAM role: {}", e.getMessage());
            throw new RuntimeException("Failed to create IAM role: " + e.getMessage(), e);
        }
    }

    private void attachSsmPolicy() {
        try {
            // Check if policy is already attached
            ListAttachedRolePoliciesRequest listRequest = ListAttachedRolePoliciesRequest.builder()
                    .roleName(ROLE_NAME)
                    .build();

            ListAttachedRolePoliciesResponse listResponse = iamClient.listAttachedRolePolicies(listRequest);

            boolean alreadyAttached = listResponse.attachedPolicies().stream()
                    .anyMatch(p -> SSM_MANAGED_POLICY_ARN.equals(p.policyArn()));

            if (alreadyAttached) {
                log.debug("SSM managed policy already attached to role");
                return;
            }

            // Attach the policy
            AttachRolePolicyRequest attachRequest = AttachRolePolicyRequest.builder()
                    .roleName(ROLE_NAME)
                    .policyArn(SSM_MANAGED_POLICY_ARN)
                    .build();

            iamClient.attachRolePolicy(attachRequest);
            log.info("Attached SSM managed policy to role: {}", ROLE_NAME);

        } catch (IamException e) {
            log.error("Failed to attach SSM policy to role: {}", e.getMessage());
            throw new RuntimeException("Failed to attach SSM policy: " + e.getMessage(), e);
        }
    }

    private String getOrCreateInstanceProfile(String roleArn) {
        // Check if instance profile exists
        try {
            GetInstanceProfileRequest getRequest = GetInstanceProfileRequest.builder()
                    .instanceProfileName(INSTANCE_PROFILE_NAME)
                    .build();

            GetInstanceProfileResponse response = iamClient.getInstanceProfile(getRequest);
            InstanceProfile profile = response.instanceProfile();

            // Ensure the role is attached to the profile
            boolean roleAttached = profile.roles().stream()
                    .anyMatch(r -> r.roleName().equals(ROLE_NAME));

            if (!roleAttached) {
                addRoleToInstanceProfile();
            }

            log.debug("Found existing instance profile: {}", INSTANCE_PROFILE_NAME);
            return INSTANCE_PROFILE_NAME;

        } catch (NoSuchEntityException e) {
            // Instance profile doesn't exist, create it
            log.info("Creating instance profile: {}", INSTANCE_PROFILE_NAME);
        }

        try {
            CreateInstanceProfileRequest createRequest = CreateInstanceProfileRequest.builder()
                    .instanceProfileName(INSTANCE_PROFILE_NAME)
                    .tags(
                            software.amazon.awssdk.services.iam.model.Tag.builder()
                                    .key("ManagedBy").value("ClusterAdmin").build()
                    )
                    .build();

            iamClient.createInstanceProfile(createRequest);
            log.info("Created instance profile: {}", INSTANCE_PROFILE_NAME);

            // Add the role to the instance profile
            addRoleToInstanceProfile();

            // Wait for instance profile to propagate (IAM is eventually consistent)
            waitForInstanceProfilePropagation();

            return INSTANCE_PROFILE_NAME;

        } catch (IamException e) {
            log.error("Failed to create instance profile: {}", e.getMessage());
            throw new RuntimeException("Failed to create instance profile: " + e.getMessage(), e);
        }
    }

    private void addRoleToInstanceProfile() {
        try {
            AddRoleToInstanceProfileRequest addRoleRequest = AddRoleToInstanceProfileRequest.builder()
                    .instanceProfileName(INSTANCE_PROFILE_NAME)
                    .roleName(ROLE_NAME)
                    .build();

            iamClient.addRoleToInstanceProfile(addRoleRequest);
            log.info("Added role {} to instance profile {}", ROLE_NAME, INSTANCE_PROFILE_NAME);

        } catch (LimitExceededException e) {
            // Role already attached
            log.debug("Role already attached to instance profile");
        } catch (IamException e) {
            log.error("Failed to add role to instance profile: {}", e.getMessage());
            throw new RuntimeException("Failed to add role to instance profile: " + e.getMessage(), e);
        }
    }

    private void waitForInstanceProfilePropagation() {
        // IAM is eventually consistent, wait for the instance profile to be available
        log.debug("Waiting for instance profile propagation...");
        try {
            Thread.sleep(10000); // Wait 10 seconds for IAM propagation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get the instance profile name (returns cached value or null if not yet created)
     */
    public String getInstanceProfileName() {
        return cachedInstanceProfileName.get();
    }

    /**
     * Clear the cached instance profile name
     */
    public void clearCache() {
        cachedInstanceProfileName.set(null);
    }
}
