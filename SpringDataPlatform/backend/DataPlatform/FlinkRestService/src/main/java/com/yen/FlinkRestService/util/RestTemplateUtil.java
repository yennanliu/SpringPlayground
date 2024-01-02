package com.yen.FlinkRestService.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

// https://github.com/thestyleofme/flink-api-spring-boot-starter/blob/master/src/main/java/com/github/codingdebugallday/client/infra/utils/RestTemplateUtil.java

public class RestTemplateUtil {

    private RestTemplateUtil() {
        throw new IllegalStateException("util class");
    }

    /**
     * 封装请求头
     *
     * @return org.springframework.http.HttpHeaders
     */
    public static HttpHeaders applicationJsonHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    /**
     * 封装请求头
     *
     * @return org.springframework.http.HttpHeaders
     */
    public static HttpHeaders applicationMultiDataHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        return httpHeaders;
    }

}
