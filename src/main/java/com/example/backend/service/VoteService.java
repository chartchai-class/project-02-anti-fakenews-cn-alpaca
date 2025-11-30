package com.example.backend.service;

import com.example.backend.entity.Vote;
import com.example.backend.entity.Vote.VoteType;

import java.util.List;
import java.util.Optional;

public interface VoteService {
    Vote createOrUpdateVote(Long userId, Long newsId, VoteType voteType);
    Optional<Vote> getVoteByUserIdAndNewsId(Long userId, Long newsId);
    boolean hasVoted(Long userId, Long newsId);
    List<Long> getVotedNewsIdsByUserId(Long userId);
    int getVoteCountByNewsIdAndVoteType(Long newsId, VoteType voteType);
}