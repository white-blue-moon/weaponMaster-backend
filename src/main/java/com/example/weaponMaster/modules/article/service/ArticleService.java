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
    private final UserInfoRepository userInfoRepository;

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
                .map(article -> ArticleDto.builder()
                        .id(article.getId())
                        .categoryType(article.getCategoryType())
                        .articleType(article.getArticleType())
                        .articleDetailType(article.getArticleDetailType())
                        .title(article.getTitle())
                        .contents(article.getContents())
                        .userId(article.getUserId())
                        .createDate(article.getCreateDate())
                        .updateDate(article.getUpdateDate())
                        .viewCount(article.getViewCount())
                        .isPinned(article.getIsPinned())
                        .build()
                )
                .toArray(ArticleDto[]::new); // 스트림 결과를 ArticleDto[] 배열로 변환
    }

    public ArticleDto[] getArticle(Integer id) {
        Optional<Article> articleOptional;

        try {
            articleOptional = articleRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error get article by ID: " + e.getMessage());
            return null;
        }

        if (articleOptional.isEmpty()) {
            return null;
        }

        // Optional 에서 값을 가져옴
        Article article = articleOptional.get();

        // Article 객체를 ArticleDto 배열로 변환
        ArticleDto articleDto = ArticleDto.builder()
                .id(article.getId())
                .categoryType(article.getCategoryType())
                .articleType(article.getArticleType())
                .articleDetailType(article.getArticleDetailType())
                .title(article.getTitle())
                .contents(article.getContents())
                .userId(article.getUserId())
                .createDate(article.getCreateDate())
                .updateDate(article.getUpdateDate())
                .viewCount(article.getViewCount())
                .isPinned(article.getIsPinned())
                .build();

        // ArticleDto 를 배열로 반환
        return new ArticleDto[]{articleDto};
    }

}
