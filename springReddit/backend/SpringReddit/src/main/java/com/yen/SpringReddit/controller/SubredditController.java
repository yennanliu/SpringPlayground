package com.yen.SpringReddit.controller;

// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/controller/SubredditController.java
// https://youtu.be/PN3gxkub4Ew?t=367

import com.yen.SpringReddit.service.SubredditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subreddit")
//@AllArgsConstructor
@Slf4j
public class SubredditController {

    @Autowired
    SubredditService subredditService;
}
