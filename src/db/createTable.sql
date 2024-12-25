-- 포커스 배너 이미지 리스트 관리 테이블 TODO (img_url 의 경우 서버에서 공백 포함 여부 체크하기)
DROP TABLE IF EXISTS ref_focus_banner_info;
CREATE TABLE ref_focus_banner_info (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    version     INT             NOT NULL COMMENT '배너 버전',
    banner_type TINYINT         NOT NULL COMMENT '배너 타입 (1: 메인 포커스 배너, 2: 뉴스 포커스 배너 첫번째, 3: 뉴스 포커스 배너 두번째)',
    -- TODO banner_url 컬럼을 추가해서 해당 배너와 실제 연결되는 페이지 링크 정보도 DB 에서 관리하도록 하기
    img_url     VARCHAR(255)    NOT NULL COMMENT '이미지 소스 링크',
    img_order   SMALLINT        NOT NULL COMMENT '이미지 출력 순서',
    img_comment VARCHAR(255)    COMMENT '이미지 설명 (참고용)',
    CONSTRAINT unique_ref_bannerInfo UNIQUE (version, banner_type, img_order)
) CHARSET=utf8 COMMENT='포커스 배너 이미지 리스트 관리 테이블';

-- 홈페이지 설정 값 관리 테이블 TODO 설정을 별도로 지정할 수 있는 보조 툴 만들기
DROP TABLE IF EXISTS site_setting;
CREATE TABLE site_setting (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    is_active           TINYINT         NOT NULL DEFAULT 0 COMMENT '설정값 리스트 활성화 여부 (0: 비활성화, 1: 활성화)',
    settings            JSON            NOT NULL COMMENT '설정값 리스트',
    settings_comment    VARCHAR(255)    COMMENT '설정값 리스트 설명 (참고용)'
) CHARSET=utf8 COMMENT='홈페이지 설정 값 관리 테이블';

-- 홈페이지 설정 값 관리 테이블 인덱스 추가
CREATE INDEX idx_site_settings_is_active ON site_setting(is_active);

-- 홈페이지 회원 정보 관리 테이블
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
    user_id             VARCHAR(255)    NOT NULL PRIMARY KEY COMMENT '유저 아이디',
    user_pw             VARCHAR(255)    NOT NULL COMMENT '유저 비밀번호',
    user_type           TINYINT         NOT NULL DEFAULT 0 COMMENT '권한 타입(0: 일반 유저, 1: 관리자 권한 유저)',
    df_server_id        VARCHAR(255)    DEFAULT '' COMMENT '던파 캐릭터 서버 아이디 (ex. hilder)',
    df_character_name   VARCHAR(255)    DEFAULT '' COMMENT '던파 캐릭터 이름 (ex. 음악들으면서)',
    last_login_date     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가장 마지막 로그인 시간',
    join_date           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '회원가입한 시간'
) CHARSET=utf8 COMMENT='홈페이지 회원 정보 관리 테이블';
