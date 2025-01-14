package com.example.weaponMaster.modules.article.service;

import com.example.weaponMaster.api.articles.dto.ReqArticlesDto;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import com.example.weaponMaster.modules.article.constant.ArticleType;
import com.example.weaponMaster.modules.article.constant.CategoryType;
import com.example.weaponMaster.modules.article.dto.ArticleDto;
import com.example.weaponMaster.modules.article.entity.Article;
import com.example.weaponMaster.modules.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserInfoRepository userInfoRepository; // TODO 서비스단 불러오기

    public boolean createArticle(ReqArticlesDto request) {
        // TODO 새소식인 경우 관리자 권한이 있는지 확인하기

        // TODO isPinned 와 같은 값도 반영되도록 수정 필요 (생성, 수정) 공통 사용 중
        Article article = new Article(
                request.getCategoryType(),
                request.getArticleType(),
                request.getArticleDetailType(),
                request.getTitle(),
                request.getContents(),
                request.getUserId()
        );

        try {
            articleRepository.save(article);
            return true;
        } catch (Exception e) {
            System.err.println("Error create article: " + e.getMessage());
            return false;
        }
    }

    public boolean updateArticle(ReqArticlesDto request, Integer id) {
        // TODO 게시물 소유자가 맞는지 확인하기, 새소식인 경우 관리자 권한이 있는지 확인하기

        // TODO 게시물 DB 조회 후 변경된 값만 바꿔서 update 되는 방식으로 수정하기
        // TODO isPinned 와 같은 값도 반영되도록 수정 필요 (생성, 수정) 공통 사용 중
        Article article = new Article(
                request.getCategoryType(),
                request.getArticleType(),
                request.getArticleDetailType(),
                request.getTitle(),
                request.getContents(),
                request.getUserId()
        );
        article.setId(id);

        try {
            articleRepository.save(article);
            return true;
        } catch (Exception e) {
            System.err.println("Error update article: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCommentCount(Integer articleId, Integer commentCount) {
        Article article = getArticleEntity(articleId);
        if (article == null) {
            System.err.println("Error article doesn't exist: " + articleId);
            return false;
        }

        article.setCommentCount(commentCount);

        try {
            articleRepository.save(article);
        } catch (Exception e) {
            System.err.println("Error update article commentCount: " + e.getMessage() + (" ("+ articleId + "/" + commentCount + ")"));
            return false;
        }

        return true;
    }

    public boolean deleteArticle(ReqArticlesDto request, Integer id) {
        Optional<Article> articleOptional;

        try {
            articleOptional = articleRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error get article by ID (delete article): " + e.getMessage());
            return false;
        }

        if (articleOptional.isEmpty()) {
            return false;
        }

        // Optional 에서 값을 가져옴
        Article article = articleOptional.get();

        // TODO 게시물 소유자가 맞는지 확인하기 (일반 게시물)
        // 새소식 카테고리 글 삭제 시도 시 관리자 권한 있는지 확인
        if (article.getCategoryType() == CategoryType.NEWS) {
            UserInfo userInfo;

            try {
                userInfo = userInfoRepository.findByUserId(request.getUserId());
            } catch (Exception e) {
                System.err.println("Error get user info (delete article): " + e.getMessage());
                return false;
            }

            if (userInfo == null) {
                System.err.printf("Error can't find user info (delete article), userId: %s \n", request.getUserId());
                return false;
            }

            if (userInfo.getUserType() == UserType.NORMAL) {
                System.err.println("Error userType is not ADMIN (delete article)");
                return false;
            }
        }

        try {
            articleRepository.deleteById(article.getId());
            return true;
        } catch (Exception e) {
            System.err.println("Error delete article: " + e.getMessage());
            return false;
        }
    }

    public ArticleDto[] getArticleList(Integer categoryType, Integer articleType) {
        Article[] articles;

        try {
            if (articleType == ArticleType.ALL) {
                articles = articleRepository.findArticlesByCategory(categoryType);
            } else {
                articles = articleRepository.findArticleList(categoryType, articleType);
            }
        } catch (Exception e) {
            System.err.println("Error get article list: " + e.getMessage());
            return null;
        }

        if (articles == null) {
            return null;
        }

        return Arrays.stream(articles)
                .map(this::convertToDto)  // 변환 함수 호출
                .toArray(ArticleDto[]::new);
    }

    public Article getArticleEntity(Integer id) {
        Optional<Article> articleOptional;

        try {
            articleOptional = articleRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error get article: " + e.getMessage());
            return null;
        }

        Article article = articleOptional.get();
        return article;
    }

    public ArticleDto getArticle(Integer id) {
        Article article = getArticleEntity(id);  // 엔티티 가져오기
        return convertToDto(article);  // 엔티티를 DTO로 변환
    }

    // Article 엔티티를 ArticleDto로 변환하는 메서드
    private ArticleDto convertToDto(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .categoryType(article.getCategoryType())
                .articleType(article.getArticleType())
                .articleDetailType(article.getArticleDetailType())
                .title(article.getTitle())
                .contents(article.getContents())
                .userId(article.getUserId())
                .createDate(article.getCreateDate())
                .updateDate(article.getUpdateDate())
                .commentCount(article.getCommentCount())
                .viewCount(article.getViewCount())
                .isPinned(article.getIsPinned())
                .build();
    }
}
