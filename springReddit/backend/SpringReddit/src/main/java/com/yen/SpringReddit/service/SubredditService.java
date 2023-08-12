package com.yen.SpringReddit.service;

import com.yen.SpringReddit.dto.SubredditDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/service/SubredditService.java

public interface SubredditService {

    @Transactional
    public SubredditDto save(SubredditDto subredditDto);

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll();

    public SubredditDto getSubreddit(Long id);
}
