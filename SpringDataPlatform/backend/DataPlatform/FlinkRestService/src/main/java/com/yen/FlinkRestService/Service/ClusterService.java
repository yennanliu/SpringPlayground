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

        ClusterPingResponse clusterPingResponse = new ClusterPingResponse();

        if (!clusterRepository.findById(clusterId).isPresent()){
            String msg = "failed, No saved cluster with id : " + clusterId;
            log.warn(msg);
            clusterPingResponse.setMessage(msg);
            return clusterPingResponse;
        }

        Cluster cluster = clusterRepository.findById(clusterId).get();
        ResponseEntity<String> resp =  restTemplateService.pingServer(cluster.getUrl(), cluster.getPort());

        // if null resp (restTemplate can't access cluster)
        if (resp == null || resp.toString().equals(null)){
            cluster.setStatus("not_connected");
            clusterPingResponse.setMessage("failed");
            clusterPingResponse.setIsAccessible(false);
            // save to DB
            clusterRepository.save(cluster);
            return clusterPingResponse;
        }

        log.info("(pingCluster) resp status = " + resp.getStatusCode());
        clusterPingResponse = new ClusterPingResponse();

        // if 2xx // TODO : optimize this
        if (StringUtils.startsWithIgnoreCase(resp.getStatusCode().toString(), "2")){
            clusterPingResponse.setIsAccessible(true);
            cluster.setStatus("connected");
        }else{
            clusterPingResponse.setIsAccessible(false);
            cluster.setStatus("not_connected");
        }
        clusterPingResponse.setMessage(resp.getBody());
        // save to DB
        clusterRepository.save(cluster);
        return clusterPingResponse;
    }

}
