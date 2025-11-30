package com.example.backend.service;

import com.example.backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentService {
    Comment createComment(Comment comment);
    Optional<Comment> getCommentById(Long id);
    Page<Comment> getCommentsByNewsId(Long newsId, Pageable pageable);
    Comment updateComment(Comment comment);
    void deleteComment(Long id);
}