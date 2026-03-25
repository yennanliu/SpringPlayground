package com.yen.clusterAdmin.controller;

import com.yen.clusterAdmin.model.dto.CommandResultDTO;
import com.yen.clusterAdmin.model.dto.ModifyInstanceRequest;
import com.yen.clusterAdmin.model.dto.NodeCreateRequest;
import com.yen.clusterAdmin.model.dto.NodeDTO;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.service.Ec2ModifyService;
import com.yen.clusterAdmin.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.ec2.model.Volume;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nodes")
@Tag(name = "Node Management", description = "APIs for managing cluster nodes")
public class NodeController {

    private final NodeService nodeService;
    private final Ec2ModifyService ec2ModifyService;

    public NodeController(NodeService nodeService, Ec2ModifyService ec2ModifyService) {
        this.nodeService = nodeService;
        this.ec2ModifyService = ec2ModifyService;
    }

    @GetMapping
    @Operation(summary = "List all nodes", description = "Retrieve all nodes in the cluster")
    public ResponseEntity<List<NodeDTO>> getAllNodes(
            @RequestParam(required = false) NodeStatus status) {
        List<NodeDTO> nodes;
        if (status != null) {
            nodes = nodeService.getNodesByStatus(status);
        } else {
            nodes = nodeService.getAllNodes();
        }
        return ResponseEntity.ok(nodes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get node by ID", description = "Retrieve a specific node by its ID")
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable UUID id) {
        NodeDTO node = nodeService.getNodeById(id);
        return ResponseEntity.ok(node);
    }

    @PostMapping
    @Operation(summary = "Create new node", description = "Register a new node in the cluster")
    public ResponseEntity<NodeDTO> createNode(@Valid @RequestBody NodeCreateRequest request) {
        NodeDTO node = nodeService.createNode(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(node);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update node", description = "Update an existing node's information")
    public ResponseEntity<NodeDTO> updateNode(
            @PathVariable UUID id,
            @Valid @RequestBody NodeCreateRequest request) {
        NodeDTO node = nodeService.updateNode(id, request);
        return ResponseEntity.ok(node);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete node", description = "Remove a node from the cluster")
    public ResponseEntity<Void> deleteNode(@PathVariable UUID id) {
        nodeService.deleteNode(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start node", description = "Start a stopped EC2 instance")
    public ResponseEntity<NodeDTO> startNode(@PathVariable UUID id) {
        NodeDTO node = nodeService.startNode(id);
        return ResponseEntity.ok(node);
    }

    @PostMapping("/{id}/stop")
    @Operation(summary = "Stop node", description = "Stop a running EC2 instance")
    public ResponseEntity<NodeDTO> stopNode(@PathVariable UUID id) {
        NodeDTO node = nodeService.stopNode(id);
        return ResponseEntity.ok(node);
    }

    @PostMapping("/{id}/terminate")
    @Operation(summary = "Terminate node", description = "Terminate an EC2 instance")
    public ResponseEntity<NodeDTO> terminateNode(@PathVariable UUID id) {
        NodeDTO node = nodeService.terminateNode(id);
        return ResponseEntity.ok(node);
    }

    @PostMapping("/{id}/sync")
    @Operation(summary = "Sync node", description = "Sync node state with EC2 instance")
    public ResponseEntity<NodeDTO> syncNode(@PathVariable UUID id) {
        NodeDTO node = nodeService.syncNode(id);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/{id}/ssh")
    @Operation(summary = "Get SSH command", description = "Get the SSH command to connect to this node")
    public ResponseEntity<SshCommandResponse> getSshCommand(@PathVariable UUID id) {
        String sshCommand = nodeService.getSshCommand(id);
        return ResponseEntity.ok(new SshCommandResponse(sshCommand));
    }

    // ==================== EC2 Modification Endpoints ====================

    @PostMapping("/{id}/modify-type")
    @Operation(summary = "Modify instance type", description = "Change the EC2 instance type (will stop and restart if running)")
    public ResponseEntity<Map<String, String>> modifyInstanceType(
            @PathVariable UUID id,
            @RequestBody ModifyInstanceRequest request) {

        if (request.getInstanceType() == null || request.getInstanceType().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "instanceType is required"));
        }

        NodeDTO node = nodeService.getNodeById(id);
        if (node.getInstanceId() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Node does not have an EC2 instance"));
        }

        ec2ModifyService.modifyInstanceType(
                node.getInstanceId(),
                request.getInstanceType(),
                node.getRegion()
        );

        // Update the node's instance type in database
        NodeCreateRequest updateRequest = new NodeCreateRequest();
        updateRequest.setInstanceType(request.getInstanceType());
        nodeService.updateNode(id, updateRequest);

        return ResponseEntity.ok(Map.of(
                "message", "Instance type modified successfully",
                "newType", request.getInstanceType()
        ));
    }

    @PostMapping("/{id}/install-packages")
    @Operation(summary = "Install packages", description = "Install packages on the EC2 instance via SSM")
    public ResponseEntity<CommandResultDTO> installPackages(
            @PathVariable UUID id,
            @RequestBody ModifyInstanceRequest request) {

        if (request.getPackages() == null || request.getPackages().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new CommandResultDTO(null, "packages list is required"));
        }

        NodeDTO node = nodeService.getNodeById(id);
        if (node.getInstanceId() == null) {
            return ResponseEntity.badRequest()
                    .body(new CommandResultDTO(null, "Node does not have an EC2 instance"));
        }

        if (node.getStatus() != NodeStatus.RUNNING) {
            return ResponseEntity.badRequest()
                    .body(new CommandResultDTO(null, "Instance must be running to install packages"));
        }

        String commandId = ec2ModifyService.installPackages(
                node.getInstanceId(),
                request.getPackages(),
                node.getRegion()
        );

        return ResponseEntity.ok(new CommandResultDTO(
                commandId,
                "Package installation command sent. Use GET /nodes/" + id + "/commands/" + commandId + " to check status"
        ));
    }

    @PostMapping("/{id}/run-command")
    @Operation(summary = "Run shell command", description = "Run shell commands on the EC2 instance via SSM")
    public ResponseEntity<CommandResultDTO> runCommand(
            @PathVariable UUID id,
            @RequestBody ModifyInstanceRequest request) {

        if (request.getCommands() == null || request.getCommands().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new CommandResultDTO(null, "commands list is required"));
        }

        NodeDTO node = nodeService.getNodeById(id);
        if (node.getInstanceId() == null) {
            return ResponseEntity.badRequest()
                    .body(new CommandResultDTO(null, "Node does not have an EC2 instance"));
        }

        if (node.getStatus() != NodeStatus.RUNNING) {
            return ResponseEntity.badRequest()
                    .body(new CommandResultDTO(null, "Instance must be running to execute commands"));
        }

        String commandId = ec2ModifyService.runCommand(
                node.getInstanceId(),
                request.getCommands(),
                node.getRegion()
        );

        return ResponseEntity.ok(new CommandResultDTO(
                commandId,
                "Command sent. Use GET /nodes/" + id + "/commands/" + commandId + " to check status"
        ));
    }

    @GetMapping("/{id}/commands/{commandId}")
    @Operation(summary = "Get command result", description = "Get the result of an SSM command")
    public ResponseEntity<CommandResultDTO> getCommandResult(
            @PathVariable UUID id,
            @PathVariable String commandId) {

        NodeDTO node = nodeService.getNodeById(id);
        if (node.getInstanceId() == null) {
            return ResponseEntity.badRequest()
                    .body(new CommandResultDTO(null, "Node does not have an EC2 instance"));
        }

        Ec2ModifyService.CommandResult result = ec2ModifyService.getCommandResult(
                commandId,
                node.getInstanceId(),
                node.getRegion()
        );

        return ResponseEntity.ok(new CommandResultDTO(
                commandId,
                result.status(),
                result.output(),
                result.error(),
                result.responseCode()
        ));
    }

    @GetMapping("/{id}/volumes")
    @Operation(summary = "Get volumes", description = "Get EBS volumes attached to this node")
    public ResponseEntity<List<VolumeInfo>> getVolumes(@PathVariable UUID id) {
        NodeDTO node = nodeService.getNodeById(id);
        if (node.getInstanceId() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Volume> volumes = ec2ModifyService.getInstanceVolumes(
                node.getInstanceId(),
                node.getRegion()
        );

        List<VolumeInfo> volumeInfos = volumes.stream()
                .map(v -> new VolumeInfo(
                        v.volumeId(),
                        v.size(),
                        v.volumeTypeAsString(),
                        v.stateAsString(),
                        v.attachments().isEmpty() ? null : v.attachments().get(0).device()
                ))
                .toList();

        return ResponseEntity.ok(volumeInfos);
    }

    @PostMapping("/{id}/volumes/{volumeId}/resize")
    @Operation(summary = "Resize volume", description = "Resize an EBS volume (can only increase size)")
    public ResponseEntity<Map<String, String>> resizeVolume(
            @PathVariable UUID id,
            @PathVariable String volumeId,
            @RequestBody ModifyInstanceRequest request) {

        if (request.getVolumeSizeGb() == null || request.getVolumeSizeGb() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "volumeSizeGb is required and must be positive"));
        }

        NodeDTO node = nodeService.getNodeById(id);

        ec2ModifyService.modifyVolumeSize(volumeId, request.getVolumeSizeGb(), node.getRegion());

        return ResponseEntity.ok(Map.of(
                "message", "Volume resize initiated",
                "volumeId", volumeId,
                "newSize", request.getVolumeSizeGb() + "GB"
        ));
    }

    public record SshCommandResponse(String command) {}

    public record VolumeInfo(
            String volumeId,
            int sizeGb,
            String volumeType,
            String state,
            String device
    ) {}
}
