package com.yen.clusterAdmin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.clusterAdmin.model.dto.HeartbeatRequest;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.repository.NodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WorkerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NodeRepository nodeRepository;

    @InjectMocks
    private WorkerController workerController;

    private ObjectMapper objectMapper;
    private Node testNode;
    private UUID testNodeId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(workerController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testNodeId = UUID.randomUUID();
        testNode = new Node();
        testNode.setId(testNodeId);
        testNode.setName("test-worker");
        testNode.setInstanceId("i-abc123");
        testNode.setStatus(NodeStatus.RUNNING);
        testNode.setFailedHealthChecks(0);
        testNode.setCreatedAt(Instant.now());
    }

    @Nested
    @DisplayName("POST /api/v1/worker/heartbeat")
    class HeartbeatTests {

        @Test
        @DisplayName("should acknowledge heartbeat by UUID")
        void shouldAcknowledgeHeartbeatByUuid() throws Exception {
            HeartbeatRequest request = new HeartbeatRequest();
            request.setNodeIdentifier(testNodeId.toString());
            request.setCpuUsage(45.0);
            request.setMemoryUsage(60.0);

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            mockMvc.perform(post("/api/v1/worker/heartbeat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("acknowledged")))
                    .andExpect(jsonPath("$.nodeName", is("test-worker")));

            verify(nodeRepository).save(any(Node.class));
        }

        @Test
        @DisplayName("should acknowledge heartbeat by instanceId")
        void shouldAcknowledgeHeartbeatByInstanceId() throws Exception {
            HeartbeatRequest request = new HeartbeatRequest();
            request.setNodeIdentifier("i-abc123");
            request.setCpuUsage(50.0);

            when(nodeRepository.findByInstanceId("i-abc123")).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            mockMvc.perform(post("/api/v1/worker/heartbeat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("acknowledged")));
        }

        @Test
        @DisplayName("should return 404 for unknown node")
        void shouldReturn404ForUnknownNode() throws Exception {
            HeartbeatRequest request = new HeartbeatRequest();
            request.setNodeIdentifier("i-unknown");

            when(nodeRepository.findByInstanceId("i-unknown")).thenReturn(Optional.empty());

            mockMvc.perform(post("/api/v1/worker/heartbeat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should recover unhealthy node")
        void shouldRecoverUnhealthyNode() throws Exception {
            testNode.setStatus(NodeStatus.UNHEALTHY);
            testNode.setFailedHealthChecks(3);

            HeartbeatRequest request = new HeartbeatRequest();
            request.setNodeIdentifier(testNodeId.toString());

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenAnswer(inv -> {
                Node n = inv.getArgument(0);
                return n;
            });

            mockMvc.perform(post("/api/v1/worker/heartbeat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(nodeRepository).save(argThat(node ->
                    node.getStatus() == NodeStatus.RUNNING &&
                            node.getFailedHealthChecks() == 0
            ));
        }

        @Test
        @DisplayName("should transition pending node to running")
        void shouldTransitionPendingNodeToRunning() throws Exception {
            testNode.setStatus(NodeStatus.PENDING);

            HeartbeatRequest request = new HeartbeatRequest();
            request.setNodeIdentifier(testNodeId.toString());

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenAnswer(inv -> inv.getArgument(0));

            mockMvc.perform(post("/api/v1/worker/heartbeat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(nodeRepository).save(argThat(node ->
                    node.getStatus() == NodeStatus.RUNNING
            ));
        }

        @Test
        @DisplayName("should update metrics from heartbeat")
        void shouldUpdateMetricsFromHeartbeat() throws Exception {
            HeartbeatRequest request = new HeartbeatRequest();
            request.setNodeIdentifier(testNodeId.toString());
            request.setCpuUsage(75.5);
            request.setMemoryUsage(82.3);

            when(nodeRepository.findById(testNodeId)).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenAnswer(inv -> inv.getArgument(0));

            mockMvc.perform(post("/api/v1/worker/heartbeat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(nodeRepository).save(argThat(node ->
                    node.getCpuUsage() == 75.5 &&
                            node.getMemoryUsage() == 82.3
            ));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/worker/register")
    class RegisterWorkerTests {

        @Test
        @DisplayName("should register new worker")
        void shouldRegisterNewWorker() throws Exception {
            Map<String, String> request = Map.of(
                    "instanceId", "i-newworker",
                    "name", "new-worker",
                    "privateIp", "10.0.1.100"
            );

            when(nodeRepository.findByInstanceId("i-newworker")).thenReturn(Optional.empty());
            when(nodeRepository.save(any(Node.class))).thenAnswer(inv -> {
                Node n = inv.getArgument(0);
                n.setId(UUID.randomUUID());
                return n;
            });

            mockMvc.perform(post("/api/v1/worker/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("registered")))
                    .andExpect(jsonPath("$.nodeName", is("new-worker")));

            verify(nodeRepository).save(argThat(node ->
                    node.getInstanceId().equals("i-newworker") &&
                            node.getName().equals("new-worker") &&
                            node.getStatus() == NodeStatus.RUNNING
            ));
        }

        @Test
        @DisplayName("should update existing worker")
        void shouldUpdateExistingWorker() throws Exception {
            Map<String, String> request = Map.of(
                    "instanceId", "i-abc123",
                    "privateIp", "10.0.1.200"
            );

            testNode.setStatus(NodeStatus.PENDING);
            when(nodeRepository.findByInstanceId("i-abc123")).thenReturn(Optional.of(testNode));
            when(nodeRepository.save(any(Node.class))).thenReturn(testNode);

            mockMvc.perform(post("/api/v1/worker/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("updated")));

            verify(nodeRepository).save(argThat(node ->
                    node.getPrivateIp().equals("10.0.1.200") &&
                            node.getStatus() == NodeStatus.RUNNING
            ));
        }

        @Test
        @DisplayName("should return 400 when instanceId missing")
        void shouldReturn400WhenInstanceIdMissing() throws Exception {
            Map<String, String> request = Map.of(
                    "name", "worker"
            );

            mockMvc.perform(post("/api/v1/worker/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("instanceId is required")));
        }

        @Test
        @DisplayName("should generate name when not provided")
        void shouldGenerateNameWhenNotProvided() throws Exception {
            Map<String, String> request = Map.of(
                    "instanceId", "i-12345678"
            );

            when(nodeRepository.findByInstanceId("i-12345678")).thenReturn(Optional.empty());
            when(nodeRepository.save(any(Node.class))).thenAnswer(inv -> {
                Node n = inv.getArgument(0);
                n.setId(UUID.randomUUID());
                return n;
            });

            mockMvc.perform(post("/api/v1/worker/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nodeName", is("worker-12345678")));
        }
    }
}
