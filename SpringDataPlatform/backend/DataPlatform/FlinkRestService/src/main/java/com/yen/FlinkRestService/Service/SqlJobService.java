package com.yen.FlinkRestService.Service;

import com.alibaba.fastjson2.JSON;
import com.yen.FlinkRestService.Repository.SqlJobRepository;
import com.yen.FlinkRestService.model.SqlJob;
import com.yen.FlinkRestService.model.dto.SqlJobSubmitDto;
import com.yen.FlinkRestService.model.response.JobSubmitResponse;
import com.yen.FlinkRestService.model.response.SqlJobSubmitResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SqlJobService {

    @Value("${flink.sql_gateway.base_url}")
    private String BASE_URL;

    @Autowired
    private SqlJobRepository sqlJobRepository;

    @Autowired
    private RestTemplateService restTemplateService;

    public String submitSQLJob(SqlJobSubmitDto sqlJobSubmitDto){

        String url = BASE_URL + "/v1/sessions" ; // http://localhost:8083/v1/sessions
        log.info("url = " + url);

        ResponseEntity<String> responseEntity = restTemplateService.sendPostRequest(url, "", null);
        log.info(">>> responseEntity = " + responseEntity.toString());
        log.info(">>> responseEntity body = " + responseEntity.getBody());

        SqlJobSubmitResponse sqlJobSubmitResponse = JSON.parseObject(responseEntity.getBody(), SqlJobSubmitResponse.class);
        String sessionHandle = sqlJobSubmitResponse.getSessionHandle();
        log.info(">>> sessionHandle = " + sessionHandle);

        String jobSubmitUrl = url + "/" + sessionHandle + "/statements/"; //"http://localhost:8083/v1/sessions/${sessionHandle}/statements/"
        log.info("jobSubmitUrl = " + jobSubmitUrl);

        // TODO : get below from request payload
        //String sqlCMD = "{\"statement\": \"SELECT 1, 2, 3\"}";
        String sqlCMD = sqlJobSubmitDto.toString(); //sqlJobSubmitDto.getStatement();
        log.info("sqlCMD = " + sqlCMD);
        ResponseEntity<String> jobResponseEntity = restTemplateService.sendPostRequest(jobSubmitUrl, sqlCMD, null);
        SqlJobSubmitResponse sqlJobSubmitResponse2 = JSON.parseObject(jobResponseEntity.getBody(), SqlJobSubmitResponse.class);
        log.info(">>> OperationHandle = " + sqlJobSubmitResponse2.getOperationHandle());
        return null;
    }

}
