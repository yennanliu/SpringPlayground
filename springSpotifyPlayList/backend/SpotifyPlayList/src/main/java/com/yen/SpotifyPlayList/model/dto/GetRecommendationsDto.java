package com.yen.SpotifyPlayList.model.dto;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class GetRecommendationsDto {

  private int amount = 10;
  private CountryCode market = CountryCode.JP;
  private int maxPopularity = 100;
  private int minPopularity = 0;
  private String seedArtistId; // e.g. : 0LcJLqbBmaGUft1e9Mm8HV
  private String seedGenres;
  private String seedTrack; // e.g. 01iyCAUm8EvOFqVWYJ3dVX
  private int targetPopularity = 50;
}
