package com.example.backend.service.impl;

import com.example.backend.entity.News;
import com.example.backend.entity.User;
import com.example.backend.entity.Vote;
import com.example.backend.entity.Vote.VoteType;
import com.example.backend.repository.NewsRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.service.NewsService;
import com.example.backend.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final NewsService newsService;

    @Override
    public Vote createOrUpdateVote(Long userId, Long newsId, VoteType voteType) {
        // 获取用户和新闻
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        // 查找现有投票
        Optional<Vote> existingVote = voteRepository.findByUserIdAndNewsId(userId, newsId);

        Vote vote;
        if (existingVote.isPresent()) {
            // 更新现有投票
            vote = existingVote.get();
            vote.setVoteType(voteType);
        } else {
            // 创建新投票
            vote = new Vote();
            vote.setUser(user);
            vote.setNews(news);
            vote.setVoteType(voteType);
        }

        // 保存投票
        Vote savedVote = voteRepository.save(vote);

        // 更新新闻投票数
        newsService.updateNewsVotes(newsId);

        return savedVote;
    }

    @Override
    public Optional<Vote> getVoteByUserIdAndNewsId(Long userId, Long newsId) {
        return voteRepository.findByUserIdAndNewsId(userId, newsId);
    }

    @Override
    public boolean hasVoted(Long userId, Long newsId) {
        return voteRepository.findByUserIdAndNewsId(userId, newsId).isPresent();
    }

    @Override
    public List<Long> getVotedNewsIdsByUserId(Long userId) {
        return voteRepository.findVotedNewsIdsByUserId(userId);
    }

    @Override
    public int getVoteCountByNewsIdAndVoteType(Long newsId, VoteType voteType) {
        return voteRepository.countByNewsIdAndVoteType(newsId, voteType);
    }
}