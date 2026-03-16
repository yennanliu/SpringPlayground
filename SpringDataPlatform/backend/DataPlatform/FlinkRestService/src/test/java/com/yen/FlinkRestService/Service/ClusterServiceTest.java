package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.ClusterRepository;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.model.Cluster;
import com.yen.FlinkRestService.model.dto.cluster.AddClusterDto;
import com.yen.FlinkRestService.model.dto.cluster.UpdateClusterDto;
import com.yen.FlinkRestService.model.enums.ClusterStatus;
import com.yen.FlinkRestService.model.response.ClusterPingResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClusterServiceTest {

    @Mock
    private ClusterRepository clusterRepository;

    @Mock
    private RestTemplateService restTemplateService;

    private ClusterService clusterService;

    @BeforeEach
    void setUp() {
        clusterService = new ClusterService(clusterRepository, restTemplateService);
    }

    @Test
    void testGetClusters() {
        Cluster cluster1 = new Cluster(1, "url1", 9999, "status1");
        Cluster cluster2 = new Cluster(2, "url2", 8888, "status2");
        List<Cluster> clusters = Arrays.asList(cluster1, cluster2);

        when(clusterRepository.findAll()).thenReturn(clusters);

        List<Cluster> result = clusterService.getClusters();

        assertEquals(2, result.size());
        assertEquals(clusters, result);
        verify(clusterRepository, times(1)).findAll();
    }

    @Test
    void testGetClusterById_Found() {
        Cluster cluster = new Cluster();
        cluster.setId(1);
        cluster.setUrl("http://example.com");

        when(clusterRepository.findById(1)).thenReturn(Optional.of(cluster));

        Cluster result = clusterService.getClusterById(1);

        assertEquals(cluster, result);
        verify(clusterRepository, times(1)).findById(1);
    }

    @Test
    void testGetClusterById_NotFound() {
        when(clusterRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            clusterService.getClusterById(999);
        });

        verify(clusterRepository, times(1)).findById(999);
    }

    @Test
    void testAddCluster() {
        AddClusterDto addClusterDto = new AddClusterDto("http://example.com", 8080);

        Cluster savedCluster = new Cluster();
        savedCluster.setId(1);
        savedCluster.setUrl("http://example.com");
        savedCluster.setPort(8080);
        savedCluster.setStatus(ClusterStatus.ADDED.getValue());

        when(clusterRepository.save(any(Cluster.class))).thenReturn(savedCluster);

        Cluster result = clusterService.addCluster(addClusterDto);

        assertNotNull(result);
        assertEquals("http://example.com", result.getUrl());
        assertEquals(8080, result.getPort());
        verify(clusterRepository, times(1)).save(any(Cluster.class));
    }

    @Test
    void testUpdateCluster_Found() {
        UpdateClusterDto updateClusterDto = new UpdateClusterDto(1, "http://new-url.com", 9090);

        Cluster existingCluster = new Cluster();
        existingCluster.setId(1);
        existingCluster.setUrl("http://old-url.com");
        existingCluster.setPort(8080);

        when(clusterRepository.findById(1)).thenReturn(Optional.of(existingCluster));
        when(clusterRepository.save(any(Cluster.class))).thenReturn(existingCluster);

        Cluster result = clusterService.updateCluster(updateClusterDto);

        assertEquals("http://new-url.com", result.getUrl());
        assertEquals(9090, result.getPort());
        assertEquals(ClusterStatus.UPDATED.getValue(), result.getStatus());
        verify(clusterRepository, times(1)).findById(1);
        verify(clusterRepository, times(1)).save(any(Cluster.class));
    }

    @Test
    void testUpdateCluster_NotFound() {
        UpdateClusterDto updateClusterDto = new UpdateClusterDto(999, "http://new-url.com", 9090);

        when(clusterRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            clusterService.updateCluster(updateClusterDto);
        });

        verify(clusterRepository, times(1)).findById(999);
        verify(clusterRepository, never()).save(any(Cluster.class));
    }

    @Test
    void testPingCluster_Success() {
        Cluster cluster = new Cluster();
        cluster.setId(1);
        cluster.setUrl("http://example.com");
        cluster.setPort(8080);

        when(clusterRepository.findById(1)).thenReturn(Optional.of(cluster));

        ResponseEntity<String> responseEntity = new ResponseEntity<>("pong", HttpStatus.OK);
        when(restTemplateService.pingServer("http://example.com", 8080)).thenReturn(responseEntity);
        when(clusterRepository.save(any(Cluster.class))).thenReturn(cluster);

        ClusterPingResponse result = clusterService.pingCluster(1);

        assertEquals("pong", result.getMessage());
        assertTrue(result.getIsAccessible());
        assertEquals(ClusterStatus.CONNECTED.getValue(), cluster.getStatus());
        verify(clusterRepository, times(1)).save(any(Cluster.class));
    }

    @Test
    void testPingCluster_ConnectionFailed() {
        Cluster cluster = new Cluster();
        cluster.setId(1);
        cluster.setUrl("http://example.com");
        cluster.setPort(8080);

        when(clusterRepository.findById(1)).thenReturn(Optional.of(cluster));
        when(restTemplateService.pingServer("http://example.com", 8080)).thenReturn(null);
        when(clusterRepository.save(any(Cluster.class))).thenReturn(cluster);

        ClusterPingResponse result = clusterService.pingCluster(1);

        assertEquals("Connection failed", result.getMessage());
        assertFalse(result.getIsAccessible());
        assertEquals(ClusterStatus.NOT_CONNECTED.getValue(), cluster.getStatus());
        verify(clusterRepository, times(1)).save(any(Cluster.class));
    }

    @Test
    void testPingCluster_NotFound() {
        when(clusterRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            clusterService.pingCluster(999);
        });

        verify(clusterRepository, times(1)).findById(999);
        verify(restTemplateService, never()).pingServer(anyString(), anyInt());
    }

    @Test
    void testDeleteCluster_Found() {
        Cluster cluster = new Cluster();
        cluster.setId(1);

        when(clusterRepository.findById(1)).thenReturn(Optional.of(cluster));
        doNothing().when(clusterRepository).delete(cluster);

        assertDoesNotThrow(() -> clusterService.deleteCluster(1));

        verify(clusterRepository, times(1)).findById(1);
        verify(clusterRepository, times(1)).delete(cluster);
    }

    @Test
    void testDeleteCluster_NotFound() {
        when(clusterRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            clusterService.deleteCluster(999);
        });

        verify(clusterRepository, times(1)).findById(999);
        verify(clusterRepository, never()).delete(any(Cluster.class));
    }
}
