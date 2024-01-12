package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Service.ZeppelinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zeppelin")
public class ZeppelinController {

    @Autowired
    private ZeppelinService zeppelinService;


    /**
     *
     *     @PostMapping("/ping")
     *     public ResponseEntity<ApiResponse> pingCluster(@RequestBody PingClusterDto pingClusterDto) {
     *
     *         ClusterPingResponse resp = clusterService.pingCluster(pingClusterDto.getId());
     *         return new ResponseEntity<ApiResponse>(new ApiResponse(resp.getIsAccessible(), resp.getMessage()), HttpStatus.OK);
     *     }
     */

    @PostMapping("/create")
    public String createNotebook(@RequestBody String notePath){

        String res = zeppelinService.createNote(notePath);
        return res;
    }

}
