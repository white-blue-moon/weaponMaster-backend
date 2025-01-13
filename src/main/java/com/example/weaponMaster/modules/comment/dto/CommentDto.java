package com.example.weaponMaster.modules.comment.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Integer id;
    private String userId;
    private Integer articleId;
    private Integer reCommentId;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
