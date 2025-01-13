package com.example.weaponMaster.modules.comment.repository;

import com.example.weaponMaster.modules.article.entity.Article;
import com.example.weaponMaster.modules.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT * FROM user_comments WHERE article_id = :articleId ORDER BY id ASC", nativeQuery = true)
    Comment[] findCommentList(
            @Param("articleId") Integer articleId
    );


}
