package com.example.backend.service;

import com.example.backend.entity.News;
import com.example.backend.entity.News.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    News createNews(News news);
    Optional<News> getNewsById(Long id);
    Page<News> getAllNews(Pageable pageable);
    Page<News> getNewsByStatus(Status status, Pageable pageable);
    Page<News> searchNews(String keyword, Pageable pageable);
    Page<News> searchNewsByStatus(String keyword, Status status, Pageable pageable);
    News updateNews(News news);
    void deleteNews(Long id);
    void updateNewsVotes(Long newsId);
}