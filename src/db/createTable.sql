-- 배너 이미지 리스트 관리 테이블 TODO (img_url 의 경우 서버에서 공백 포함 여부 체크하기)
DROP TABLE IF EXISTS ref_banner_info;
CREATE TABLE ref_banner_info (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '기본 키 컬럼',
    version     INT             NOT NULL COMMENT '배너 버전',
    banner_type TINYINT         NOT NULL COMMENT '배너 타입(1: 메인 포커스 배너, 2: 뉴스 배너 첫번째, 3: 뉴스 배너 두번째)',
    img_url     VARCHAR(255)    NOT NULL COMMENT '이미지 링크',
    img_order   SMALLINT        NOT NULL COMMENT '이미지 출력 순서',
    img_comment VARCHAR(255)    COMMENT '이미지 설명',  -- 기본값 없음 (NULL 허용)
    CONSTRAINT unique_ref_bannerInfo UNIQUE (version, banner_type, img_order)
) CHARSET=utf8 COMMENT='배너 이미지 리스트 관리 테이블';
