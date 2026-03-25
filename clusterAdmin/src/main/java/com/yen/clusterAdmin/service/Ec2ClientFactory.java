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
import software.amazon.awssdk.services.ec2.Ec2Client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Ec2ClientFactory {

    private static final Logger log = LoggerFactory.getLogger(Ec2ClientFactory.class);

    private final Map<String, Ec2Client> clients = new ConcurrentHashMap<>();

    @Value("${aws.defaultRegion:us-east-1}")
    private String defaultRegion;

    @Value("${aws.accessKeyId:}")
    private String accessKeyId;

    @Value("${aws.secretKey:}")
    private String secretKey;

    public Ec2Client getClient(String region) {
        String targetRegion = (region != null && !region.isEmpty()) ? region : defaultRegion;
        return clients.computeIfAbsent(targetRegion, this::createClient);
    }

    public String getDefaultRegion() {
        return defaultRegion;
    }

    private Ec2Client createClient(String region) {
        log.info("Creating Ec2Client for region: {}", region);

        AwsCredentialsProvider credentialsProvider;

        if (accessKeyId != null && !accessKeyId.isEmpty()
                && secretKey != null && !secretKey.isEmpty()) {
            credentialsProvider = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKeyId, secretKey)
            );
        } else {
            credentialsProvider = DefaultCredentialsProvider.create();
        }

        return Ec2Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
