package com.yen.FlinkRestService.Common;

public class RestTemplateResponse<T> {

    private final boolean success;
    private final T response;

    public RestTemplateResponse(boolean success, T response) {
        this.success = success;
        this.response = response;
    }

}
