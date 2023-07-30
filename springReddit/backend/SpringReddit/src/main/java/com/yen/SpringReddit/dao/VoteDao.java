package com.yen.SpringReddit.dao;

import com.yen.SpringReddit.po.Post;
import com.yen.SpringReddit.po.User;
import com.yen.SpringReddit.po.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteDao extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}