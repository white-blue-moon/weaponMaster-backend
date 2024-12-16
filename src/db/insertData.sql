-- 명시적으로 .sql 파일 문자셋 설정 (한글 깨짐 방지)
SET NAMES 'utf8';

-- 메인 포커스 배너 1 버전 정보 추가
DELETE FROM ref_focus_banner_info WHERE version = 1 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/0a3de54d-8ab7-0919-3ef8-266c04a409e4.jpg', 1, '빙결사');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/23e9140b-102e-f4c2-9bfb-f48f7dc1813a.jpg', 2, '페이트 콜라보');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/bebf85e9-c22d-e4b1-938e-3e862ebb1ad3.jpg', 3, '아트 공모전');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/ce8502c3-776d-0d74-3f57-0b919abcf14b.jpg', 4, '중천');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/8d466130-c079-f15b-2c1a-2233b298cfde.jpg', 5, '던파 ON 리뉴얼');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/05243a37-826d-0ef2-e2b5-9b6d8fe4de1a.jpg', 6, '겨울 방학 생활');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/cc55693a-3e3c-3310-c5fb-d160747652cd.jpg', 7, '스노우 메이지 추억 앨범');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/13a64cd4-684e-77f8-4799-f8fcd2330326.jpg', 8, '스노우 메이지 스펙업');

-- 뉴스 포커스 배너 첫번째 1 버전 정보 추가
DELETE FROM ref_focus_banner_info WHERE version = 1 AND banner_type = 2;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 2, 'https://bbscdn.df.nexon.com/data6/commu/202411/094bad7a-b6d9-5c78-f677-e013907b33d6.jpg', 1, '스노우 메이지 크리쳐');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 2, 'https://bbscdn.df.nexon.com/data6/commu/202411/37727be4-df26-1db9-b31c-6e36a1d1e761.jpg', 2, '이달의 아이템');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 2, 'https://bbscdn.df.nexon.com/data6/commu/202410/66deb79c-4cb3-d46d-aaa7-29360f5e1831.jpg', 3, '아라드 패스 시즌5');

-- 뉴스 포커스 배너 두번째 1 버전 정보 추가
DELETE FROM ref_focus_banner_info WHERE version = 1 AND banner_type = 3;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 3, 'https://bbscdn.df.nexon.com/data6/commu/202411/9323fca1-bdf3-793d-2be0-3ad61ef8f07f.jpg', 1, '던페 쿠폰 보상');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 3, 'https://bbscdn.df.nexon.com/data6/commu/202411/4297ef52-57ee-be59-2a83-e926ea732041.jpg', 2, '중천 발표 요약');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 3, 'https://bbscdn.df.nexon.com/data6/commu/202411/693e417e-9076-d8e8-8c77-264cc97a7404.jpg', 3, '중천 쿠폰');

-- 홈페이지 설정 값 리스트 테스트 정보 추가
DELETE FROM site_setting WHERE id IN (1, 2);
INSERT INTO site_setting (id, is_active, settings, settings_comment) VALUES(1, 1, '{"home_main_focus_ver": 1, "home_news_focus_first_ver": 1, "home_news_focus_second_ver": 1}', '설정 테스트');
INSERT INTO site_setting (id, is_active, settings, settings_comment) VALUES(2, 0, '{"home_main_focus_ver": 2, "home_news_focus_first_ver": 2, "home_news_focus_second_ver": 2}', '설정 테스트 ver.2');
