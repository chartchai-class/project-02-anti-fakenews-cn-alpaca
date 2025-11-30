package com.example.backend.controller;

import com.example.backend.entity.Vote;
import com.example.backend.entity.Vote.VoteType;
import com.example.backend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "http://localhost:5173")
public class VoteController {
    @Autowired
    private VoteService voteService;

    // 创建或更新投票
    @PostMapping
    public ResponseEntity<Vote> createOrUpdateVote(@RequestBody VoteRequest voteRequest) {
        Vote vote = voteService.createOrUpdateVote(voteRequest.getUserId(), voteRequest.getNewsId(), voteRequest.getVoteType());
        return ResponseEntity.ok(vote);
    }

    // 检查用户是否已投票
    @GetMapping("/has-voted")
    public ResponseEntity<Boolean> hasVoted(@RequestParam Long userId, @RequestParam Long newsId) {
        boolean hasVoted = voteService.hasVoted(userId, newsId);
        return ResponseEntity.ok(hasVoted);
    }

    // 获取用户已投票的新闻ID列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Long>> getVotedNewsIdsByUserId(@PathVariable Long userId) {
        List<Long> votedNewsIds = voteService.getVotedNewsIdsByUserId(userId);
        return ResponseEntity.ok(votedNewsIds);
    }

    // 获取新闻的投票数
    @GetMapping("/count")
    public ResponseEntity<VoteCountResponse> getVoteCount(@RequestParam Long newsId, @RequestParam VoteType voteType) {
        int count = voteService.getVoteCountByNewsIdAndVoteType(newsId, voteType);
        VoteCountResponse response = new VoteCountResponse(count);
        return ResponseEntity.ok(response);
    }

    // VoteRequest 内部类
    public static class VoteRequest {
        private Long userId;
        private Long newsId;
        private VoteType voteType;

        // Getters and Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getNewsId() {
            return newsId;
        }

        public void setNewsId(Long newsId) {
            this.newsId = newsId;
        }

        public VoteType getVoteType() {
            return voteType;
        }

        public void setVoteType(VoteType voteType) {
            this.voteType = voteType;
        }
    }

    // VoteCountResponse 内部类
    public static class VoteCountResponse {
        private int count;

        public VoteCountResponse(int count) {
            this.count = count;
        }

        // Getters and Setters
        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}