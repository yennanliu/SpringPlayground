package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.ClusterService;
import com.yen.FlinkRestService.model.Cluster;
import com.yen.FlinkRestService.model.dto.cluster.AddClusterDto;
import com.yen.FlinkRestService.model.dto.cluster.PingClusterDto;
import com.yen.FlinkRestService.model.dto.cluster.UpdateClusterDto;
import com.yen.FlinkRestService.model.response.ClusterPingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cluster")
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @GetMapping("/")
    public ResponseEntity<List<Cluster>> getClusters(){

        List<Cluster> clusters = clusterService.getClusters();
        return new ResponseEntity<>(clusters, HttpStatus.OK);
    }

    @GetMapping("/{clusterId}")
    public ResponseEntity<Cluster> getClusterById(@PathVariable("clusterId") Integer clusterId) {

        Cluster cluster = clusterService.getClusterById(clusterId);
        return new ResponseEntity<>(cluster, HttpStatus.OK);
    }

    @PostMapping("/add_cluster")
    public ResponseEntity<ApiResponse> addJobJar(@RequestBody AddClusterDto addClusterDto){

        clusterService.addCluster(addClusterDto);
        return new ResponseEntity<>(new ApiResponse(true, "Cluster has been added"), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateCluster(@RequestBody UpdateClusterDto updateClusterDto) {

        clusterService.updateCluster(updateClusterDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Cluster has been updated"), HttpStatus.OK);
    }
    
    @PostMapping("/ping")
    public ResponseEntity<ApiResponse> pingCluster(@RequestBody PingClusterDto pingClusterDto) {

        ClusterPingResponse resp = clusterService.pingCluster(Integer.parseInt(pingClusterDto.getId()));
        return new ResponseEntity<ApiResponse>(new ApiResponse(resp.getIsAccessible(), resp.getMessage()), HttpStatus.OK);
    }

}
