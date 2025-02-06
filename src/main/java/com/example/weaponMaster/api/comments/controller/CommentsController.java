package com.example.weaponMaster.api.comments.controller;

import com.example.weaponMaster.api.comments.dto.ReqCommentsDto;
import com.example.weaponMaster.modules.comment.dto.CommentDto;
import com.example.weaponMaster.modules.comment.service.CommentService;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/comments")
    public ApiResponse<Void> createComment(@RequestBody ReqCommentsDto request) {
        return commentService.createComment(request);
    }

    // 댓글 리스트 조회
    @GetMapping("/comments/{id}")
    public ApiResponse<CommentDto[]> getCommentList(@PathVariable("id") Integer articleId) {
        return commentService.getCommentList(articleId);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ApiResponse<Void> deleteComment(@RequestBody ReqCommentsDto request, @PathVariable("id") Integer id) {
        return commentService.deleteComment(request, id);
    }
}
