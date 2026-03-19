package com.yen.clusterAdmin.controller;

import com.yen.clusterAdmin.model.dto.ClusterHealthDTO;
import com.yen.clusterAdmin.model.dto.ClusterStatusDTO;
import com.yen.clusterAdmin.service.HealthMonitorService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClusterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HealthMonitorService healthMonitorService;

    @InjectMocks
    private ClusterController clusterController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clusterController).build();
    }

    @Nested
    @DisplayName("GET /api/v1/cluster/status")
    class GetClusterStatusTests {

        @Test
        @DisplayName("should return cluster status")
        void shouldReturnClusterStatus() throws Exception {
            ClusterStatusDTO status = ClusterStatusDTO.builder()
                    .totalNodes(10)
                    .runningNodes(8)
                    .pendingNodes(1)
                    .stoppedNodes(0)
                    .unhealthyNodes(1)
                    .terminatedNodes(0)
                    .averageCpuUsage(45.5)
                    .averageMemoryUsage(62.3)
                    .lastUpdated(Instant.now())
                    .build();

            when(healthMonitorService.getClusterStatus()).thenReturn(status);

            mockMvc.perform(get("/api/v1/cluster/status"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.totalNodes", is(10)))
                    .andExpect(jsonPath("$.runningNodes", is(8)))
                    .andExpect(jsonPath("$.unhealthyNodes", is(1)))
                    .andExpect(jsonPath("$.averageCpuUsage", is(45.5)));
        }

        @Test
        @DisplayName("should return empty cluster status")
        void shouldReturnEmptyClusterStatus() throws Exception {
            ClusterStatusDTO status = ClusterStatusDTO.builder()
                    .totalNodes(0)
                    .runningNodes(0)
                    .pendingNodes(0)
                    .stoppedNodes(0)
                    .unhealthyNodes(0)
                    .terminatedNodes(0)
                    .lastUpdated(Instant.now())
                    .build();

            when(healthMonitorService.getClusterStatus()).thenReturn(status);

            mockMvc.perform(get("/api/v1/cluster/status"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalNodes", is(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/cluster/health")
    class GetClusterHealthTests {

        @Test
        @DisplayName("should return healthy cluster")
        void shouldReturnHealthyCluster() throws Exception {
            ClusterHealthDTO health = ClusterHealthDTO.builder()
                    .status("HEALTHY")
                    .totalNodes(5)
                    .healthyNodes(5)
                    .unhealthyNodes(0)
                    .unhealthyNodesList(List.of())
                    .timestamp(Instant.now())
                    .build();

            when(healthMonitorService.getClusterHealth()).thenReturn(health);

            mockMvc.perform(get("/api/v1/cluster/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("HEALTHY")))
                    .andExpect(jsonPath("$.healthyNodes", is(5)))
                    .andExpect(jsonPath("$.unhealthyNodes", is(0)));
        }

        @Test
        @DisplayName("should return degraded cluster with unhealthy nodes")
        void shouldReturnDegradedCluster() throws Exception {
            ClusterHealthDTO.NodeHealthSummary unhealthyNode = new ClusterHealthDTO.NodeHealthSummary(
                    "uuid-123",
                    "unhealthy-worker",
                    "i-abc123",
                    Instant.now().minusSeconds(300),
                    3
            );

            ClusterHealthDTO health = ClusterHealthDTO.builder()
                    .status("DEGRADED")
                    .totalNodes(5)
                    .healthyNodes(4)
                    .unhealthyNodes(1)
                    .unhealthyNodesList(List.of(unhealthyNode))
                    .timestamp(Instant.now())
                    .build();

            when(healthMonitorService.getClusterHealth()).thenReturn(health);

            mockMvc.perform(get("/api/v1/cluster/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("DEGRADED")))
                    .andExpect(jsonPath("$.unhealthyNodesList[0].name", is("unhealthy-worker")))
                    .andExpect(jsonPath("$.unhealthyNodesList[0].failedChecks", is(3)));
        }

        @Test
        @DisplayName("should return empty cluster")
        void shouldReturnEmptyCluster() throws Exception {
            ClusterHealthDTO health = ClusterHealthDTO.builder()
                    .status("EMPTY")
                    .totalNodes(0)
                    .healthyNodes(0)
                    .unhealthyNodes(0)
                    .unhealthyNodesList(List.of())
                    .timestamp(Instant.now())
                    .build();

            when(healthMonitorService.getClusterHealth()).thenReturn(health);

            mockMvc.perform(get("/api/v1/cluster/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("EMPTY")));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/cluster/sync")
    class SyncAllNodesTests {

        @Test
        @DisplayName("should sync all nodes successfully")
        void shouldSyncAllNodesSuccessfully() throws Exception {
            doNothing().when(healthMonitorService).syncAllNodesWithEc2();

            mockMvc.perform(post("/api/v1/cluster/sync"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("Sync completed")));

            verify(healthMonitorService).syncAllNodesWithEc2();
        }
    }

    @Nested
    @DisplayName("POST /api/v1/cluster/health-check")
    class TriggerHealthChecksTests {

        @Test
        @DisplayName("should trigger health checks successfully")
        void shouldTriggerHealthChecksSuccessfully() throws Exception {
            doNothing().when(healthMonitorService).performHealthChecks();

            mockMvc.perform(post("/api/v1/cluster/health-check"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("Health checks completed")));

            verify(healthMonitorService).performHealthChecks();
        }
    }
}
