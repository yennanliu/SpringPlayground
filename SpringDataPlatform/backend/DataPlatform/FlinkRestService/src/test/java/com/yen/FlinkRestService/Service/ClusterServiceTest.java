package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.ClusterRepository;
import com.yen.FlinkRestService.model.Cluster;
import com.yen.FlinkRestService.model.dto.cluster.AddClusterDto;
import com.yen.FlinkRestService.model.dto.cluster.UpdateClusterDto;
import com.yen.FlinkRestService.model.response.ClusterPingResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClusterServiceTest {

    @Mock
    private ClusterRepository clusterRepository;

    @Mock
    private RestTemplateService restTemplateService;

    @InjectMocks
    private ClusterService clusterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClusters() {

        Cluster cluster1 =  new Cluster(1, "url1", 9999, "status1");
        Cluster cluster2 =  new Cluster(2, "url2", 8888, "status2");
        List<Cluster> clusters = Arrays.asList(cluster1, cluster2);

        // mock
        when(clusterRepository.findAll()).thenReturn(clusters);

        List<Cluster> result = clusterService.getClusters();

        assertEquals(2, result.size());
        assertEquals(result, clusters);
    }

    @Test
    void testGetClusterById() {

        Cluster cluster = new Cluster();
        cluster.setId(1);
        cluster.setUrl("http://example.com");

        // mock
        when(clusterRepository.findById(1)).thenReturn(Optional.of(cluster));

        Cluster result = clusterService.getClusterById(1);

        assertEquals(cluster, result);
    }

    @Test
    void testAddCluster() {

        AddClusterDto addClusterDto = new AddClusterDto("http://example.com", 8080);
        clusterService.addCluster(addClusterDto);

        verify(clusterRepository, times(1)).save(any());
    }

    @Test
    void testUpdateCluster() {

        UpdateClusterDto updateClusterDto = new UpdateClusterDto(1, "http://example.com", 8080);
        Cluster cluster = new Cluster();
        cluster.setId(1);
        cluster.setUrl("http://old-url.com");
        when(clusterRepository.findById(1)).thenReturn(Optional.of(cluster));

        Cluster result = clusterService.updateCluster(updateClusterDto);

        assertEquals("http://example.com", result.getUrl());
    }

    @Test
    void testPingCluster() {

        Cluster cluster = new Cluster();
        cluster.setId(1);
        cluster.setUrl("http://example.com");
        cluster.setPort(8080);
        
        // mock
        when(clusterRepository.findById(1)).thenReturn(Optional.of(cluster));
        ResponseEntity<String> responseEntity = new ResponseEntity<>("pong", HttpStatus.OK);
        when(restTemplateService.pingServer("http://example.com", 8080)).thenReturn(responseEntity);

        ClusterPingResponse result = clusterService.pingCluster(1);

        assertEquals("pong", result.getMessage());
        assertEquals(true, result.getIsAccessible());
    }

}
