package com.yen.FlinkRestService.util;

import com.yen.FlinkRestService.model.response.JarUploadResponse;

public class JarUtil {

    // get 31c74f38-9278-4c72-ab8a-c1bb2e47004c_TopSpeedWindowing.jar from "/var/folders/tz/5r4lbzxj5hs5q87gwdwyjnph0000gn/T/flink-web-2f052123-bb4e-4d85-80f7-44f673625f02/flink-web-upload/31c74f38-9278-4c72-ab8a-c1bb2e47004c_TopSpeedWindowing.jar"
    public String getJarNameFromRepsonse(JarUploadResponse jarUploadResponse){
        try{
            String fileName = jarUploadResponse.getFilename();
            String[] list = fileName.split("/flink-web-upload");
            return list[list.length-1];
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
