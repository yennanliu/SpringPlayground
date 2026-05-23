package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.Response.LegacyRecommendationsResponse;
import com.yen.SpotifyPlayList.model.dto.Response.SpotifyRecommendationsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RecommendationsValidator {

    public ValidationResult validateSpotifyResponse(SpotifyRecommendationsResponse response) {
        List<String> issues = new ArrayList<>();

        if (response == null) {
            issues.add("Response is null");
            return new ValidationResult(false, issues);
        }

        // Validate tracks
        if (response.getTracks() == null) {
            issues.add("Tracks array is null");
        } else {
            for (int i = 0; i < response.getTracks().size(); i++) {
                SpotifyRecommendationsResponse.SpotifyTrack track = response.getTracks().get(i);
                validateTrack(track, i, issues);
            }
        }

        // Validate seeds
        if (response.getSeeds() != null) {
            for (int i = 0; i < response.getSeeds().size(); i++) {
                SpotifyRecommendationsResponse.SpotifySeed seed = response.getSeeds().get(i);
                validateSeed(seed, i, issues);
            }
        }

        boolean isValid = issues.isEmpty();
        if (!isValid) {
            log.warn("Spotify response validation failed: {}", issues);
        }

        return new ValidationResult(isValid, issues);
    }

    public ValidationResult validateLegacyResponse(LegacyRecommendationsResponse response) {
        List<String> issues = new ArrayList<>();

        if (response == null) {
            issues.add("Response is null");
            return new ValidationResult(false, issues);
        }

        // Validate tracks
        if (response.getTracks() == null) {
            issues.add("Tracks array is null");
        } else {
            for (int i = 0; i < response.getTracks().size(); i++) {
                LegacyRecommendationsResponse.LegacyTrack track = response.getTracks().get(i);
                validateLegacyTrack(track, i, issues);
            }
        }

        boolean isValid = issues.isEmpty();
        if (!isValid) {
            log.warn("Legacy response validation failed: {}", issues);
        }

        return new ValidationResult(isValid, issues);
    }

    private void validateTrack(SpotifyRecommendationsResponse.SpotifyTrack track, int index, List<String> issues) {
        if (track == null) {
            issues.add("Track at index " + index + " is null");
            return;
        }

        if (track.getId() == null || track.getId().trim().isEmpty()) {
            issues.add("Track at index " + index + " has missing or empty ID");
        }

        if (track.getName() == null || track.getName().trim().isEmpty()) {
            issues.add("Track at index " + index + " has missing or empty name");
        }

        if (track.getUri() == null || !track.getUri().startsWith("spotify:track:")) {
            issues.add("Track at index " + index + " has invalid URI format");
        }

        // Validate album if present
        if (track.getAlbum() != null) {
            validateAlbum(track.getAlbum(), index, issues);
        }

        // Validate artists if present
        if (track.getArtists() != null && track.getArtists().isEmpty()) {
            issues.add("Track at index " + index + " has empty artists array");
        }
    }

    private void validateAlbum(SpotifyRecommendationsResponse.SpotifyAlbum album, int trackIndex, List<String> issues) {
        if (album.getId() == null || album.getId().trim().isEmpty()) {
            issues.add("Album for track at index " + trackIndex + " has missing or empty ID");
        }

        if (album.getName() == null || album.getName().trim().isEmpty()) {
            issues.add("Album for track at index " + trackIndex + " has missing or empty name");
        }
    }

    private void validateSeed(SpotifyRecommendationsResponse.SpotifySeed seed, int index, List<String> issues) {
        if (seed == null) {
            issues.add("Seed at index " + index + " is null");
            return;
        }

        if (seed.getId() == null || seed.getId().trim().isEmpty()) {
            issues.add("Seed at index " + index + " has missing or empty ID");
        }

        if (seed.getType() == null || seed.getType().trim().isEmpty()) {
            issues.add("Seed at index " + index + " has missing or empty type");
        } else {
            String type = seed.getType().toLowerCase();
            if (!type.equals("artist") && !type.equals("track") && !type.equals("genre")) {
                issues.add("Seed at index " + index + " has invalid type: " + seed.getType());
            }
        }
    }

    private void validateLegacyTrack(LegacyRecommendationsResponse.LegacyTrack track, int index, List<String> issues) {
        if (track == null) {
            issues.add("Legacy track at index " + index + " is null");
            return;
        }

        if (track.getId() == null || track.getId().trim().isEmpty()) {
            issues.add("Legacy track at index " + index + " has missing or empty ID");
        }

        if (track.getName() == null || track.getName().trim().isEmpty()) {
            issues.add("Legacy track at index " + index + " has missing or empty name");
        }

        // Validate external URLs structure for frontend compatibility
        if (track.getExternalUrls() != null) {
            if (track.getExternalUrls().getExternalUrls() == null) {
                issues.add("Legacy track at index " + index + " has missing nested externalUrls.externalUrls structure");
            } else if (track.getExternalUrls().getExternalUrls().getSpotify() == null) {
                issues.add("Legacy track at index " + index + " has missing Spotify URL in nested structure");
            }
        }
    }

    public static class ValidationResult {
        private final boolean valid;
        private final List<String> issues;

        public ValidationResult(boolean valid, List<String> issues) {
            this.valid = valid;
            this.issues = new ArrayList<>(issues);
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getIssues() {
            return new ArrayList<>(issues);
        }

        public String getIssuesAsString() {
            return String.join("; ", issues);
        }
    }
}