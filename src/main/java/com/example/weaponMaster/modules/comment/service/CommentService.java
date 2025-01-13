package com.example.weaponMaster.modules.comment.service;

import com.example.weaponMaster.api.comments.dto.ReqCommentsDto;
import com.example.weaponMaster.modules.article.constant.ArticleType;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.entity.Article;
import com.example.weaponMaster.modules.comment.dto.CommentDto;
import com.example.weaponMaster.modules.comment.entity.Comment;
import com.example.weaponMaster.modules.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public boolean createComment(ReqCommentsDto request) {
        Comment comment = new Comment(
                request.getUserId(),
                request.getArticleId(),
                request.getReCommentId(),
                request.getContents()
        );

        try {
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            System.err.println("Error create comment: " + e.getMessage());
            return false;
        }
    }

    public CommentDto[] getCommentList(Integer articleId) {
        Comment[] comments;

        try {
            comments = commentRepository.findByArticleId(articleId);
        } catch (Exception e) {
            System.err.println("Error fetching comments: " + e.getMessage());
            return null;
        }

        if (comments == null) {
            return null;
        }

        // Comment 엔티티를 CommentDto 로 매핑
        return Arrays.stream(comments)
                .map(comment -> CommentDto.builder()
                        .id(comment.getId())
                        .userId(comment.getUserId())
                        .articleId(comment.getArticleId())
                        .reCommentId(comment.getReCommentId())
                        .contents(comment.getContents())
                        .createDate(comment.getCreateDate())
                        .updateDate(comment.getUpdateDate())
                        .build())
                .toArray(CommentDto[]::new);
    }
}
