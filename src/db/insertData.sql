-- 명시적으로 .sql 파일 문자셋 설정 (한글 깨짐 방지)
SET NAMES 'utf8';

-- 배너 리스트 관리 테이블 1 버전 정보 추가
DELETE FROM ref_banner_info WHERE version = 1;
INSERT INTO ref_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/0a3de54d-8ab7-0919-3ef8-266c04a409e4.jpg', 1, '빙결사');
INSERT INTO ref_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/23e9140b-102e-f4c2-9bfb-f48f7dc1813a.jpg', 2, '페이트 콜라보');
INSERT INTO ref_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/bebf85e9-c22d-e4b1-938e-3e862ebb1ad3.jpg', 3, '아트 공모전');
INSERT INTO ref_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/ce8502c3-776d-0d74-3f57-0b919abcf14b.jpg', 4, '중천');
INSERT INTO ref_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/8d466130-c079-f15b-2c1a-2233b298cfde.jpg', 5, '던파 ON 리뉴얼');
INSERT INTO ref_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/05243a37-826d-0ef2-e2b5-9b6d8fe4de1a.jpg', 6, '겨울 방학 생활');
INSERT INTO ref_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/cc55693a-3e3c-3310-c5fb-d160747652cd.jpg', 7, '스노우 메이지 추억 앨범');
INSERT INTO ref_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/13a64cd4-684e-77f8-4799-f8fcd2330326.jpg', 8, '스노우 메이지 스펙업');
