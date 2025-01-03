package com.example.weaponMaster.modules.components.article.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ref_article")
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

    @Column(name = "author", nullable = false, length = 255, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String author;

    @Column(name = "create_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    @Column(name = "view_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer viewCount;

    @Column(name = "is_pinned", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isPinned;

    public Article(Integer categoryType, Integer articleType, Integer articleDetailType, String title, String contents, String author) {
        this.categoryType = categoryType;
        this.articleType = articleType;
        this.articleDetailType = articleDetailType;
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.viewCount = 0;
        this.isPinned = false;
    }
}
