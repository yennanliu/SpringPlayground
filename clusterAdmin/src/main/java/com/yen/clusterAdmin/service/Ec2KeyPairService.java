package com.yen.clusterAdmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Ec2KeyPairService {

    private static final Logger log = LoggerFactory.getLogger(Ec2KeyPairService.class);
    private static final String KEY_PAIR_PREFIX = "clusteradmin-";

    private final Ec2ClientFactory ec2ClientFactory;

    @Value("${ec2.keypair.storage-path:${user.home}/.clusteradmin/keys}")
    private String keyStoragePath;

    // Cache of region -> key pair name
    private final Map<String, String> keyPairCache = new ConcurrentHashMap<>();

    public Ec2KeyPairService(Ec2ClientFactory ec2ClientFactory) {
        this.ec2ClientFactory = ec2ClientFactory;
    }

    /**
     * Get or create a key pair for the specified region.
     * Returns the key pair name to use when launching instances.
     */
    public String getOrCreateKeyPair(String region) {
        return keyPairCache.computeIfAbsent(region, this::ensureKeyPair);
    }

    private String ensureKeyPair(String region) {
        String keyName = KEY_PAIR_PREFIX + region;
        Ec2Client ec2Client = ec2ClientFactory.getClient(region);

        // Check if key pair already exists in AWS
        if (keyPairExistsInAws(ec2Client, keyName)) {
            // Check if we have the private key locally
            if (privateKeyExists(keyName)) {
                log.info("Using existing key pair for region {}: {}", region, keyName);
                return keyName;
            } else {
                // Key exists in AWS but we don't have private key - delete and recreate
                log.warn("Key pair {} exists in AWS but private key not found locally. Recreating...", keyName);
                deleteKeyPair(ec2Client, keyName);
            }
        }

        // Create new key pair
        return createKeyPair(ec2Client, region, keyName);
    }

    private boolean keyPairExistsInAws(Ec2Client ec2Client, String keyName) {
        try {
            DescribeKeyPairsRequest request = DescribeKeyPairsRequest.builder()
                    .keyNames(keyName)
                    .build();
            DescribeKeyPairsResponse response = ec2Client.describeKeyPairs(request);
            return !response.keyPairs().isEmpty();
        } catch (Ec2Exception e) {
            // Key pair not found
            return false;
        }
    }

    private boolean privateKeyExists(String keyName) {
        Path keyPath = getPrivateKeyPath(keyName);
        return Files.exists(keyPath);
    }

    private void deleteKeyPair(Ec2Client ec2Client, String keyName) {
        try {
            DeleteKeyPairRequest request = DeleteKeyPairRequest.builder()
                    .keyName(keyName)
                    .build();
            ec2Client.deleteKeyPair(request);
            log.info("Deleted key pair: {}", keyName);
        } catch (Ec2Exception e) {
            log.warn("Failed to delete key pair {}: {}", keyName, e.getMessage());
        }
    }

    private String createKeyPair(Ec2Client ec2Client, String region, String keyName) {
        log.info("Creating new key pair for region {}: {}", region, keyName);

        try {
            CreateKeyPairRequest request = CreateKeyPairRequest.builder()
                    .keyName(keyName)
                    .keyType(KeyType.RSA)
                    .keyFormat(KeyFormat.PEM)
                    .tagSpecifications(TagSpecification.builder()
                            .resourceType(ResourceType.KEY_PAIR)
                            .tags(
                                    Tag.builder().key("Name").value(keyName).build(),
                                    Tag.builder().key("ManagedBy").value("ClusterAdmin").build(),
                                    Tag.builder().key("Region").value(region).build()
                            )
                            .build())
                    .build();

            CreateKeyPairResponse response = ec2Client.createKeyPair(request);

            // Save private key to local storage
            savePrivateKey(keyName, response.keyMaterial());

            log.info("Created and saved key pair: {}", keyName);
            return keyName;

        } catch (Ec2Exception e) {
            log.error("Failed to create key pair for region {}: {}", region, e.getMessage());
            throw new RuntimeException("Failed to create key pair: " + e.getMessage(), e);
        }
    }

    private void savePrivateKey(String keyName, String privateKeyContent) {
        try {
            Path storageDir = Paths.get(keyStoragePath);
            Files.createDirectories(storageDir);

            Path keyPath = getPrivateKeyPath(keyName);
            Files.writeString(keyPath, privateKeyContent);

            // Set file permissions to 600 (owner read/write only) on Unix systems
            try {
                Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-------");
                Files.setPosixFilePermissions(keyPath, permissions);
            } catch (UnsupportedOperationException e) {
                // Windows doesn't support POSIX permissions
                log.debug("POSIX file permissions not supported on this system");
            }

            log.info("Private key saved to: {}", keyPath);

        } catch (IOException e) {
            log.error("Failed to save private key: {}", e.getMessage());
            throw new RuntimeException("Failed to save private key: " + e.getMessage(), e);
        }
    }

    public Path getPrivateKeyPath(String keyName) {
        return Paths.get(keyStoragePath, keyName + ".pem");
    }

    public String getPrivateKeyContent(String region) {
        String keyName = KEY_PAIR_PREFIX + region;
        Path keyPath = getPrivateKeyPath(keyName);

        if (!Files.exists(keyPath)) {
            throw new RuntimeException("Private key not found for region: " + region);
        }

        try {
            return Files.readString(keyPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read private key: " + e.getMessage(), e);
        }
    }

    /**
     * Get SSH command to connect to an instance
     */
    public String getSshCommand(String region, String publicIp, String user) {
        String keyName = KEY_PAIR_PREFIX + region;
        Path keyPath = getPrivateKeyPath(keyName);
        String sshUser = user != null ? user : "ec2-user"; // Default for Amazon Linux
        return String.format("ssh -i %s %s@%s", keyPath, sshUser, publicIp);
    }

    public void clearCache() {
        keyPairCache.clear();
    }
}
