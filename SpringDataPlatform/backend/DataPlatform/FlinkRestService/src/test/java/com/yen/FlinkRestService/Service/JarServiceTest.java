package com.yen.FlinkRestService.Service;

import org.apache.flink.api.common.JobID;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JarServiceTest {

    @Test
    public void test(){
        System.out.println(123);

        String str = "PANKAJ";
        byte[] byteArr = str.getBytes();
        // print the byte[] elements
        System.out.println("String to byte array: " + Arrays.toString(byteArr));
    }

    // https://mvnrepository.com/artifact/org.apache.flink/flink-clients/1.17.2
    // https://blog.51cto.com/u_16175517/8075303
    @Test
    public void testFlinkClient() throws Exception {

        String restURL = "localhost";

        Configuration config = new Configuration();
        config.setString("rest.address", restURL);
        //new org.apache.flink.configuration();
        RestClusterClient client = new RestClusterClient(config, restURL);


        // string -> byte[]
        // byte[] byteArrray = inputString.getBytes("IBM01140");
        // https://www.baeldung.com/java-string-to-byte-array
        String flinkJobId = "36f62fd9d9cff27eb51b6325a1d0b86d";
        byte[] byteFlinkJobId = flinkJobId.getBytes(); //flinkJobId.getBytes(StandardCharsets.UTF_8); //flinkJobId.getBytes(); //Base64.decodeBase64(flinkJobId); //flinkJobId.getBytes();
        System.out.println("String to byte array: " + Arrays.toString(byteFlinkJobId));
        //System.out.println("byteFlinkJobId = " + byteFlinkJobId);
        //JobID jobId = new JobID(byteFlinkJobId);
        //client.get();

        System.out.println("client = " + client.toString());
        System.out.println("ClusterId = " + client.getClusterId());
        //System.out.println("JobStatus = " + client.getJobStatus(jobId));

        JobGraph jobGraph = new JobGraph("Flink Streaming Job");
        client.submitJob(jobGraph);
    }

}