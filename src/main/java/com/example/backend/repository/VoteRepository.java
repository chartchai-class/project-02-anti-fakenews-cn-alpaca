package com.example.backend.repository;

import com.example.backend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndNewsId(Long userId, Long newsId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.news.id = :newsId AND v.voteType = :voteType")
    int countByNewsIdAndVoteType(@Param("newsId") Long newsId, @Param("voteType") Vote.VoteType voteType);

    @Query("SELECT v.news.id FROM Vote v WHERE v.user.id = :userId")
    List<Long> findVotedNewsIdsByUserId(@Param("userId") Long userId);
}