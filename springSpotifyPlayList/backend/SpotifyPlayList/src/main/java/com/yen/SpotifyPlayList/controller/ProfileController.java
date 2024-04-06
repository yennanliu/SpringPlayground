//package com.yen.SpotifyPlayList.controller;
//
//import com.yen.SpotifyPlayList.service.AlbumService;
//import com.yen.SpotifyPlayList.service.ProfileService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import se.michaelthelin.spotify.model_objects.specification.Paging;
//import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
//
//@Slf4j
//@RestController
//@RequestMapping("/profile")
//public class UserProfileController {
//
//    @Autowired
//    private ProfileService profileService;
//
//    @GetMapping("/userId")
//    public ResponseEntity getCurrentUserId(@PathVariable String authCode){
//        String userId = null;
//        try{
//            //userId = profileService.getCurrentUserId(authCode);
//            userId = profileService.getCurrentUserId();
//            return ResponseEntity.status(HttpStatus.OK).body(userId);
//        }catch (Exception e){
//            log.error("getCurrentUserId error : " + e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//}
