package com.yen.FlinkRestService.util;

import com.yen.FlinkRestService.model.response.JarUploadResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JarUtilTest {

    @Test
    void testGetJarNameFromResponse_WithFlinkWebUploadPath() {
        JarUploadResponse response = new JarUploadResponse();
        response.setFilename("/var/folders/tz/5r4lbzxj5hs5q87gwdwyjnph0000gn/T/flink-web-2f052123-bb4e-4d85-80f7-44f673625f02/flink-web-upload/31c74f38-9278-4c72-ab8a-c1bb2e47004c_TopSpeedWindowing.jar");

        String result = JarUtil.getJarNameFromResponse(response);

        assertEquals("31c74f38-9278-4c72-ab8a-c1bb2e47004c_TopSpeedWindowing.jar", result);
    }

    @Test
    void testGetJarNameFromResponse_WithSimplePath() {
        JarUploadResponse response = new JarUploadResponse();
        response.setFilename("/some/path/myjar.jar");

        String result = JarUtil.getJarNameFromResponse(response);

        assertEquals("myjar.jar", result);
    }

    @Test
    void testGetJarNameFromResponse_WithNoPath() {
        JarUploadResponse response = new JarUploadResponse();
        response.setFilename("simple.jar");

        String result = JarUtil.getJarNameFromResponse(response);

        assertEquals("simple.jar", result);
    }

    @Test
    void testGetJarNameFromResponse_NullResponse() {
        String result = JarUtil.getJarNameFromResponse(null);

        assertNull(result);
    }

    @Test
    void testGetJarNameFromResponse_NullFilename() {
        JarUploadResponse response = new JarUploadResponse();
        response.setFilename(null);

        String result = JarUtil.getJarNameFromResponse(response);

        assertNull(result);
    }
}
