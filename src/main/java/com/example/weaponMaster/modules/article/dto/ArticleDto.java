package com.example.weaponMaster.modules.article.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ArticleDto {

    private Integer id;                // 게시물 고유 ID
    private Integer categoryType;      // 카테고리 타입
    private Integer articleType;       // 게시물 타입
    private Integer articleDetailType; // 게시물 세부 타입
    private String title;              // 게시물 제목
    private String contents;           // 게시물 내용
    private String userId;             // 작성자
    private LocalDateTime createDate;  // 작성 날짜
    private LocalDateTime updateDate;  // 수정 날짜
    private Integer viewCount;         // 조회수
    private Boolean isPinned;          // 상단 고정 여부
}
