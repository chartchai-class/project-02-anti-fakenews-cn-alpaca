package com.example.backend.controller;

import com.example.backend.entity.News;
import com.example.backend.entity.News.Status;
import com.example.backend.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:5173")
public class NewsController {
    @Autowired
    private NewsService newsService;

    // 获取新闻列表，支持分页、状态过滤和关键词搜索
    @GetMapping
    public ResponseEntity<Page<News>> getNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage;

        if (keyword != null && !keyword.isEmpty()) {
            if (status != null) {
                newsPage = newsService.searchNewsByStatus(keyword, status, pageable);
            } else {
                newsPage = newsService.searchNews(keyword, pageable);
            }
        } else if (status != null) {
            newsPage = newsService.getNewsByStatus(status, pageable);
        } else {
            newsPage = newsService.getAllNews(pageable);
        }

        return ResponseEntity.ok(newsPage);
    }

    // 获取单个新闻
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 创建新闻
    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News createdNews = newsService.createNews(news);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    // 更新新闻
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News newsDetails) {
        return newsService.getNewsById(id)
                .map(existingNews -> {
                    newsDetails.setId(id);
                    News updatedNews = newsService.updateNews(newsDetails);
                    return ResponseEntity.ok(updatedNews);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 删除新闻
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        return newsService.getNewsById(id)
                .map(news -> {
                    newsService.deleteNews(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}