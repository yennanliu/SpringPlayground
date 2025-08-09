package com.yen.SpotifyPlayList.model.dto.Response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpotifyErrorResponse {
    private SpotifyError error;

    @Data
    @ToString
    public static class SpotifyError {
        private int status;
        private String message;
    }
}