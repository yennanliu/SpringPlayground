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
    public ResponseEntity<byte[]> downloadPrivateKey(@PathVariable String region) {
        if (!SUPPORTED_REGIONS.contains(region)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Ensure key pair exists
            keyPairService.getOrCreateKeyPair(region);

            // Get private key content
            String privateKeyContent = keyPairService.getPrivateKeyContent(region);
            String filename = "clusteradmin-" + region + ".pem";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(privateKeyContent.getBytes());

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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

            return ResponseEntity.ok(Map.of(
                    "region", region,
                    "keyName", keyName,
                    "localPath", keyPath,
                    "downloadUrl", "/api/v1/keypairs/" + region + "/download"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
