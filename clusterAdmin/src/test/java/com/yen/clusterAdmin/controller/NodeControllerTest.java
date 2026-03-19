package com.yen.clusterAdmin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.clusterAdmin.exception.GlobalExceptionHandler;
import com.yen.clusterAdmin.exception.NodeNotFoundException;
import com.yen.clusterAdmin.model.dto.NodeCreateRequest;
import com.yen.clusterAdmin.model.dto.NodeDTO;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.service.NodeService;
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
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NodeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NodeService nodeService;

    @InjectMocks
    private NodeController nodeController;

    private ObjectMapper objectMapper;
    private NodeDTO testNodeDTO;
    private UUID testNodeId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(nodeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testNodeId = UUID.randomUUID();
        testNodeDTO = new NodeDTO();
        testNodeDTO.setId(testNodeId);
        testNodeDTO.setName("test-worker");
        testNodeDTO.setInstanceId("i-abc123");
        testNodeDTO.setInstanceType("t3.medium");
        testNodeDTO.setStatus(NodeStatus.RUNNING);
        testNodeDTO.setCreatedAt(Instant.now());
    }

    @Nested
    @DisplayName("GET /api/v1/nodes")
    class GetAllNodesTests {

        @Test
        @DisplayName("should return all nodes")
        void shouldReturnAllNodes() throws Exception {
            when(nodeService.getAllNodes()).thenReturn(List.of(testNodeDTO));

            mockMvc.perform(get("/api/v1/nodes"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is("test-worker")));
        }

        @Test
        @DisplayName("should filter by status")
        void shouldFilterByStatus() throws Exception {
            when(nodeService.getNodesByStatus(NodeStatus.RUNNING)).thenReturn(List.of(testNodeDTO));

            mockMvc.perform(get("/api/v1/nodes")
                            .param("status", "RUNNING"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(nodeService).getNodesByStatus(NodeStatus.RUNNING);
        }

        @Test
        @DisplayName("should return empty list when no nodes")
        void shouldReturnEmptyList() throws Exception {
            when(nodeService.getAllNodes()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/nodes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/nodes/{id}")
    class GetNodeByIdTests {

        @Test
        @DisplayName("should return node when found")
        void shouldReturnNodeWhenFound() throws Exception {
            when(nodeService.getNodeById(testNodeId)).thenReturn(testNodeDTO);

            mockMvc.perform(get("/api/v1/nodes/{id}", testNodeId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(testNodeId.toString())))
                    .andExpect(jsonPath("$.name", is("test-worker")));
        }

        @Test
        @DisplayName("should return 404 when not found")
        void shouldReturn404WhenNotFound() throws Exception {
            when(nodeService.getNodeById(testNodeId))
                    .thenThrow(new NodeNotFoundException(testNodeId));

            mockMvc.perform(get("/api/v1/nodes/{id}", testNodeId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is("Not Found")));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/nodes")
    class CreateNodeTests {

        @Test
        @DisplayName("should create node successfully")
        void shouldCreateNodeSuccessfully() throws Exception {
            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("new-worker");
            request.setInstanceType("t3.small");

            testNodeDTO.setName("new-worker");
            testNodeDTO.setStatus(NodeStatus.PENDING);

            when(nodeService.createNode(any(NodeCreateRequest.class))).thenReturn(testNodeDTO);

            mockMvc.perform(post("/api/v1/nodes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is("new-worker")));
        }

        @Test
        @DisplayName("should return 400 when name is blank")
        void shouldReturn400WhenNameBlank() throws Exception {
            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("");

            mockMvc.perform(post("/api/v1/nodes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/nodes/{id}")
    class UpdateNodeTests {

        @Test
        @DisplayName("should update node successfully")
        void shouldUpdateNodeSuccessfully() throws Exception {
            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("updated-worker");

            testNodeDTO.setName("updated-worker");

            when(nodeService.updateNode(eq(testNodeId), any(NodeCreateRequest.class)))
                    .thenReturn(testNodeDTO);

            mockMvc.perform(put("/api/v1/nodes/{id}", testNodeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("updated-worker")));
        }

        @Test
        @DisplayName("should return 404 when node not found")
        void shouldReturn404WhenNodeNotFound() throws Exception {
            NodeCreateRequest request = new NodeCreateRequest();
            request.setName("updated");

            when(nodeService.updateNode(eq(testNodeId), any(NodeCreateRequest.class)))
                    .thenThrow(new NodeNotFoundException(testNodeId));

            mockMvc.perform(put("/api/v1/nodes/{id}", testNodeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/nodes/{id}")
    class DeleteNodeTests {

        @Test
        @DisplayName("should delete node successfully")
        void shouldDeleteNodeSuccessfully() throws Exception {
            doNothing().when(nodeService).deleteNode(testNodeId);

            mockMvc.perform(delete("/api/v1/nodes/{id}", testNodeId))
                    .andExpect(status().isNoContent());

            verify(nodeService).deleteNode(testNodeId);
        }

        @Test
        @DisplayName("should return 404 when node not found")
        void shouldReturn404WhenNodeNotFound() throws Exception {
            doThrow(new NodeNotFoundException(testNodeId)).when(nodeService).deleteNode(testNodeId);

            mockMvc.perform(delete("/api/v1/nodes/{id}", testNodeId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/nodes/{id}/start")
    class StartNodeTests {

        @Test
        @DisplayName("should start node successfully")
        void shouldStartNodeSuccessfully() throws Exception {
            testNodeDTO.setStatus(NodeStatus.PENDING);
            when(nodeService.startNode(testNodeId)).thenReturn(testNodeDTO);

            mockMvc.perform(post("/api/v1/nodes/{id}/start", testNodeId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("PENDING")));
        }

        @Test
        @DisplayName("should return 400 when starting terminated node")
        void shouldReturn400WhenStartingTerminatedNode() throws Exception {
            when(nodeService.startNode(testNodeId))
                    .thenThrow(new IllegalStateException("Cannot start a terminated node"));

            mockMvc.perform(post("/api/v1/nodes/{id}/start", testNodeId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("terminated")));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/nodes/{id}/stop")
    class StopNodeTests {

        @Test
        @DisplayName("should stop node successfully")
        void shouldStopNodeSuccessfully() throws Exception {
            testNodeDTO.setStatus(NodeStatus.STOPPED);
            when(nodeService.stopNode(testNodeId)).thenReturn(testNodeDTO);

            mockMvc.perform(post("/api/v1/nodes/{id}/stop", testNodeId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("STOPPED")));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/nodes/{id}/terminate")
    class TerminateNodeTests {

        @Test
        @DisplayName("should terminate node successfully")
        void shouldTerminateNodeSuccessfully() throws Exception {
            testNodeDTO.setStatus(NodeStatus.TERMINATED);
            when(nodeService.terminateNode(testNodeId)).thenReturn(testNodeDTO);

            mockMvc.perform(post("/api/v1/nodes/{id}/terminate", testNodeId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("TERMINATED")));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/nodes/{id}/sync")
    class SyncNodeTests {

        @Test
        @DisplayName("should sync node successfully")
        void shouldSyncNodeSuccessfully() throws Exception {
            when(nodeService.syncNode(testNodeId)).thenReturn(testNodeDTO);

            mockMvc.perform(post("/api/v1/nodes/{id}/sync", testNodeId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(testNodeId.toString())));
        }
    }
}
