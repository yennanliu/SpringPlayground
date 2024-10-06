package com.yen.SpotifyPlayList.service;

import com.yen.SpotifyPlayList.model.dto.GetRecommendationsDto;
import com.yen.SpotifyPlayList.model.dto.GetRecommendationsWithFeatureDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecommendationsService {

    @Autowired
    private AuthService authService;

    @Autowired
    private PlayListService playListService;

    private SpotifyApi spotifyApi;

    public RecommendationsService() {

    }

    public Recommendations getRecommendation(GetRecommendationsDto getRecommendationsDto) throws SpotifyWebApiException {

        Recommendations recommendations = null;
        try {
            this.spotifyApi = authService.initializeSpotifyApi();
            GetRecommendationsRequest getRecommendationsRequest = prepareRecommendationsRequest(getRecommendationsDto);
            recommendations = getRecommendationsRequest.execute();
            log.info("recommendations = " + recommendations);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SpotifyWebApiException("getRecommendation error: " + e.getMessage());
        }
        return recommendations;
    }

    public Recommendations getRecommendationWithPlayList(String playListId) throws SpotifyWebApiException {

        GetRecommendationsWithFeatureDto getRecommendationsWithFeatureDto = new GetRecommendationsWithFeatureDto();
        Recommendations recommendations = null;

        try{
            List<AudioFeatures> audioFeaturesList = playListService.getSongFeatureByPlayList(playListId);
            // TODO : update `GetRecommendationsRequest` :  make seedAlbum, seedArtist not required
            // or, implement the API call by ourself : https://developer.spotify.com/documentation/web-api/reference/get-recommendations
            // https://api.spotify.com/v1/recommendations

            // get avg value of features

            // TODO : use functional way
            Float energy = null;
            Float acousticness = null;
            Float danceability = null;
            Float liveness = null;
            Float loudness = null;
            Float speechiness = null;

            for (AudioFeatures audioFeatures : audioFeaturesList){
                energy += audioFeatures.getEnergy();
                acousticness += audioFeatures.getAcousticness();
                danceability += audioFeatures.getDanceability();
                liveness += audioFeatures.getLiveness();
                loudness += audioFeatures.getLoudness();
                speechiness += audioFeatures.getSpeechiness();
                // TODO : implement others
            }
            // getRecommendationsWithFeatureDto
            GetRecommendationsRequest getRecommendationsRequest = prepareRecommendationsWithSongFeatureRequest(getRecommendationsWithFeatureDto);
            recommendations = getRecommendationsRequest.execute();
            return recommendations;
        }catch (Exception e){

        }
        return recommendations;
    }

    // TODO : merge below
    private GetRecommendationsRequest prepareRecommendationsRequest(GetRecommendationsDto getRecommendationsDto){
        return  spotifyApi.getRecommendations()
                .limit(getRecommendationsDto.getAmount())
                .market(getRecommendationsDto.getMarket())
                .max_popularity(getRecommendationsDto.getMaxPopularity())
                .min_popularity(getRecommendationsDto.getMinPopularity())
                .seed_artists(getRecommendationsDto.getSeedArtistId())
                .seed_genres(getRecommendationsDto.getSeedGenres())
                .seed_tracks(getRecommendationsDto.getSeedTrack())
                .target_popularity(getRecommendationsDto.getTargetPopularity())
                .build();
    }

    private GetRecommendationsRequest prepareRecommendationsWithSongFeatureRequest(GetRecommendationsWithFeatureDto getRecommendationsWithFeatureDto){
        return  spotifyApi.getRecommendations()
                .limit(getRecommendationsWithFeatureDto.getAmount())
                .market(getRecommendationsWithFeatureDto.getMarket())
                .max_popularity(getRecommendationsWithFeatureDto.getMaxPopularity())
                .min_popularity(getRecommendationsWithFeatureDto.getMinPopularity())
                .seed_artists(getRecommendationsWithFeatureDto.getSeedArtistId())
                .seed_genres(getRecommendationsWithFeatureDto.getSeedGenres())
                .seed_tracks(getRecommendationsWithFeatureDto.getSeedTrack())
                .target_popularity(getRecommendationsWithFeatureDto.getTargetPopularity())
                .target_danceability(getRecommendationsWithFeatureDto.getDanceability())
                .target_energy(getRecommendationsWithFeatureDto.getEnergy())
                .target_instrumentalness(getRecommendationsWithFeatureDto.getInstrumentalness())
                .target_liveness(getRecommendationsWithFeatureDto.getLiveness())
                .target_loudness(getRecommendationsWithFeatureDto.getLoudness())
                .target_speechiness(getRecommendationsWithFeatureDto.getSpeechiness())
                .build();
    }

}
