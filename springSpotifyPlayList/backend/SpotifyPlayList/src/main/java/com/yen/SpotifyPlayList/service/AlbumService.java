package com.yen.SpotifyPlayList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;

import java.io.IOException;

@Service
@Slf4j
public class AlbumService {

  @Autowired private AuthService authService;

  /**
   * Fetches the album by the given album ID.
   *
   * @param albumId The ID of the album to fetch.
   * @return The album details.
   * @throws SpotifyWebApiException If an error occurs during the API request.
   */
  public Album getAlbum(String albumId) throws SpotifyWebApiException {
    SpotifyApi spotifyApi = authService.initializeSpotifyApi();

    try {
      final GetAlbumRequest getAlbumRequest = spotifyApi.getAlbum(albumId).build();
      Album album = getAlbumRequest.execute();
      log.info("Album fetched: {}", album);
      return album;
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      log.error("Error fetching album: {}", e.getMessage());
      throw new SpotifyWebApiException("Error fetching album: " + e.getMessage(), e);
    }
  }

  /**
   * Fetches the tracks of the given album ID.
   *
   * @param albumId The ID of the album whose tracks are to be fetched.
   * @return A Paging object containing the simplified tracks.
   * @throws SpotifyWebApiException If an error occurs during the API request.
   */
  public Paging<TrackSimplified> getAlbumTrack(String albumId) throws SpotifyWebApiException {
    SpotifyApi spotifyApi = authService.initializeSpotifyApi();

    try {
      final GetAlbumsTracksRequest getAlbumsTracksRequest =
          spotifyApi.getAlbumsTracks(albumId).build();
      Paging<TrackSimplified> trackSimplifiedPaging = getAlbumsTracksRequest.execute();
      log.info("Fetched {} tracks for album {}", trackSimplifiedPaging.getTotal(), albumId);
      return trackSimplifiedPaging;
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      log.error("Error fetching album tracks: {}", e.getMessage());
      throw new SpotifyWebApiException("Error fetching album tracks: " + e.getMessage(), e);
    }
  }
}
