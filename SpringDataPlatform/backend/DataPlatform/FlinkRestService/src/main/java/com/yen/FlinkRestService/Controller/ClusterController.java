package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.ClusterService;
import com.yen.FlinkRestService.model.Cluster;
import com.yen.FlinkRestService.model.dto.cluster.AddClusterDto;
import com.yen.FlinkRestService.model.dto.cluster.PingClusterDto;
import com.yen.FlinkRestService.model.dto.cluster.UpdateClusterDto;
import com.yen.FlinkRestService.model.response.ClusterPingResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cluster")
@RequiredArgsConstructor
public class ClusterController {

    private final ClusterService clusterService;

    @GetMapping
    public ResponseEntity<List<Cluster>> getClusters() {
        List<Cluster> clusters = clusterService.getClusters();
        return ResponseEntity.ok(clusters);
    }

    @GetMapping("/{clusterId}")
    public ResponseEntity<Cluster> getClusterById(@PathVariable Integer clusterId) {
        Cluster cluster = clusterService.getClusterById(clusterId);
        return ResponseEntity.ok(cluster);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCluster(@Valid @RequestBody AddClusterDto addClusterDto) {
        clusterService.addCluster(addClusterDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Cluster has been added"));
    }

    @PutMapping("/{clusterId}")
    public ResponseEntity<ApiResponse> updateCluster(
            @PathVariable Integer clusterId,
            @Valid @RequestBody UpdateClusterDto updateClusterDto) {
        updateClusterDto.setId(clusterId);
        clusterService.updateCluster(updateClusterDto);
        return ResponseEntity.ok(new ApiResponse(true, "Cluster has been updated"));
    }

    @PostMapping("/{clusterId}/ping")
    public ResponseEntity<ApiResponse> pingCluster(@PathVariable Integer clusterId) {
        ClusterPingResponse resp = clusterService.pingCluster(clusterId);
        return ResponseEntity.ok(new ApiResponse(resp.getIsAccessible(), resp.getMessage()));
    }

    @DeleteMapping("/{clusterId}")
    public ResponseEntity<ApiResponse> deleteCluster(@PathVariable Integer clusterId) {
        clusterService.deleteCluster(clusterId);
        return ResponseEntity.ok(new ApiResponse(true, "Cluster has been deleted"));
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/")
    public ResponseEntity<List<Cluster>> getClustersLegacy() {
        return getClusters();
    }

    @PostMapping("/add_cluster")
    public ResponseEntity<ApiResponse> addClusterLegacy(@Valid @RequestBody AddClusterDto addClusterDto) {
        return addCluster(addClusterDto);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateClusterLegacy(@Valid @RequestBody UpdateClusterDto updateClusterDto) {
        clusterService.updateCluster(updateClusterDto);
        return ResponseEntity.ok(new ApiResponse(true, "Cluster has been updated"));
    }

    @PostMapping("/ping")
    public ResponseEntity<ApiResponse> pingClusterLegacy(@RequestBody PingClusterDto pingClusterDto) {
        ClusterPingResponse resp = clusterService.pingCluster(Integer.parseInt(pingClusterDto.getId()));
        return ResponseEntity.ok(new ApiResponse(resp.getIsAccessible(), resp.getMessage()));
    }
}
