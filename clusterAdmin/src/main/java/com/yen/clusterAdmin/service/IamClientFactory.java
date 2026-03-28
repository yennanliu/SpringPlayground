package com.yen.clusterAdmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;

@Component
public class IamClientFactory {

    private static final Logger log = LoggerFactory.getLogger(IamClientFactory.class);

    @Value("${aws.accessKeyId:}")
    private String accessKeyId;

    @Value("${aws.secretKey:}")
    private String secretKey;

    private volatile IamClient client;

    /**
     * Get or create the IAM client.
     * IAM is a global service, so we only need one client.
     */
    public IamClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = createClient();
                }
            }
        }
        return client;
    }

    private IamClient createClient() {
        log.info("Creating IamClient");

        AwsCredentialsProvider credentialsProvider;

        if (accessKeyId != null && !accessKeyId.isEmpty()
                && secretKey != null && !secretKey.isEmpty()) {
            credentialsProvider = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKeyId, secretKey)
            );
        } else {
            credentialsProvider = DefaultCredentialsProvider.create();
        }

        // IAM is a global service, but we need to specify a region for the SDK
        return IamClient.builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
