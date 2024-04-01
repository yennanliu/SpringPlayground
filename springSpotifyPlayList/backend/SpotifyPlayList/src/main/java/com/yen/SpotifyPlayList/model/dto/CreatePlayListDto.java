package com.yen.SpotifyPlayList.model.dto;

import lombok.*;

@ToString
@Data
public class CreatePlayListDto {

    private String userId;
    private String name;
    // TODO : remove below
    private String authCode;
}
