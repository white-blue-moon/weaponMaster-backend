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

-- 메인 포커스 배너 2 버전 정보 추가
DELETE FROM ref_focus_banner_info WHERE version = 2 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/22f7c302-82c5-971a-89cc-184e1b7f18ef.jpg', 1, '중천 경례');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/59a585f4-e3f4-a0f5-eebd-f0efb11797b5.jpg', 2, '던파 ON 포인트 교환소 업데이트');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/316900f6-e7fa-c9e4-0f52-456b6d2f7eaa.jpg', 3, '강남 스노우메이지');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/f1fdd3c6-d432-7132-d394-19237df3f815.jpg', 4, '2부 팬아트 콘테스트');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/b9372af9-2d9a-0681-4b11-60abe1f859eb.jpg', 5, '왕실 기사단 패키지');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/3a222bc2-c132-122a-6fad-152eed8f9c17.jpg', 6, '2025 복주머니');

-- 메인 포커스 배너 3 버전 정보 추가
DELETE FROM ref_focus_banner_info WHERE version = 3 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/a58974c1-8e62-afe6-aa58-75021f19aeba.jpg', 1, '시즌10 중천 NEW WAVE');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/8118041f-e82f-615a-e696-ddde1f0e1e9f.jpg', 2, '레어 아바타 GIFT');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/e9a1c861-0d69-0807-23e5-57196e259e88.jpg', 3, '중천 탐사 모듈');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/d38cb663-b69e-6b73-19f8-d339da1a6911.jpg', 4, '종말의 흔적 탐색');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/bbd770e5-a1e3-2854-af0e-bb9e8f7fa267.jpg', 5, '넥플과 중천 여행');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/c9147b7e-e00b-3f6c-86cb-51c32f874e8a.jpg', 6, '새내기와 겨울나기');

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

-- 퍼블리셔 로고 1 ~ 5 버전 정보 추가
DELETE FROM ref_publisher_logo WHERE version >= 1 AND version <= 5;
INSERT INTO ref_publisher_logo (version, img_url, alt) VALUES(1, 'https://rs.nxfs.nexon.com/gnb/images/logo_nexon.png', 'NEXON');
INSERT INTO ref_publisher_logo (version, img_url, alt) VALUES(2, 'https://rs.nxfs.nexon.com/bannerusr/24/3/fvfQ21143034203.gif', 'Logo');
INSERT INTO ref_publisher_logo (version, img_url, alt) VALUES(3, 'https://rs.nxfs.nexon.com/bannerusr/24/12/cb8Q30153846079.png', '추모');
INSERT INTO ref_publisher_logo (version, img_url, alt) VALUES(4, 'https://rs.nxfs.nexon.com/bannerusr/24/12/LoDB30160754039.png', '1230 애도');
INSERT INTO ref_publisher_logo (version, img_url, alt) VALUES(5, 'https://rs.nxfs.nexon.com/bannerusr/24/2/5k9s02153134458.png', '설날(2025)');

-- 점검페이지 메인 포커스 배너 1 버전 정보 추가
DELETE FROM ref_focus_banner_info WHERE version = 1 AND banner_type = 1001;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/3e0a78be-a19f-d2d3-5dbf-540aa173cab5.jpg', 1, ' ');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/eb467836-7628-ea9b-f49c-db53254e8bf4.jpg', 2, ' ');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/9641f064-a335-a7cd-df77-db9abc6ee8d1.png', 3, ' ');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/a3390e26-c053-759e-e460-486467f9f3e5.jpg', 4, ' ');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/0a3b7ce4-cb9c-1144-461d-634afa4f27c7.jpg', 5, ' ');
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/9a10acd4-d732-4002-2bec-c0a09bbfff45.jpg', 6, ' ');

-- 캐릭터 배너 1 버전 추가
DELETE FROM ref_character_banner WHERE version = 1;
INSERT INTO ref_character_banner (version, character_type, character_desc, name_img_url, thumb_img_url, banner_order) VALUES (1, 4, '격투가(여)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt04.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_04.png', 1);
INSERT INTO ref_character_banner (version, character_type, character_desc, name_img_url, thumb_img_url, banner_order) VALUES (1, 1, '귀검사(남)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt01.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_01.png', 2);

-- 캐릭터 배너 상세 정보 1 버전 추가
DELETE FROM ref_character_banner_detail WHERE version = 1 AND character_type = 4;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 4, 1, '넨마스터(여)', '넨의 힘을 활용하기 위한 수련을 한 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg14_3.jpg', '/pg/characters/cim14');
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 4, 2, '스트라이커(여)', '육체를 극한까지 단련한 정통파 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg15_3.jpg', '/pg/characters/cim15');
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 4, 3, '스트리트파이터(여)', '이기는 싸움을 추구하는 실전 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg16_3.jpg', '/pg/characters/cim16');
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 4, 4, '그래플러(여)', '잡기 기술을 극한까지 연마한 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg17_3.jpg', '/pg/characters/cim17');

DELETE FROM ref_character_banner_detail WHERE version = 1 AND character_type = 1;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 1, 1, '웨펀마스터', '귀수에 깃든 귀신을 억누르며 검술 연마에 매진하는 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg01_3.jpg', '/pg/characters/cim01');
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 1, 2, '소울브링어', '귀신과 소통함으로써 그 힘을 활용할 수 있게 된 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg02_3.jpg', '/pg/characters/cim02');
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 1, 3, '버서커', '강력한 힘을 위해 부작용을 감수하며 카잔증후군을 받아들인 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg03_3.jpg', '/pg/characters/cim03');
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 1, 4, '아수라', '파동의 힘을 느끼기 위해 스스로 시력을 포기한 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg04_3.jpg', '/pg/characters/cim04');
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES (1, 1, 5, '검귀', '원귀의 혼과 융합하여 귀신과 인간의 경계에 선 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg05_3.jpg', '/pg/characters/cim05');

-----------[ 세팅 환경에 맞게 추가해야 하는 부분 ]--------------
-- 홈페이지 설정 값 리스트 테스트 정보 추가
DELETE FROM site_setting WHERE settings_comment = "설정 테스트";
INSERT INTO site_setting (active_state, settings, settings_comment) VALUES(1, '{"publisher_logo_ver": 1, "home_main_focus_ver": 2, "home_news_focus_first_ver": 1, "home_news_focus_second_ver": 1, "inspection_main_focus_ver": 1, "character_banner_ver": 1}', '설정 테스트');

-- 슬랙봇 정보 추가
DELETE FROM slack_bot WHERE `type` = 1;
INSERT INTO slack_bot (type_comment, `type`, client_id, client_secret, redirect_uri) VALUES('(기본) 웨펀마스터-알림봇', 1, '클라이언트.ID', '클라이언트secret', '백엔드_도메인/slack/oauth/callback');

-- 관리자 슬랙 알림 정보 추가
DELETE FROM admin_slack_notice WHERE slack_bot_type = 1;
INSERT INTO admin_slack_notice (channel_comment, channel_type, slack_bot_type, slack_bot_token, slack_channel_id) VALUES('웨펀마스터 1:1 문의글 등록 알림', 1, 1, '토큰', '채널ID');
INSERT INTO admin_slack_notice (channel_comment, channel_type, slack_bot_type, slack_bot_token, slack_channel_id) VALUES('웨펀마스터 백엔드 에러 알림', 100, 1, '토큰', '채널ID');

-- 페이지 접근 제한 비밀번호 정보 추가
DELETE FROM access_gate_password WHERE `type` IN (1, 2);
INSERT INTO access_gate_password (`type`, password) VALUES(1, '6자리_비밀번호');
INSERT INTO access_gate_password (`type`, password) VALUES(2, '6자리_비밀번호');
