-----------------------------------------------------------------------
---------------------------[ 홈페이지 설정 값 ]----------------------------

-- 홈페이지 설정 값 관리 테이블
DROP TABLE IF EXISTS site_setting;
CREATE TABLE site_setting (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    active_state        TINYINT         NOT NULL DEFAULT 0 COMMENT '설정값 리스트 활성화 여부 (0: 비활성화, 1: 활성화, 2: 예약중)',
    settings            JSON            NOT NULL COMMENT '설정값 리스트',
    settings_comment    VARCHAR(255)    COMMENT '설정값 리스트 설명 (참고용)',
    create_date         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '설정값 최초 작성일자',
    update_date         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '설정값 최종 수정 시간'
) CHARSET=utf8 COMMENT='홈페이지 설정 값 관리 테이블';
-- 홈페이지 설정 값 관리 테이블 인덱스 추가
CREATE INDEX idx_site_settings_is_active ON site_setting(active_state);


------------------------------------------------------------------------
----------------------------[ 버전 별 참조 값 ]----------------------------

-- 포커스 배너 이미지 리스트 관리 테이블 TODO (img_url 의 경우 서버에서 공백 포함 여부 체크하기)
DROP TABLE IF EXISTS ref_focus_banner_info;
CREATE TABLE ref_focus_banner_info (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    version     INT             NOT NULL COMMENT '배너 버전',
    banner_type SMALLINT        NOT NULL COMMENT '배너 타입 (ex. 1: 메인 포커스 배너, 2: 뉴스 포커스 배너 첫번째, 3: 뉴스 포커스 배너 두번째)',
    -- TODO banner_url 컬럼을 추가해서 해당 배너와 실제 연결되는 페이지 링크 정보도 DB 에서 관리하도록 하기
    img_url     VARCHAR(255)    NOT NULL COMMENT '이미지 소스 링크',
    img_order   SMALLINT        NOT NULL COMMENT '이미지 출력 순서',
    img_comment VARCHAR(255)    COMMENT '이미지 설명 (참고용)',
    CONSTRAINT unique_ref_bannerInfo UNIQUE (version, banner_type, img_order)
) CHARSET=utf8 COMMENT='포커스 배너 이미지 리스트 관리 테이블';

-- 퍼블리셔 로고 관리 테이블
DROP TABLE IF EXISTS ref_publisher_logo;
CREATE TABLE ref_publisher_logo (
    id      INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    version INT             NOT NULL COMMENT '배너 버전',
    img_url VARCHAR(255)    NOT NULL COMMENT '로고 이미지 소스 링크',
    alt     VARCHAR(255)    NOT NULL COMMENT '로고 이미지 설명 (alt 기입용)',
    CONSTRAINT unique_ref_bannerInfo UNIQUE (version)
) CHARSET=utf8 COMMENT='퍼블리셔 로고 관리 테이블';


-------------------------------------------------------------------------
----------------------------[ 사용자 별 참조 값 ]----------------------------

-- TODO id 컬럼 추가된 부분 백엔드 반영 필요
-- 유저 회원 정보 관리 테이블
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    user_id             VARCHAR(255)    NOT NULL COMMENT '유저 아이디',
    user_pw             VARCHAR(255)    NOT NULL COMMENT '유저 비밀번호',
    user_type           TINYINT         NOT NULL DEFAULT 0 COMMENT '권한 타입 (0: 일반 유저, 1: 관리자 권한 유저)',
    -- df_server_id        VARCHAR(255)    DEFAULT '' COMMENT '던파 캐릭터 서버 아이디 (ex. hilder)',
    -- df_character_name   VARCHAR(255)    DEFAULT '' COMMENT '던파 캐릭터 이름 (ex. 음악들으면서)',
    join_date           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '회원가입한 시간',
    last_login_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가장 마지막 로그인 시간',
    UNIQUE (user_id)
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
    comment_count           INT             NOT NULL DEFAULT 0 COMMENT '댓글 개수',
    view_count              INT             NOT NULL DEFAULT 0 COMMENT '조회수',
    is_pinned               TINYINT         NOT NULL DEFAULT 0 COMMENT '게시물 상단 고정 여부 (0: 고정 안 함, 1: 고정함)',
    create_date             TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '게시물 최초 작성일자',
    update_date             TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '게시물 최종 수정일자'
) CHARSET=utf8 COMMENT='게시물 관리 테이블';
-- 유저 게시물 관리 테이블 인덱스 추가
CREATE INDEX idx_user_article_category_type  ON user_article(category_type);
CREATE INDEX idx_user_article_article_type   ON user_article(article_type);
CREATE INDEX idx_user_article_is_pinned      ON user_article(is_pinned);
CREATE INDEX idx_user_article_view_count     ON user_article(view_count);

-- 유저 댓글 관리 테이블
DROP TABLE IF EXISTS user_comments;
CREATE TABLE user_comments (
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 고유 번호 아이디',
    user_id         VARCHAR(255)    NOT NULL COMMENT '작성자',
    article_id      INT             NOT NULL COMMENT '게시물 타겟 아이디',
    re_comment_id   INT             NOT NULL DEFAULT 0 COMMENT '대댓글 타겟 아이디 (대댓글이 아닌 일반 댓글이면 0)',
    contents        TEXT            NOT NULL COMMENT '내용',
    is_deleted      TINYINT         NOT NULL DEFAULT 0 COMMENT '댓글 삭제 여부 (0: 삭제 안 함, 1: 삭제됨)',
    create_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '댓글 최초 작성일자',
    update_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '댓글 최종 수정일자'
) CHARSET=utf8 COMMENT='유저 댓글 관리 테이블';
-- 유저 댓글 관리 테이블 인덱스 추가
CREATE INDEX idx_user_comments_article_id ON user_comments(article_id);

-- 유저 경매 판매 알림 등록 관리 테이블
DROP TABLE IF EXISTS user_auction_notice;
CREATE TABLE user_auction_notice (
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '경매 알림 고유 ID',
    user_id         VARCHAR(255)    NOT NULL COMMENT '유저 ID',
    auction_no      VARCHAR(255)    NOT NULL COMMENT '경매 등록 고유 번호',
    auction_state   TINYINT         NOT NULL DEFAULT 0 COMMENT '경매 상태 (0: 판매 중, 1: 판매 완료, 2: 판매 기간 만료)',
    item_img        VARCHAR(255)    NOT NULL COMMENT '경매 아이템 이미지 URL',
    item_info       JSON            NOT NULL COMMENT '경매 아이템 정보',
    create_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '알림 생성 날짜',
    update_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '알림 수정 날짜',
    CONSTRAINT unique_user_auction_notice UNIQUE (user_id, auction_no)
) CHARSET=utf8mb4 COMMENT='유저 경매 판매 알림 등록 관리 테이블';

-- 유저 개인 슬랙 알림 정보 관리 테이블
DROP TABLE IF EXISTS user_slack_notice;
CREATE TABLE user_slack_notice (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 row ID',
    user_id             VARCHAR(255)    NOT NULL COMMENT '유저 ID',
    notice_type         TINYINT         NOT NULL COMMENT '알림 종류 (ex. 1: 경매 판매 알림)',
    slack_bot_type      TINYINT         NOT NULL COMMENT '슬랙봇 종류 (ex. 1: 일반 웨펀마스터 알림봇)',
    slack_bot_token     VARCHAR(255)    NOT NULL COMMENT '슬랙 봇 토큰',
    slack_channel_id    VARCHAR(255)    NOT NULL COMMENT '개인 슬랙 채널 아이디',
    create_date         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '정보 생성 날짜',
    update_date         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '정보 수정 날짜',
    UNIQUE (user_id, notice_type)
) CHARSET=utf8mb4 COMMENT='유저 개인 슬랙 알림 정보 관리 테이블';


-------------------------------------------------------------------------
----------------------------[ 관리용 참조 값 ]------------------------------

-- 관리자 공통 슬랙 알림 정보 관리 테이블
DROP TABLE IF EXISTS admin_slack_notice;
CREATE TABLE admin_slack_notice (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 row ID',
    channel_comment     VARCHAR(255)    DEFAULT '' COMMENT '채널 알림 종류 설명 (참고용 서술)',
    channel_type        TINYINT         NOT NULL   COMMENT '채널 종류 (ex. 1: 1대1 문의 등록 알림)',
    slack_bot_type      TINYINT         NOT NULL   COMMENT '슬랙봇 종류 (ex. 1: 일반 웨펀마스터 알림봇)',
    slack_bot_token     VARCHAR(255)    NOT NULL   COMMENT '슬랙 봇 토큰',
    slack_channel_id    VARCHAR(255)    NOT NULL   COMMENT '슬랙 채널 아이디',
    create_date         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '정보 생성 날짜',
    update_date         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '정보 수정 날짜',
    UNIQUE (channel_type, slack_bot_type)
) CHARSET=utf8mb4 COMMENT='관리자 공통 슬랙 알림 정보 관리 테이블';

-- 슬랙봇 정보 관리 테이블
DROP TABLE IF EXISTS slack_bot;
CREATE TABLE slack_bot (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 row ID',
    type_comment        VARCHAR(255)    DEFAULT '' COMMENT '슬랙봇 타입 설명 (참고용 서술)',
    type                TINYINT         NOT NULL   COMMENT '슬랙봇 타입 (ex. 1: 일반 웨펀마스터 알림봇)',
    client_id           VARCHAR(255)    NOT NULL   COMMENT '슬랙봇 클라이언트 ID',
    client_secret       VARCHAR(255)    NOT NULL   COMMENT '슬랙봇 클라이언트 secret',
    redirect_uri        VARCHAR(255)    NOT NULL   COMMENT '슬랙봇 리디렉트 주소',
    create_date         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '정보 생성 날짜',
    update_date         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '정보 수정 날짜',
    UNIQUE (type)
) CHARSET=utf8mb4 COMMENT='슬랙봇 정보 관리 테이블';
