package com.example.weaponMaster.api.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqCommentsDto {
    private String userId;
    private Integer articleId;
    private Integer reCommentId;
    private String contents;
}
