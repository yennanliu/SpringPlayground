package com.yen.clusterAdmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SsmClientFactory {

    private static final Logger log = LoggerFactory.getLogger(SsmClientFactory.class);

    private final Map<String, SsmClient> clients = new ConcurrentHashMap<>();

    @Value("${aws.defaultRegion:us-east-1}")
    private String defaultRegion;

    @Value("${aws.accessKeyId:}")
    private String accessKeyId;

    @Value("${aws.secretKey:}")
    private String secretKey;

    public SsmClient getClient(String region) {
        String targetRegion = (region != null && !region.isEmpty()) ? region : defaultRegion;
        return clients.computeIfAbsent(targetRegion, this::createClient);
    }

    private SsmClient createClient(String region) {
        log.info("Creating SSM client for region: {}", region);

        software.amazon.awssdk.services.ssm.SsmClientBuilder builder = SsmClient.builder()
                .region(Region.of(region));

        if (accessKeyId != null && !accessKeyId.isEmpty() &&
            secretKey != null && !secretKey.isEmpty()) {
            builder.credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKeyId, secretKey)
                    )
            );
        } else {
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        return builder.build();
    }

    public String getDefaultRegion() {
        return defaultRegion;
    }
}
