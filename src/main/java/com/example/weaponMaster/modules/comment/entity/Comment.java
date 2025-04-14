package com.example.weaponMaster.modules.comment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_comments")
@DynamicUpdate // 변경된 필드만 업데이트
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "article_id", nullable = false)
    private Integer articleId;

    @Column(name = "re_comment_id", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer reCommentId = 0;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isDeleted = false;

    @Column(name = "create_date", nullable = false, updatable = false, insertable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    public Comment(String userId, Integer articleId, Integer reCommentId, String contents) {
        this.userId      = userId;
        this.articleId   = articleId;
        this.reCommentId = reCommentId;
        this.contents    = contents;
    }
}
