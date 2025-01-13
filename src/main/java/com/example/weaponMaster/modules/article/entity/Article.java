package com.example.weaponMaster.modules.article.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_article")
@Data
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "category_type", nullable = false)
    private Integer categoryType;

    @Column(name = "article_type", nullable = false)
    private Integer articleType;

    @Column(name = "article_detail_type", nullable = false)
    private Integer articleDetailType;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(name = "user_id", nullable = false, length = 255, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String userId;

    @Column(name = "create_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0;

    @Column(name = "is_pinned", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isPinned = false;

    public Article(Integer categoryType, Integer articleType, Integer articleDetailType, String title, String contents, String userId) {
        this.categoryType = categoryType;
        this.articleType = articleType;
        this.articleDetailType = articleDetailType;
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }
}
