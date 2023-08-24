//package com.yen.SpringReddit.mapper;
//
//// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/mapper/PostMapper.java
//
////import com.github.marlonlom.utilities.timeago.TimeAgo;
//import com.yen.SpringReddit.dao.CommentDao;
//import com.yen.SpringReddit.dao.VoteDao;
//import com.yen.SpringReddit.dto.PostRequest;
//import com.yen.SpringReddit.dto.PostResponse;
//import com.yen.SpringReddit.po.*;
//import com.yen.SpringReddit.service.AuthService;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Optional;
//
//import static com.yen.SpringReddit.po.VoteType.DOWNVOTE;
//import static com.yen.SpringReddit.po.VoteType.UPVOTE;
//
//@Mapper(componentModel = "spring")
//public abstract class PostMapper {
//
//    @Autowired
//    private CommentDao commentRepository;
//    @Autowired
//    private VoteDao voteRepository;
//    @Autowired
//    private AuthService authService;
//
//    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
//    @Mapping(target = "description", source = "postRequest.description")
//    @Mapping(target = "subreddit", source = "subreddit")
//    @Mapping(target = "voteCount", constant = "0")
//    @Mapping(target = "user", source = "user")
//    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);
//
//    @Mapping(target = "id", source = "postId")
//    @Mapping(target = "subredditName", source = "subreddit.name")
//    @Mapping(target = "userName", source = "user.username")
//    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
//    @Mapping(target = "duration", expression = "java(getDuration(post))")
//    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
//    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
//    public abstract PostResponse mapToDto(Post post);
//
//    Integer commentCount(Post post) {
//        return commentRepository.findByPost(post).size();
//    }
//
//    String getDuration(Post post) {
//        // TODO : fix below
//        //return TimeAgo.using(post.getCreatedDate().toEpochMilli());
//        return String.valueOf(post.getCreatedDate().toEpochMilli());
//    }
//
//    boolean isPostUpVoted(Post post) {
//        return checkVoteType(post, UPVOTE);
//    }
//
//    boolean isPostDownVoted(Post post) {
//        return checkVoteType(post, DOWNVOTE);
//    }
//
//    private boolean checkVoteType(Post post, VoteType voteType) {
//        if (authService.isLoggedIn()) {
//            Optional<Vote> voteForPostByUser =
//                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
//                            authService.getCurrentUser());
//            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
//                    .isPresent();
//        }
//        return false;
//    }
//
//}