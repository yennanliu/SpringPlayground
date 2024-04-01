package com.yen.SpotifyPlayList.model.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class AddSongToPlayListDto {

    private String playlistId;
    private String[] songUris;
    // TODO : remove below
    private String authCode;
}
