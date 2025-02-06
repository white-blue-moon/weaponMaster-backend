package com.example.weaponMaster.modules.comment.service;

import com.example.weaponMaster.api.comments.dto.ReqCommentsDto;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.service.UserInfoService;
import com.example.weaponMaster.modules.article.service.ArticleService;
import com.example.weaponMaster.modules.comment.dto.CommentDto;
import com.example.weaponMaster.modules.comment.entity.Comment;
import com.example.weaponMaster.modules.comment.repository.CommentRepository;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleService articleService;
    private final UserInfoService userInfoService;
    private final CommentRepository commentRepository;

    @Transactional
    public ApiResponse<Void> createComment(ReqCommentsDto request) {
        Comment comment = new Comment(
                request.getUserId(),
                request.getArticleId(),
                request.getReCommentId(),
                request.getContents()
        );

        commentRepository.save(comment);
        updateCommentCount(request.getArticleId());

        return ApiResponse.success();
    }

    public ApiResponse<CommentDto[]> getCommentList(Integer articleId) {
        Comment[] comments = commentRepository.findByArticleId(articleId);
        if (comments == null || comments.length == 0) {
            return ApiResponse.success(new CommentDto[0]);
        }

        CommentDto[] commentDtoList = Arrays.stream(comments)
                .map(this::convertToDto)
                .toArray(CommentDto[]::new);

        return ApiResponse.success(commentDtoList);
    }

    @Transactional
    public ApiResponse<Void> deleteComment(ReqCommentsDto request, Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found, id: " + id));

        // 1. 댓글 소유자가 맞는지 확인
        if (!comment.getUserId().equals(request.getUserId())) {
            UserInfo userInfo = userInfoService.getUserInfoEntity(request.getUserId());
            if (userInfo == null) {
                throw new IllegalArgumentException("User not found, userId: " + request.getUserId());
            }

            // 2. 소유자가 다를 경우 관리자 권한 있는지 확인
            if (userInfo.getUserType() != UserType.ADMIN) {
                throw new IllegalArgumentException("User does not have admin privileges: " + request.getUserId());
            }
        }

        commentRepository.deleteById(id);
        updateCommentCount(request.getArticleId());

        return ApiResponse.success();
    }

    @Transactional
    private void updateCommentCount(Integer articleId) {
        int commentCount = commentRepository.countByArticleId(articleId);
        articleService.updateCommentCount(articleId, commentCount);
    }

    private CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .articleId(comment.getArticleId())
                .reCommentId(comment.getReCommentId())
                .contents(comment.getContents())
                .createDate(comment.getCreateDate())
                .updateDate(comment.getUpdateDate())
                .build();
    }
}
