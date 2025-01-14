package com.example.weaponMaster.modules.comment.service;

import com.example.weaponMaster.api.comments.dto.ReqCommentsDto;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.service.UserInfoService;
import com.example.weaponMaster.modules.article.service.ArticleService;
import com.example.weaponMaster.modules.comment.dto.CommentDto;
import com.example.weaponMaster.modules.comment.entity.Comment;
import com.example.weaponMaster.modules.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleService articleService;
    private final UserInfoService userInfoService;
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
        } catch (Exception e) {
            System.err.println("Error create comment: " + e.getMessage());
            return false;
        }

        // 댓글 개수 업데이트
        CommentDto[] comments = getCommentList(request.getArticleId());
        boolean isSuccess = articleService.updateCommentCount(request.getArticleId(), comments.length);
        return isSuccess;
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

    public boolean deleteComment(ReqCommentsDto request, Integer id) {
        Optional<Comment> commentOptional;

        try {
            commentOptional = commentRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error get comment (delete comment): " + e.getMessage());
            return false;
        }

        if (commentOptional.isEmpty()) {
            return false;
        }

        // Optional 에서 값을 가져옴
        Comment comment = commentOptional.get();

        // 1. 댓글 소유자가 맞는지 확인하기
        // 2. 소유자가 다를 경우 관리자 권한 있는지 확인
        if (!Objects.equals(comment.getUserId(), request.getUserId())) {
            UserInfo userInfo;

            try {
                userInfo = userInfoService.getUserInfoEntity(request.getUserId());
            } catch (Exception e) {
                System.err.println("Error get user info (delete comment): " + e.getMessage());
                return false;
            }

            if (userInfo == null) {
                System.err.printf("Error can't find user info (delete comment), userId: %s \n", request.getUserId());
                return false;
            }

            if (userInfo.getUserType() != UserType.ADMIN) {
                System.err.println("Error userType is not ADMIN (delete comment)");
                return false;
            }
        }

        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error delete comment: " + e.getMessage());
            return false;
        }
        
        // 댓글 개수 업데이트
        CommentDto[] comments = getCommentList(request.getArticleId());
        boolean isSuccess = articleService.updateCommentCount(request.getArticleId(), comments.length);
        return isSuccess;
    }

}
