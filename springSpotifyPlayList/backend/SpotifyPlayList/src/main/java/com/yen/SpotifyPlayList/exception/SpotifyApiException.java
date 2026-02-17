package com.yen.SpotifyPlayList.exception;

public class SpotifyApiException extends RuntimeException {
    private final int statusCode;
    private final String spotifyError;

    public SpotifyApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.spotifyError = null;
    }

    public SpotifyApiException(String message, int statusCode, String spotifyError) {
        super(message);
        this.statusCode = statusCode;
        this.spotifyError = spotifyError;
    }

    public SpotifyApiException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
        this.spotifyError = null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getSpotifyError() {
        return spotifyError;
    }
}