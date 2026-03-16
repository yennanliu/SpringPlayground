package com.yen.FlinkRestService.util;

import com.yen.FlinkRestService.model.response.JarUploadResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JarUtil {

    private JarUtil() {
        // Utility class - prevent instantiation
    }

    /**
     * Extracts JAR filename from Flink upload response.
     * Example input: "/var/folders/.../flink-web-upload/31c74f38-9278-4c72-ab8a-c1bb2e47004c_TopSpeedWindowing.jar"
     * Example output: "31c74f38-9278-4c72-ab8a-c1bb2e47004c_TopSpeedWindowing.jar"
     */
    public static String getJarNameFromResponse(JarUploadResponse jarUploadResponse) {
        if (jarUploadResponse == null || jarUploadResponse.getFilename() == null) {
            log.warn("Invalid JAR upload response - null filename");
            return null;
        }

        String fileName = jarUploadResponse.getFilename();

        // Extract filename after "flink-web-upload/"
        if (fileName.contains("/flink-web-upload/")) {
            String[] parts = fileName.split("/flink-web-upload/");
            return parts.length > 1 ? parts[parts.length - 1] : fileName;
        }

        // Fallback: extract just the filename
        int lastSlash = fileName.lastIndexOf('/');
        return lastSlash >= 0 ? fileName.substring(lastSlash + 1) : fileName;
    }

    /**
     * @deprecated Use {@link #getJarNameFromResponse(JarUploadResponse)} instead
     */
    @Deprecated
    public String getJarNameFromRepsonse(JarUploadResponse jarUploadResponse) {
        return getJarNameFromResponse(jarUploadResponse);
    }
}
