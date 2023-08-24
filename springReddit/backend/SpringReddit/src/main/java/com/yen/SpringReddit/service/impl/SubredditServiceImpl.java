//package com.yen.SpringReddit.service.impl;
//
//import com.yen.SpringReddit.dao.SubredditDao;
//import com.yen.SpringReddit.dto.SubredditDto;
//import com.yen.SpringReddit.exceptions.SpringRedditException;
//import com.yen.SpringReddit.mapper.SubredditMapper;
//import com.yen.SpringReddit.po.Subreddit;
//import com.yen.SpringReddit.service.SubredditService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static java.util.stream.Collectors.toList;
//
//@Service
////@AllArgsConstructor
//@Slf4j
//public class SubredditServiceImpl implements SubredditService {
//
//    @Autowired
//    SubredditDto subredditDto;
//
//    @Autowired
//    SubredditDao subredditDao;
//
//    @Autowired
//    SubredditMapper subredditMapper;
//
////    private final SubredditDao subredditRepository;
////    private final SubredditMapper subredditMapper;
//
//    @Transactional
//    @Override
//    public SubredditDto save(SubredditDto subredditDto) {
//
//        //SubredditDto save = subredditDao.save(subredditDto);
//        Subreddit save = subredditDao.save(subredditMapper.mapDtoToSubreddit(subredditDto));
//        subredditDto.setId(save.getId());
//        return subredditDto;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<SubredditDto> getAll() {
//
//        return subredditDao.findAll()
//                .stream()
//                .map(subredditMapper::mapSubredditToDto)
//                .collect(toList());
//    }
//
//    @Override
//    public SubredditDto getSubreddit(Long id) {
//
//        Subreddit subreddit = subredditDao.findById(id)
//                .orElseThrow(
//                        () -> new SpringRedditException("No subreddit found with ID - " + id)
//                );
//        return subredditMapper.mapSubredditToDto(subreddit);
//    }
//
//}
