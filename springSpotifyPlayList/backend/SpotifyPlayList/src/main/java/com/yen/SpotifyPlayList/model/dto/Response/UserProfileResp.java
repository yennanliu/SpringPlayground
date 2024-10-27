package com.yen.SpotifyPlayList.model.dto.Response;

import lombok.Data;
import lombok.ToString;
import java.util.HashMap;

/**
 *  Example resp
 *
 * {  "display_name" : "some_user_name",
 * "external_urls" : {
 * "spotify" : "https://open.spotify.com/user/some_id"
 * },
 * "href" : "https://api.spotify.com/v1/users/some_id",
 * "id" : "some_id",
 * "images" : [ ],
 * "type" : "user",
 * "uri" : "spotify:user:some_id",
 * "followers" : {
 * "href" : null,
 * "total" : 2
 * }
 * }
 */
@ToString
@Data
public class UserProfileResp {

    private String displayName;
    private HashMap<String, String> externalUrls;
    private String href;
    private String id;
    private String[] images;
    private String type;
    private String uri;
    private HashMap<String, Object> followers;
}
