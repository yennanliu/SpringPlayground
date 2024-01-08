package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.ClusterRepository;
import com.yen.FlinkRestService.model.Cluster;
import com.yen.FlinkRestService.model.dto.AddClusterDto;
import com.yen.FlinkRestService.model.dto.UpdateClusterDto;
import com.yen.FlinkRestService.model.response.ClusterPingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private RestTemplateService restTemplateService;

    public List<Cluster> getClusters() {

        return clusterRepository.findAll();
    }

    public Cluster getClusterById(Integer clusterId) {

        if (clusterRepository.findById(clusterId).isPresent()){
            return clusterRepository.findById(clusterId).get();
        }
        log.warn("No Cluster with clusterId = " + clusterId);
        return null;
    }

    public void addCluster(AddClusterDto addClusterDto){

        Cluster cluster = new Cluster();
        cluster.setUrl(addClusterDto.getUrl());
        cluster.setPort(addClusterDto.getPort());
        cluster.setStatus("Added"); // TODO : replace with enums
        clusterRepository.save(cluster);
    }

    public Cluster updateCluster(UpdateClusterDto updateClusterDto){

        if (!clusterRepository.findById(updateClusterDto.getId()).isPresent()){
            log.warn("No saved cluster with id : " + updateClusterDto);
            return null;
        }

        Cluster curCluster = clusterRepository.findById(updateClusterDto.getId()).get();

        curCluster.setUrl(updateClusterDto.getUrl());
        curCluster.setPort(updateClusterDto.getPort());
        curCluster.setStatus("Updated"); // TODO : replace with enums
        clusterRepository.save(curCluster);
        return curCluster;
    }

    public ClusterPingResponse pingCluster(Integer clusterId){

        if (!clusterRepository.findById(clusterId).isPresent()){
            log.warn("No saved cluster with id : " + clusterId);
            return null;
        }

        Cluster cluster = clusterRepository.findById(clusterId).get();
        ResponseEntity<String> resp =  restTemplateService.pingServer(cluster.getUrl(), cluster.getPort());
        ClusterPingResponse clusterResp = new ClusterPingResponse();
        // if 2xx // TODO : optimize this
        if (StringUtils.startsWithIgnoreCase(resp.getStatusCode().toString(), "2")){
            clusterResp.setIsAccessible(true);
        }else{
            clusterResp.setIsAccessible(false);
        }
        clusterResp.setMessage(resp.getBody());
        return new ClusterPingResponse();
    }

}
