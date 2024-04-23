package com.yen.FlinkRestService.util;

import com.yen.FlinkRestService.model.response.JarUploadResponse;
import org.junit.jupiter.api.Test;

class JarUtilTest {

    @Test
    public void TestGetJarName(){

        /**
         *   jobSubmitDto = JobSubmitDto(
         *   jarId=/var/folders/51/3j6n8rwd74g2tsjt5jhcy91w0000gn/T/flink-web-2837802e-a6d4-46f8-bc36-5f695312ec9c/flink-web-upload/ac26d385-88b2-4d0f-9697-b743d3a105dc_SessionWindowing.jar,
         *   entryClass=string,
         *   parallelism=0,
         *   programArgs=string,
         *   savePointPath=string,
         *   allowNonRestoredState=true
         *   )
         */
        JarUtil jarUtil =  new JarUtil();
        JarUploadResponse jarUploadResponse = new JarUploadResponse();
        String jarName = "/var/folders/tz/5r4lbzxj5hs5q87gwdwyjnph0000gn/T/flink-web-2f052123-bb4e-4d85-80f7-44f673625f02/flink-web-upload/31c74f38-9278-4c72-ab8a-c1bb2e47004c_TopSpeedWindowing.jar";
        jarUploadResponse.setFilename(jarName);

        System.out.println("jarUploadResponse = " + jarUploadResponse);
        System.out.println("response jar id = " + jarUtil.getJarNameFromRepsonse(jarUploadResponse));
    }

}