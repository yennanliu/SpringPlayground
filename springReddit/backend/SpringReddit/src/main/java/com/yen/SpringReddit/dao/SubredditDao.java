package com.yen.SpringReddit.dao;

import com.yen.SpringReddit.po.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubredditDao extends JpaRepository<Subreddit, Long> {

    Optional<Subreddit> findByName(String subredditName);

}