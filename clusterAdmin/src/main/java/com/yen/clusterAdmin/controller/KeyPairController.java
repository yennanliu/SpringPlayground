package com.yen.clusterAdmin.controller;

import com.yen.clusterAdmin.service.Ec2KeyPairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/keypairs")
@Tag(name = "Key Pair Management", description = "APIs for managing SSH key pairs")
public class KeyPairController {

    private final Ec2KeyPairService keyPairService;

    // Supported regions
    private static final List<String> SUPPORTED_REGIONS = List.of(
            "us-east-1", "us-west-2", "eu-west-1", "ap-northeast-1", "ap-southeast-1"
    );

    public KeyPairController(Ec2KeyPairService keyPairService) {
        this.keyPairService = keyPairService;
    }

    @GetMapping("/regions")
    @Operation(summary = "List available regions", description = "List all supported AWS regions for key pairs")
    public ResponseEntity<List<String>> listRegions() {
        return ResponseEntity.ok(SUPPORTED_REGIONS);
    }

    @PostMapping("/{region}")
    @Operation(summary = "Create key pair", description = "Create a new SSH key pair for a specific region")
    public ResponseEntity<Map<String, String>> createKeyPair(@PathVariable String region) {
        if (!SUPPORTED_REGIONS.contains(region)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unsupported region: " + region));
        }

        String keyName = keyPairService.getOrCreateKeyPair(region);
        return ResponseEntity.ok(Map.of(
                "region", region,
                "keyName", keyName,
                "message", "Key pair created/retrieved successfully"
        ));
    }

    @GetMapping("/{region}/download")
    @Operation(summary = "Download private key", description = "Download the private key file for a specific region")
    public ResponseEntity<?> downloadPrivateKey(@PathVariable String region) {
        if (!SUPPORTED_REGIONS.contains(region)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unsupported region: " + region));
        }

        try {
            // Check if private key exists locally first
            if (!keyPairService.hasPrivateKey(region)) {
                return ResponseEntity.status(404)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Map.of(
                                "error", "Private key not available",
                                "message", "The private key for region '" + region + "' is not available on this server. " +
                                        "This can happen if the key was created on a different server or the file was deleted.",
                                "solution", "To fix this: 1) Terminate existing instances in this region, " +
                                        "2) Delete the key pair 'clusteradmin-" + region + "' in AWS Console (EC2 > Key Pairs), " +
                                        "3) Create a new instance (a new key will be automatically generated)"
                        ));
            }

            // Get private key content
            String privateKeyContent = keyPairService.getPrivateKeyContent(region);
            String filename = "clusteradmin-" + region + ".pem";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(privateKeyContent.getBytes());

        } catch (Ec2KeyPairService.KeyPairNotFoundException e) {
            return ResponseEntity.status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to download key: " + e.getMessage()));
        }
    }

    @GetMapping("/{region}/info")
    @Operation(summary = "Get key pair info", description = "Get information about the key pair for a specific region")
    public ResponseEntity<Map<String, Object>> getKeyPairInfo(@PathVariable String region) {
        if (!SUPPORTED_REGIONS.contains(region)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unsupported region: " + region));
        }

        try {
            String keyName = keyPairService.getOrCreateKeyPair(region);
            String keyPath = keyPairService.getPrivateKeyPath(keyName).toString();
            boolean keyAvailable = keyPairService.hasPrivateKey(region);

            Map<String, Object> response = new java.util.HashMap<>();
            response.put("region", region);
            response.put("keyName", keyName);
            response.put("localPath", keyPath);
            response.put("keyAvailable", keyAvailable);

            if (keyAvailable) {
                response.put("downloadUrl", "/api/v1/keypairs/" + region + "/download");
            } else {
                response.put("error", "Private key not available on this server");
                response.put("solution", "Delete the key pair in AWS Console and create a new instance");
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
