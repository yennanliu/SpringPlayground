package com.yen.SpotifyPlayList.model.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class AddSongToPlayListDto {

    private String playlistId;
    private String[] songUris;
    //private String songUris; // e.g. : spotify:track:473hhKS2ebm8nZEAE0KNMo,spotify:track:1wHrcMSpzIbNk4CipbKft0,spotify:track:69TMjHuBaLNRtMPopKWbdC,spotify:track:3xsFZOyd6mfrjZT1Sf4nXR,spotify:track:4kuKGST6Pj4iMZBpO6BYl4,spotify:track:1dO38CsQliftngGVX2NwI2,spotify:track:6gWRznlX7vaUW0r8KF9iMZ,spotify:track:4KU4UDuuZEjiFGP01OkF9H,spotify:track:6J5Y0T35JVtOpQLVcOvvCa,spotify:track:1Yzrg6yWa8L4bB24EomeKV

    // TODO : remove below
    private String authCode;
}
