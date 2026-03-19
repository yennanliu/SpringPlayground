package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.exception.NodeNotFoundException;
import com.yen.clusterAdmin.model.dto.NodeCreateRequest;
import com.yen.clusterAdmin.model.dto.NodeDTO;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.repository.NodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NodeService {

    private static final Logger log = LoggerFactory.getLogger(NodeService.class);

    private final NodeRepository nodeRepository;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public List<NodeDTO> getAllNodes() {
        return nodeRepository.findAll().stream()
                .map(NodeDTO::fromEntity)
                .toList();
    }

    public NodeDTO getNodeById(UUID id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));
        return NodeDTO.fromEntity(node);
    }

    public List<NodeDTO> getNodesByStatus(NodeStatus status) {
        return nodeRepository.findByStatus(status).stream()
                .map(NodeDTO::fromEntity)
                .toList();
    }

    @Transactional
    public NodeDTO createNode(NodeCreateRequest request) {
        log.info("Creating new node: {}", request.getName());

        Node node = Node.builder()
                .name(request.getName())
                .instanceType(request.getInstanceType() != null ? request.getInstanceType() : "t3.medium")
                .availabilityZone(request.getAvailabilityZone() != null ? request.getAvailabilityZone() : "us-east-1a")
                .status(NodeStatus.PENDING)
                .build();

        Node savedNode = nodeRepository.save(node);
        log.info("Created node with id: {}", savedNode.getId());

        return NodeDTO.fromEntity(savedNode);
    }

    @Transactional
    public NodeDTO updateNode(UUID id, NodeCreateRequest request) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        if (request.getName() != null) {
            node.setName(request.getName());
        }
        if (request.getInstanceType() != null) {
            node.setInstanceType(request.getInstanceType());
        }
        if (request.getAvailabilityZone() != null) {
            node.setAvailabilityZone(request.getAvailabilityZone());
        }

        Node updatedNode = nodeRepository.save(node);
        log.info("Updated node: {}", id);

        return NodeDTO.fromEntity(updatedNode);
    }

    @Transactional
    public void deleteNode(UUID id) {
        if (!nodeRepository.existsById(id)) {
            throw new NodeNotFoundException(id);
        }
        nodeRepository.deleteById(id);
        log.info("Deleted node: {}", id);
    }

    @Transactional
    public NodeDTO startNode(UUID id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        if (node.getStatus() == NodeStatus.TERMINATED) {
            throw new IllegalStateException("Cannot start a terminated node");
        }

        node.setStatus(NodeStatus.RUNNING);
        Node updatedNode = nodeRepository.save(node);
        log.info("Started node: {}", id);

        return NodeDTO.fromEntity(updatedNode);
    }

    @Transactional
    public NodeDTO stopNode(UUID id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        if (node.getStatus() == NodeStatus.TERMINATED) {
            throw new IllegalStateException("Cannot stop a terminated node");
        }

        node.setStatus(NodeStatus.STOPPED);
        Node updatedNode = nodeRepository.save(node);
        log.info("Stopped node: {}", id);

        return NodeDTO.fromEntity(updatedNode);
    }

    @Transactional
    public NodeDTO terminateNode(UUID id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        node.setStatus(NodeStatus.TERMINATED);
        Node updatedNode = nodeRepository.save(node);
        log.info("Terminated node: {}", id);

        return NodeDTO.fromEntity(updatedNode);
    }

    public long countByStatus(NodeStatus status) {
        return nodeRepository.countByStatus(status);
    }

    public long getTotalNodeCount() {
        return nodeRepository.count();
    }
}
