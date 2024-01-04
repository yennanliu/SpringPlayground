package com.yen.FlinkRestService.util;

import com.yen.FlinkRestService.model.response.JarUploadResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JarUtilTest {

    @Test
    public void TestGetJarName(){

        JarUtil jarUtil =  new JarUtil();
        JarUploadResponse jarUploadResponse = new JarUploadResponse();
        jarUploadResponse.setFilename("/var/folders/tz/5r4lbzxj5hs5q87gwdwyjnph0000gn/T/flink-web-2f052123-bb4e-4d85-80f7-44f673625f02/flink-web-upload/31c74f38-9278-4c72-ab8a-c1bb2e47004c_TopSpeedWindowing.jar");

        System.out.println(jarUploadResponse.toString());

        System.out.println(jarUtil.getJarNameFromRepsonse(jarUploadResponse));
    }

}