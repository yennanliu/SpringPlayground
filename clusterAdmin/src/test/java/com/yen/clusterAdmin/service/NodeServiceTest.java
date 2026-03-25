package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.exception.NodeNotFoundException;
import com.yen.clusterAdmin.model.dto.NodeCreateRequest;
import com.yen.clusterAdmin.model.dto.NodeDTO;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.repository.NodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NodeServiceTest {

    @Mock
    private NodeRepository nodeRepository;

    @Mock
    private Ec2Service ec2Service;

    @Mock
    private Ec2ClientFactory ec2ClientFactory;

    private NodeService nodeService;

    private Node testNode;
    private UUID testNodeId;

    private static final String TEST_REGION = "us-east-1";

    @BeforeEach
    void setUp() {
        nodeService = new NodeService(nodeRepository, ec2Service, ec2ClientFactory);

        testNodeId = UUID.randomUUID();
        testNode = new Node();
        testNode.setId(testNodeId);
        testNode.setName("test-worker-001");
        testNode.setInstanceId("i-abc123");
        testNode.setInstanceType("t3.medium");
        testNode.setRegion(TEST_REGION);
        testNode.setAvailabilityZone("us-east-1a");
        testNode.setStatus(NodeStatus.RUNNING);
        testNode.setCreatedAt(Instant.now());

        // EC2 disabled by default in tests
        ReflectionTestUtils.setField(nodeService, "ec2Enabled", false);

        // Set default region for factory
        when(ec2ClientFactory.getDefaultRegion()).thenReturn(TEST_REGION);
    }

    @Nested
    @DisplayName("getAllNodes")
    class GetAllNodesTests {

        @Test
        @DisplayName("should return all nodes")
        void shouldReturnAllNodes() {
            when(nodeRepository.findAll()).thenReturn(List.of(testNode));

            List<NodeDTO> result = nodeService.getAllNodes();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("test-worker-001");
            verify(nodeRepository).findAll();
        }

        @Test
        @DisplayName("should return empty list when no nodes")
        void shouldReturnEmptyListWhenNoNodes() {
            when(nodeRepository.findAll()).thenReturn(List.of());

            List<NodeDTO> result = nodeService.getAllNodes();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getNodeById")
    class GetNodeByIdTests {

        @Test
        @DisplayName("should return node when found")
        void shouldReturnNodeWhenFound() {
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));

            NodeDTO result = nodeService.getNodeById(testNodeId);

            assertThat(result.getId()).isEqualTo(testNodeId);
            assertThat(result.getName()).isEqualTo("test-worker-001");
        }

        @Test
        @DisplayName("should throw NodeNotFoundException when not found")
        void shouldThrowWhenNotFound() {
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> nodeService.getNodeById(testNodeId))
                    .isInstanceOf(NodeNotFoundException.class)
                    .hasMessageContaining(testNodeId.toString());
        }
    }

    @Nested
    @DisplayName("createNode")
    class CreateNodeTests {

        @Test
        @DisplayName("should create node with provided values")
        void shouldCreateNodeWithProvidedValues() {
            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("new-worker");
            request.setInstanceType("t3.small");
            request.setRegion("ap-northeast-1");
            request.setAvailabilityZone("ap-northeast-1a");

            when(nodeRepository.save(any(Node.class))).thenAnswer(invocation -> {
                Node node = invocation.getArgument(0);
                node.setId(UUID.randomUUID());
                node.setCreatedAt(Instant.now());
                return node;
            });

            NodeDTO result = nodeService.createNode(request);

            assertThat(result.getName()).isEqualTo("new-worker");
            assertThat(result.getInstanceType()).isEqualTo("t3.small");
            assertThat(result.getRegion()).isEqualTo("ap-northeast-1");
            assertThat(result.getAvailabilityZone()).isEqualTo("ap-northeast-1a");
            assertThat(result.getStatus()).isEqualTo(NodeStatus.PENDING);
        }

        @Test
        @DisplayName("should use default region when not provided")
        void shouldUseDefaultRegion() {
            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("default-worker");

            when(nodeRepository.save(any(Node.class))).thenAnswer(invocation -> {
                Node node = invocation.getArgument(0);
                node.setId(UUID.randomUUID());
                node.setCreatedAt(Instant.now());
                return node;
            });

            NodeDTO result = nodeService.createNode(request);

            assertThat(result.getRegion()).isEqualTo(TEST_REGION);
            assertThat(result.getInstanceType()).isEqualTo("t3.medium");
            assertThat(result.getAvailabilityZone()).isEqualTo("us-east-1a");
        }

        @Test
        @DisplayName("should launch EC2 instance when EC2 is enabled")
        void shouldLaunchEc2InstanceWhenEnabled() {
            ReflectionTestUtils.setField(nodeService, "ec2Enabled", true);

            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("ec2-worker");
            request.setRegion("ap-northeast-1");

            when(ec2Service.launchInstance(eq("ec2-worker"), isNull(), isNull(), eq("ap-northeast-1")))
                    .thenReturn("i-newinstance");
            when(nodeRepository.save(any(Node.class))).thenAnswer(invocation -> {
                Node node = invocation.getArgument(0);
                node.setId(UUID.randomUUID());
                node.setCreatedAt(Instant.now());
                return node;
            });

            NodeDTO result = nodeService.createNode(request);

            assertThat(result.getInstanceId()).isEqualTo("i-newinstance");
            verify(ec2Service).launchInstance("ec2-worker", null, null, "ap-northeast-1");
        }
    }

    @Nested
    @DisplayName("updateNode")
    class UpdateNodeTests {

        @Test
        @DisplayName("should update node fields")
        void shouldUpdateNodeFields() {
            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("updated-worker");
            request.setInstanceType("t3.large");
            request.setRegion("eu-west-1");

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            NodeDTO result = nodeService.updateNode(testNodeId, request);

            assertThat(testNode.getName()).isEqualTo("updated-worker");
            assertThat(testNode.getInstanceType()).isEqualTo("t3.large");
            assertThat(testNode.getRegion()).isEqualTo("eu-west-1");
            verify(nodeRepository).save(testNode);
        }

        @Test
        @DisplayName("should throw when node not found")
        void shouldThrowWhenNodeNotFound() {
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.empty());

            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("updated");

            assertThatThrownBy(() -> nodeService.updateNode(testNodeId, request))
                    .isInstanceOf(NodeNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteNode")
    class DeleteNodeTests {

        @Test
        @DisplayName("should delete existing node")
        void shouldDeleteExistingNode() {
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            doNothing().when(nodeRepository).deleteById(testNodeId);

            nodeService.deleteNode(testNodeId);

            verify(nodeRepository).deleteById(testNodeId);
        }

        @Test
        @DisplayName("should terminate EC2 instance when enabled")
        void shouldTerminateEc2InstanceWhenEnabled() {
            ReflectionTestUtils.setField(nodeService, "ec2Enabled", true);

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            doNothing().when(nodeRepository).deleteById(testNodeId);

            nodeService.deleteNode(testNodeId);

            verify(ec2Service).terminateInstance("i-abc123", TEST_REGION);
            verify(nodeRepository).deleteById(testNodeId);
        }

        @Test
        @DisplayName("should throw when node not found")
        void shouldThrowWhenNodeNotFound() {
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> nodeService.deleteNode(testNodeId))
                    .isInstanceOf(NodeNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("startNode")
    class StartNodeTests {

        @Test
        @DisplayName("should start stopped node")
        void shouldStartStoppedNode() {
            testNode.setStatus(NodeStatus.STOPPED);
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            NodeDTO result = nodeService.startNode(testNodeId);

            assertThat(result.getStatus()).isEqualTo(NodeStatus.PENDING);
        }

        @Test
        @DisplayName("should start EC2 instance when enabled")
        void shouldStartEc2InstanceWhenEnabled() {
            ReflectionTestUtils.setField(nodeService, "ec2Enabled", true);
            testNode.setStatus(NodeStatus.STOPPED);

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            nodeService.startNode(testNodeId);

            verify(ec2Service).startInstance("i-abc123", TEST_REGION);
        }

        @Test
        @DisplayName("should throw when starting terminated node")
        void shouldThrowWhenStartingTerminatedNode() {
            testNode.setStatus(NodeStatus.TERMINATED);
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));

            assertThatThrownBy(() -> nodeService.startNode(testNodeId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("terminated");
        }
    }

    @Nested
    @DisplayName("stopNode")
    class StopNodeTests {

        @Test
        @DisplayName("should stop running node")
        void shouldStopRunningNode() {
            testNode.setStatus(NodeStatus.RUNNING);
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            NodeDTO result = nodeService.stopNode(testNodeId);

            assertThat(result.getStatus()).isEqualTo(NodeStatus.STOPPED);
        }

        @Test
        @DisplayName("should stop EC2 instance when enabled")
        void shouldStopEc2InstanceWhenEnabled() {
            ReflectionTestUtils.setField(nodeService, "ec2Enabled", true);
            testNode.setStatus(NodeStatus.RUNNING);

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            nodeService.stopNode(testNodeId);

            verify(ec2Service).stopInstance("i-abc123", TEST_REGION);
        }

        @Test
        @DisplayName("should throw when stopping terminated node")
        void shouldThrowWhenStoppingTerminatedNode() {
            testNode.setStatus(NodeStatus.TERMINATED);
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));

            assertThatThrownBy(() -> nodeService.stopNode(testNodeId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("terminated");
        }
    }

    @Nested
    @DisplayName("terminateNode")
    class TerminateNodeTests {

        @Test
        @DisplayName("should terminate node")
        void shouldTerminateNode() {
            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            NodeDTO result = nodeService.terminateNode(testNodeId);

            assertThat(result.getStatus()).isEqualTo(NodeStatus.TERMINATED);
        }

        @Test
        @DisplayName("should terminate EC2 instance when enabled")
        void shouldTerminateEc2InstanceWhenEnabled() {
            ReflectionTestUtils.setField(nodeService, "ec2Enabled", true);

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            nodeService.terminateNode(testNodeId);

            verify(ec2Service).terminateInstance("i-abc123", TEST_REGION);
        }
    }

    @Nested
    @DisplayName("countByStatus")
    class CountByStatusTests {

        @Test
        @DisplayName("should return count by status")
        void shouldReturnCountByStatus() {
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(5L);

            long count = nodeService.countByStatus(NodeStatus.RUNNING);

            assertThat(count).isEqualTo(5L);
        }
    }
}
