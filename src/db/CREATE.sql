-----------------------------------------------------------------------
---------------------------[ 홈페이지 설정 값 ]----------------------------

-- 홈페이지 설정 값 관리 테이블
DROP TABLE IF EXISTS site_setting;
CREATE TABLE site_setting (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    is_active           TINYINT         NOT NULL DEFAULT 0 COMMENT '설정값 리스트 활성화 여부 (0: 비활성화, 1: 활성화)',
    settings            JSON            NOT NULL COMMENT '설정값 리스트',
    settings_comment    VARCHAR(255)    COMMENT '설정값 리스트 설명 (참고용)'
) CHARSET=utf8 COMMENT='홈페이지 설정 값 관리 테이블';

-- 홈페이지 설정 값 관리 테이블 인덱스 추가
CREATE INDEX idx_site_settings_is_active ON site_setting(is_active);


------------------------------------------------------------------------
----------------------------[ 버전 별 참조 값 ]----------------------------

-- 포커스 배너 이미지 리스트 관리 테이블 TODO (img_url 의 경우 서버에서 공백 포함 여부 체크하기)
DROP TABLE IF EXISTS ref_focus_banner_info;
CREATE TABLE ref_focus_banner_info (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    version     INT             NOT NULL COMMENT '배너 버전',
    banner_type TINYINT         NOT NULL COMMENT '배너 타입 (ex. 1: 메인 포커스 배너, 2: 뉴스 포커스 배너 첫번째, 3: 뉴스 포커스 배너 두번째)',
    -- TODO banner_url 컬럼을 추가해서 해당 배너와 실제 연결되는 페이지 링크 정보도 DB 에서 관리하도록 하기
    img_url     VARCHAR(255)    NOT NULL COMMENT '이미지 소스 링크',
    img_order   SMALLINT        NOT NULL COMMENT '이미지 출력 순서',
    img_comment VARCHAR(255)    COMMENT '이미지 설명 (참고용)',
    CONSTRAINT unique_ref_bannerInfo UNIQUE (version, banner_type, img_order)
) CHARSET=utf8 COMMENT='포커스 배너 이미지 리스트 관리 테이블';


-------------------------------------------------------------------------
----------------------------[ 사용자 별 참조 값 ]----------------------------

-- 유저 회원 정보 관리 테이블
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
    user_id             VARCHAR(255)    NOT NULL PRIMARY KEY COMMENT '유저 아이디',
    user_pw             VARCHAR(255)    NOT NULL COMMENT '유저 비밀번호',
    user_type           TINYINT         NOT NULL DEFAULT 0 COMMENT '권한 타입 (0: 일반 유저, 1: 관리자 권한 유저)',
    df_server_id        VARCHAR(255)    DEFAULT '' COMMENT '던파 캐릭터 서버 아이디 (ex. hilder)',
    df_character_name   VARCHAR(255)    DEFAULT '' COMMENT '던파 캐릭터 이름 (ex. 음악들으면서)',
    last_login_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가장 마지막 로그인 시간',
    join_date           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '회원가입한 시간'
) CHARSET=utf8 COMMENT='유저 회원 정보 관리 테이블';

-- TODO 유저 로그 테이블 만들기

-- 유저 게시물 관리 테이블
DROP TABLE IF EXISTS user_article;
CREATE TABLE user_article (
    id                      INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼(= 게시물 고유 번호)',
    user_id                 VARCHAR(255)    NOT NULL COMMENT '작성자',
    category_type           TINYINT         NOT NULL COMMENT '카테고리 타입 (1: 새소식, 2: 커뮤니티, 3: 서비스센터)',
    article_type            TINYINT         NOT NULL COMMENT '카테고리 별 게시물 타입 (ex. 새소식 > 1: 공지사항, 2: 업데이트, 3: 이벤트, 4: 개발자노트)',
    article_detail_type     TINYINT         NOT NULL DEFAULT 0 COMMENT '게시물 별 세부 타입 (ex. 새소식 > 공지사항 > 1: 일반, 2: 점검), 세부 타입 사용하지 않을 경우 0',
    title                   VARCHAR(255)    NOT NULL COMMENT '제목',
    contents                TEXT            NOT NULL COMMENT '내용',
    create_date             TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '게시물 최초 작성일자',
    update_date             TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '게시물 최종 수정일자',
    comment_count           INT             NOT NULL DEFAULT 0 COMMENT '댓글 개수',
    view_count              INT             NOT NULL DEFAULT 0 COMMENT '조회수',
    is_pinned               TINYINT         NOT NULL DEFAULT 0 COMMENT '게시물 상단 고정 여부 (0: 고정 안 함, 1: 고정함)'
) CHARSET=utf8 COMMENT='게시물 관리 테이블';
-- 유저 게시물 관리 테이블 인덱스 추가
CREATE INDEX idx_user_article_category_type  ON user_article(category_type);
CREATE INDEX idx_user_article_article_type   ON user_article(article_type);
CREATE INDEX idx_user_article_is_pinned      ON user_article(is_pinned);

-- 유저 댓글 관리 테이블
DROP TABLE IF EXISTS user_comments;
CREATE TABLE user_comments (
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 고유 번호 아이디',
    user_id         VARCHAR(255)    NOT NULL COMMENT '작성자',
    article_id      INT             NOT NULL COMMENT '게시물 타겟 아이디',
    re_comment_id   INT             NOT NULL DEFAULT 0 COMMENT '대댓글 타겟 아이디 (대댓글이 아닌 일반 댓글이면 0)',
    contents        TEXT            NOT NULL COMMENT '내용',
    create_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '댓글 최초 작성일자',
    update_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '댓글 최종 수정일자'
) CHARSET=utf8 COMMENT='유저 댓글 관리 테이블';
-- 유저 댓글 관리 테이블 인덱스 추가
CREATE INDEX idx_user_comments_article_id   ON user_comments(article_id);
