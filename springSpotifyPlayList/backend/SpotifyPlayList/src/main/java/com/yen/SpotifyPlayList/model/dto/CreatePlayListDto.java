package com.yen.SpotifyPlayList.model.dto;

import lombok.*;

@ToString
@Data
public class CreatePlayListDto {

    private String userId;
    private String name;
    private String authCode;
}
