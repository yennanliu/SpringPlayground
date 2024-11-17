package com.yen.SpringReddit.repository;

import com.yen.SpringReddit.model.Post;
import com.yen.SpringReddit.model.Subreddit;
import com.yen.SpringReddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/repository/PostRepository.java

@Repository // TODO : check if @Repository is nessary and why
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}