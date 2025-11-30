package com.example.backend.service.impl;

import com.example.backend.entity.News;
import com.example.backend.entity.News.Status;
import com.example.backend.entity.Vote;
import com.example.backend.repository.NewsRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final VoteRepository voteRepository;

    @Override
    public News createNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public Page<News> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @Override
    public Page<News> getNewsByStatus(Status status, Pageable pageable) {
        return newsRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<News> searchNews(String keyword, Pageable pageable) {
        return newsRepository.searchByKeyword(keyword, pageable);
    }

    @Override
    public Page<News> searchNewsByStatus(String keyword, Status status, Pageable pageable) {
        return newsRepository.searchByStatusAndKeyword(status, keyword, pageable);
    }

    @Override
    public News updateNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public void updateNewsVotes(Long newsId) {
        // 获取新闻
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isPresent()) {
            News news = optionalNews.get();
            // 更新投票数
            int fakeVotes = voteRepository.countByNewsIdAndVoteType(newsId, Vote.VoteType.FAKE);
            int notFakeVotes = voteRepository.countByNewsIdAndVoteType(newsId, Vote.VoteType.NOT_FAKE);
            news.setFakeVotes(fakeVotes);
            news.setNotFakeVotes(notFakeVotes);
            // 根据投票结果更新新闻状态
            if (fakeVotes > notFakeVotes * 2) {
                news.setStatus(Status.FAKE);
            } else if (notFakeVotes > fakeVotes * 2) {
                news.setStatus(Status.NOT_FAKE);
            } else {
                news.setStatus(Status.PENDING);
            }
            // 保存更新后的新闻
            newsRepository.save(news);
        }
    }
}