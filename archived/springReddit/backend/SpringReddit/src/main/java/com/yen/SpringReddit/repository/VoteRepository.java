package com.yen.SpringReddit.repository;

import com.yen.SpringReddit.model.Post;
import com.yen.SpringReddit.model.User;
import com.yen.SpringReddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}