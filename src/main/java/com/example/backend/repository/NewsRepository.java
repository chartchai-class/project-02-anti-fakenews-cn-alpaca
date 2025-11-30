package com.example.backend.repository;

import com.example.backend.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long> {
    Page<News> findByStatus(News.Status status, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword% OR n.reporter LIKE %:keyword%")
    Page<News> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT n FROM News n WHERE (n.status = :status) AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword% OR n.reporter LIKE %:keyword%)")
    Page<News> searchByStatusAndKeyword(@Param("status") News.Status status, @Param("keyword") String keyword, Pageable pageable);
}