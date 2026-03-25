package com.yen.clusterAdmin.controller;

import com.yen.clusterAdmin.model.dto.NodeCreateRequest;
import com.yen.clusterAdmin.model.dto.NodeDTO;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nodes")
@Tag(name = "Node Management", description = "APIs for managing cluster nodes")
public class NodeController {

    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
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

    public record SshCommandResponse(String command) {}
}
