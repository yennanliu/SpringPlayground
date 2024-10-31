package com.yen.SpotifyPlayList.model.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class CreatePlayListDto {

  private String userId;
  private String name;
  // TODO : remove below
  private String authCode;
}
