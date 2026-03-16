package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.ClusterRepository;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.model.Cluster;
import com.yen.FlinkRestService.model.dto.cluster.AddClusterDto;
import com.yen.FlinkRestService.model.dto.cluster.UpdateClusterDto;
import com.yen.FlinkRestService.model.enums.ClusterStatus;
import com.yen.FlinkRestService.model.response.ClusterPingResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterService {

    private final ClusterRepository clusterRepository;
    private final RestTemplateService restTemplateService;

    @Transactional(readOnly = true)
    public List<Cluster> getClusters() {
        return clusterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cluster getClusterById(Integer clusterId) {
        return clusterRepository.findById(clusterId)
                .orElseThrow(() -> new EntityNotFoundException("Cluster", clusterId));
    }

    @Transactional
    public Cluster addCluster(AddClusterDto addClusterDto) {
        Cluster cluster = new Cluster();
        cluster.setUrl(addClusterDto.getUrl());
        cluster.setPort(addClusterDto.getPort());
        cluster.setStatus(ClusterStatus.ADDED.getValue());

        Cluster savedCluster = clusterRepository.save(cluster);
        log.info("Cluster added successfully, id={}", savedCluster.getId());
        return savedCluster;
    }

    @Transactional
    public Cluster updateCluster(UpdateClusterDto updateClusterDto) {
        Cluster cluster = clusterRepository.findById(updateClusterDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cluster", updateClusterDto.getId()));

        cluster.setUrl(updateClusterDto.getUrl());
        cluster.setPort(updateClusterDto.getPort());
        cluster.setStatus(ClusterStatus.UPDATED.getValue());

        Cluster updatedCluster = clusterRepository.save(cluster);
        log.info("Cluster updated successfully, id={}", updatedCluster.getId());
        return updatedCluster;
    }

    @Transactional
    public ClusterPingResponse pingCluster(Integer clusterId) {
        Cluster cluster = clusterRepository.findById(clusterId)
                .orElseThrow(() -> new EntityNotFoundException("Cluster", clusterId));

        ClusterPingResponse response = new ClusterPingResponse();

        ResponseEntity<String> resp = restTemplateService.pingServer(cluster.getUrl(), cluster.getPort());

        if (resp == null || resp.getBody() == null) {
            cluster.setStatus(ClusterStatus.NOT_CONNECTED.getValue());
            response.setMessage("Connection failed");
            response.setIsAccessible(false);
            clusterRepository.save(cluster);
            log.warn("Ping failed for cluster id={}", clusterId);
            return response;
        }

        log.info("Ping response status={} for cluster id={}", resp.getStatusCode(), clusterId);

        if (resp.getStatusCode().is2xxSuccessful()) {
            response.setIsAccessible(true);
            cluster.setStatus(ClusterStatus.CONNECTED.getValue());
        } else {
            response.setIsAccessible(false);
            cluster.setStatus(ClusterStatus.NOT_CONNECTED.getValue());
        }

        response.setMessage(resp.getBody());
        clusterRepository.save(cluster);
        return response;
    }

    @Transactional
    public void deleteCluster(Integer clusterId) {
        Cluster cluster = clusterRepository.findById(clusterId)
                .orElseThrow(() -> new EntityNotFoundException("Cluster", clusterId));

        clusterRepository.delete(cluster);
        log.info("Cluster deleted successfully, id={}", clusterId);
    }
}
