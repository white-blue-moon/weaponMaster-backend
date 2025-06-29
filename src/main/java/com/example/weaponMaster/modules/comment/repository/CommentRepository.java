package com.example.weaponMaster.modules.comment.repository;

import com.example.weaponMaster.modules.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT * FROM user_comments WHERE article_id = :articleId ORDER BY id ASC", nativeQuery = true)
    Comment[] findByArticleId(Integer articleId);

    @Query(value = "SELECT COUNT(id) FROM user_comments WHERE article_id = :articleId", nativeQuery = true)
    int countByArticleId(Integer articleId);

    @Modifying
    @Query(value = "DELETE FROM user_comments WHERE article_id = :articleId", nativeQuery = true)
    void deleteByArticleId(Integer articleId);
}
