package com.example.weaponMaster.api.comments.controller;

import com.example.weaponMaster.api.comments.dto.ReqCommentsDto;
import com.example.weaponMaster.api.comments.dto.RespCommentsDto;
import com.example.weaponMaster.modules.comment.dto.CommentDto;
import com.example.weaponMaster.modules.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/comments")
    public RespCommentsDto createComment(@RequestBody ReqCommentsDto request) {
        boolean isSuccess = commentService.createComment(request);
        return new RespCommentsDto(isSuccess);
    }

    // 댓글 리스트 조회
    @GetMapping("/comments/{id}")
    public RespCommentsDto getCommentList(@PathVariable("id") Integer articleId) {
        CommentDto[] comments = commentService.getCommentList(articleId);
        return new RespCommentsDto(true, comments);
    }
}
