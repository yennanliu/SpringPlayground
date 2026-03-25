package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.exception.NodeNotFoundException;
import com.yen.clusterAdmin.model.dto.NodeCreateRequest;
import com.yen.clusterAdmin.model.dto.NodeDTO;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.repository.NodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NodeService {

    private static final Logger log = LoggerFactory.getLogger(NodeService.class);

    private final NodeRepository nodeRepository;
    private final Ec2Service ec2Service;
    private final Ec2ClientFactory ec2ClientFactory;

    @Value("${cluster.ec2.enabled:false}")
    private boolean ec2Enabled;

    public NodeService(NodeRepository nodeRepository, Ec2Service ec2Service, Ec2ClientFactory ec2ClientFactory) {
        this.nodeRepository = nodeRepository;
        this.ec2Service = ec2Service;
        this.ec2ClientFactory = ec2ClientFactory;
    }

    public List<NodeDTO> getAllNodes() {
        List<Node> nodes = nodeRepository.findAll();

        // Sync with EC2 if enabled
        if (ec2Enabled) {
            nodes.forEach(this::syncNodeWithEc2);
        }

        return nodes.stream()
                .map(NodeDTO::fromEntity)
                .toList();
    }

    public NodeDTO getNodeById(UUID id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        if (ec2Enabled) {
            syncNodeWithEc2(node);
        }

        return NodeDTO.fromEntity(node);
    }

    public List<NodeDTO> getNodesByStatus(NodeStatus status) {
        return nodeRepository.findByStatus(status).stream()
                .map(NodeDTO::fromEntity)
                .toList();
    }

    @Transactional
    public NodeDTO createNode(NodeCreateRequest request) {
        log.info("Creating new node: name={}, region={}", request.getName(), request.getRegion());

        String region = resolveRegion(request.getRegion());

        Node node = Node.builder()
                .name(request.getName())
                .region(region)
                .instanceType(request.getInstanceType() != null ? request.getInstanceType() : "t3.medium")
                .availabilityZone(request.getAvailabilityZone() != null ? request.getAvailabilityZone() : region + "a")
                .status(NodeStatus.PENDING)
                .build();

        // Launch EC2 instance if enabled
        if (ec2Enabled) {
            String instanceId = ec2Service.launchInstance(
                    request.getName(),
                    request.getInstanceType(),
                    request.getTags(),
                    region,
                    request.getPackages(),
                    request.getAssignPublicIp()
            );
            node.setInstanceId(instanceId);
            log.info("Launched EC2 instance: instanceId={}, region={}, packages={}, publicIp={}",
                    instanceId, region, request.getPackages(), request.getAssignPublicIp());
        }

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
        if (request.getRegion() != null) {
            node.setRegion(request.getRegion());
        }

        Node updatedNode = nodeRepository.save(node);
        log.info("Updated node: {}", id);

        return NodeDTO.fromEntity(updatedNode);
    }

    @Transactional
    public void deleteNode(UUID id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        // Terminate EC2 instance if it exists
        if (ec2Enabled && node.getInstanceId() != null) {
            ec2Service.terminateInstance(node.getInstanceId(), node.getRegion());
            log.info("Terminated EC2 instance: instanceId={}, region={}", node.getInstanceId(), node.getRegion());
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

        // Start EC2 instance if enabled
        if (ec2Enabled && node.getInstanceId() != null) {
            ec2Service.startInstance(node.getInstanceId(), node.getRegion());
            log.info("Started EC2 instance: instanceId={}, region={}", node.getInstanceId(), node.getRegion());
        }

        node.setStatus(NodeStatus.PENDING); // Will become RUNNING when EC2 reports it
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

        // Stop EC2 instance if enabled
        if (ec2Enabled && node.getInstanceId() != null) {
            ec2Service.stopInstance(node.getInstanceId(), node.getRegion());
            log.info("Stopped EC2 instance: instanceId={}, region={}", node.getInstanceId(), node.getRegion());
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

        // Terminate EC2 instance if enabled
        if (ec2Enabled && node.getInstanceId() != null) {
            ec2Service.terminateInstance(node.getInstanceId(), node.getRegion());
            log.info("Terminated EC2 instance: instanceId={}, region={}", node.getInstanceId(), node.getRegion());
        }

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

    @Transactional
    public NodeDTO syncNode(UUID id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        if (ec2Enabled && node.getInstanceId() != null) {
            syncNodeWithEc2(node);
            nodeRepository.save(node);
        }

        return NodeDTO.fromEntity(node);
    }

    private void syncNodeWithEc2(Node node) {
        if (node.getInstanceId() == null || node.getInstanceId().isEmpty()) {
            return;
        }
        ec2Service.syncNodeFromEc2(node);
    }

    /**
     * Get SSH command to connect to a node
     */
    public String getSshCommand(UUID id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        if (node.getPublicIp() == null || node.getPublicIp().isEmpty()) {
            throw new IllegalStateException("Node does not have a public IP address");
        }

        return ec2Service.getSshCommand(node.getRegion(), node.getPublicIp());
    }

    private String resolveRegion(String region) {
        return (region != null && !region.isEmpty()) ? region : ec2ClientFactory.getDefaultRegion();
    }
}
