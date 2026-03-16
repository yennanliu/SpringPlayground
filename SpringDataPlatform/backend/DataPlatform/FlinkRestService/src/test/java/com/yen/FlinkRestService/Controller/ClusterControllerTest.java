package com.yen.FlinkRestService.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.FlinkRestService.Service.ClusterService;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.exception.GlobalExceptionHandler;
import com.yen.FlinkRestService.model.Cluster;
import com.yen.FlinkRestService.model.dto.cluster.AddClusterDto;
import com.yen.FlinkRestService.model.dto.cluster.UpdateClusterDto;
import com.yen.FlinkRestService.model.enums.ClusterStatus;
import com.yen.FlinkRestService.model.response.ClusterPingResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClusterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClusterService clusterService;

    @InjectMocks
    private ClusterController clusterController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clusterController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetClusters() throws Exception {
        Cluster cluster1 = createCluster(1, "http://localhost", 8081);
        Cluster cluster2 = createCluster(2, "http://localhost", 8082);
        List<Cluster> clusters = Arrays.asList(cluster1, cluster2);

        when(clusterService.getClusters()).thenReturn(clusters);

        mockMvc.perform(get("/cluster"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].url", is("http://localhost")))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(clusterService, times(1)).getClusters();
    }

    @Test
    void testGetClusterById_Found() throws Exception {
        Cluster cluster = createCluster(1, "http://localhost", 8081);

        when(clusterService.getClusterById(1)).thenReturn(cluster);

        mockMvc.perform(get("/cluster/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.url", is("http://localhost")))
                .andExpect(jsonPath("$.port", is(8081)));

        verify(clusterService, times(1)).getClusterById(1);
    }

    @Test
    void testGetClusterById_NotFound() throws Exception {
        when(clusterService.getClusterById(999))
                .thenThrow(new EntityNotFoundException("Cluster", 999));

        mockMvc.perform(get("/cluster/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        verify(clusterService, times(1)).getClusterById(999);
    }

    @Test
    void testAddCluster() throws Exception {
        AddClusterDto dto = new AddClusterDto("http://localhost", 8081);
        Cluster savedCluster = createCluster(1, "http://localhost", 8081);

        when(clusterService.addCluster(any(AddClusterDto.class))).thenReturn(savedCluster);

        mockMvc.perform(post("/cluster")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("added")));

        verify(clusterService, times(1)).addCluster(any(AddClusterDto.class));
    }

    @Test
    void testAddCluster_ValidationError() throws Exception {
        AddClusterDto dto = new AddClusterDto("", null); // Invalid

        mockMvc.perform(post("/cluster")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCluster() throws Exception {
        UpdateClusterDto dto = new UpdateClusterDto(1, "http://updated", 9090);
        Cluster updatedCluster = createCluster(1, "http://updated", 9090);

        when(clusterService.updateCluster(any(UpdateClusterDto.class))).thenReturn(updatedCluster);

        mockMvc.perform(put("/cluster/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("updated")));

        verify(clusterService, times(1)).updateCluster(any(UpdateClusterDto.class));
    }

    @Test
    void testPingCluster_Success() throws Exception {
        ClusterPingResponse response = new ClusterPingResponse();
        response.setIsAccessible(true);
        response.setMessage("pong");

        when(clusterService.pingCluster(1)).thenReturn(response);

        mockMvc.perform(post("/cluster/1/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("pong")));

        verify(clusterService, times(1)).pingCluster(1);
    }

    @Test
    void testPingCluster_Failed() throws Exception {
        ClusterPingResponse response = new ClusterPingResponse();
        response.setIsAccessible(false);
        response.setMessage("Connection failed");

        when(clusterService.pingCluster(1)).thenReturn(response);

        mockMvc.perform(post("/cluster/1/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("failed")));

        verify(clusterService, times(1)).pingCluster(1);
    }

    @Test
    void testDeleteCluster() throws Exception {
        doNothing().when(clusterService).deleteCluster(1);

        mockMvc.perform(delete("/cluster/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("deleted")));

        verify(clusterService, times(1)).deleteCluster(1);
    }

    private Cluster createCluster(Integer id, String url, Integer port) {
        Cluster cluster = new Cluster();
        cluster.setId(id);
        cluster.setUrl(url);
        cluster.setPort(port);
        cluster.setStatus(ClusterStatus.CONNECTED.getValue());
        return cluster;
    }
}
