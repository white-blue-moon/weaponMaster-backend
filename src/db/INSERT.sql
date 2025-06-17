-- 명시적으로 .sql 파일 문자셋 설정 (한글 깨짐 방지)
SET NAMES 'utf8';

-- 홈 > 메인포커스 배너 (버전 1)
DELETE FROM ref_focus_banner_info WHERE version = 1 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/0a3de54d-8ab7-0919-3ef8-266c04a409e4.jpg', 1, '빙결사 PC방'),
(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/ce8502c3-776d-0d74-3f57-0b919abcf14b.jpg', 2, '중천 : 신비로운 세계의 여정'),
(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/8d466130-c079-f15b-2c1a-2233b298cfde.jpg', 3, '던파 ON 리뉴얼'),
(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/05243a37-826d-0ef2-e2b5-9b6d8fe4de1a.jpg', 4, '겨울 방학 생활'),
(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/cc55693a-3e3c-3310-c5fb-d160747652cd.jpg', 5, '스노우 메이지 추억 앨범'),
(1, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/13a64cd4-684e-77f8-4799-f8fcd2330326.jpg', 6, '스노우 메이지 스펙업');

-- 홈 > 메인포커스 배너 (버전 2)
DELETE FROM ref_focus_banner_info WHERE version = 2 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/22f7c302-82c5-971a-89cc-184e1b7f18ef.jpg', 1, '중천 : 새로운 시작을 알리는 인사'),
(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/59a585f4-e3f4-a0f5-eebd-f0efb11797b5.jpg', 2, '던파 ON 포인트 교환소 업데이트'),
(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/316900f6-e7fa-c9e4-0f52-456b6d2f7eaa.jpg', 3, '강남 스노우메이지'),
(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/b9372af9-2d9a-0681-4b11-60abe1f859eb.jpg', 4, '왕실 기사단 패키지'),
(2, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/3a222bc2-c132-122a-6fad-152eed8f9c17.jpg', 5, '2025 복주머니');

-- 홈 > 메인포커스 배너 (버전 3)
DELETE FROM ref_focus_banner_info WHERE version = 3 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/a58974c1-8e62-afe6-aa58-75021f19aeba.jpg', 1, '시즌10 중천 NEW WAVE'),
(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202411/8118041f-e82f-615a-e696-ddde1f0e1e9f.jpg', 2, '레어 아바타 GIFT'),
(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/e9a1c861-0d69-0807-23e5-57196e259e88.jpg', 3, '중천 탐사 모듈'),
(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/d38cb663-b69e-6b73-19f8-d339da1a6911.jpg', 4, '종말의 흔적 탐색'),
(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/bbd770e5-a1e3-2854-af0e-bb9e8f7fa267.jpg', 5, '넥플과 중천 여행'),
(3, 1, 'https://bbscdn.df.nexon.com/data6/commu/202501/c9147b7e-e00b-3f6c-86cb-51c32f874e8a.jpg', 6, '새내기와 겨울나기');

-- 홈 > 메인포커스 배너 (버전 4)
DELETE FROM ref_focus_banner_info WHERE version = 4 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(4, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/17b2d441-d5da-1694-5b36-f79678304376.jpg', 1, '쏟아지는 햇살과 함께하는 PC방'),
(4, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/f8a6cea0-add0-e696-0f83-0868533e201a.jpg', 2, '디그밍의 여름을 부탁해!'),
(4, 1, 'https://bbscdn.df.nexon.com/data6/commu/202505/b97fddd8-189e-a14a-432f-47af833ef0d7.jpg', 3, '스노우 메이지 롯데타워'),
(4, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/1239a5e6-ce08-40bd-b2d0-4caa8cc19a10.jpg', 4, '뉴 전직! 핫 썸머!');

-- 홈 > 메인포커스 배너 (버전 5)
DELETE FROM ref_focus_banner_info WHERE version = 5 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(5, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/b2585588-da7d-a2a0-4262-ef81390e0727.jpg', 1, '시즌 10 Act 3. 스킬 개화 : 피어나는 칼날'),
(5, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/2ed3d3d6-1bb1-a0c3-6a53-eeb4077e4a36.jpg', 2, '여름맞이 안전 증폭 지원'),
(5, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/433e1fd2-dc3c-8132-be94-34a504243dd6.jpg', 3, '던파ON 포인트 부스트 이벤트'),
(5, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/1125a3da-e2c4-2b61-47a6-9a2b05a30837.jpg', 4, '진 패러메딕의 서 & 진 키메라의 서 아바타 콤보 상자'),
(5, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/e3e56931-1879-802c-d4e8-823ec0c07c58.jpg', 5, '카메린의 황금 성장 캡슐 패키지');

-- 홈 > 메인포커스 배너 (버전 6)
DELETE FROM ref_focus_banner_info WHERE version = 6 AND banner_type = 1;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(6, 1, 'https://bbscdn.df.nexon.com/data6/commu/202506/1e4551a3-1ef3-dd9d-5944-4b5a5cd64787.jpg', 1, '패러메딕/키메라 미니콘테스트'),
(6, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/f1fdd3c6-d432-7132-d394-19237df3f815.jpg', 2, '2부 팬아트 콘테스트'),
(6, 1, 'https://bbscdn.df.nexon.com/data6/commu/202412/bebf85e9-c22d-e4b1-938e-3e862ebb1ad3.jpg', 3, '아트 공모전 : 창의력 가득한 작품들'),
(6, 1, 'https://bbscdn.df.nexon.com/data6/commu/202505/a7b9aac7-a5a1-d257-713d-9fbc24e0bb36.png', 4, '2025 던파로 ON SPECIAL GIFT');


-- 홈 > 뉴스 포커스 배너 1st (버전 1)
DELETE FROM ref_focus_banner_info WHERE version = 1 AND banner_type = 2;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(1, 2, 'https://bbscdn.df.nexon.com/data6/commu/202411/094bad7a-b6d9-5c78-f677-e013907b33d6.jpg', 1, '스노우 메이지 크리쳐'),
(1, 2, 'https://bbscdn.df.nexon.com/data6/commu/202411/37727be4-df26-1db9-b31c-6e36a1d1e761.jpg', 2, '이달의 아이템'),
(1, 2, 'https://bbscdn.df.nexon.com/data6/commu/202410/66deb79c-4cb3-d46d-aaa7-29360f5e1831.jpg', 3, '아라드 패스 시즌5');

-- 홈 > 뉴스 포커스 배너 1st (버전 2)
DELETE FROM ref_focus_banner_info WHERE version = 2 AND banner_type = 2;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(2, 2, 'https://bbscdn.df.nexon.com/data6/commu/202506/f8a028fe-d057-ab4a-e380-6c3439016f35.jpg', 1, '해군 제독 아바타 콤보 상자'),
(2, 2, 'https://bbscdn.df.nexon.com/data6/commu/202506/2a5e5e4d-5b79-c176-2fab-b7e64ef7a405.png', 2, '차원 여행 아바타 콤보 상자'),
(2, 2, 'https://bbscdn.df.nexon.com/data6/commu/202505/972845d8-14e7-2565-aeb7-2d96fc4084b5.jpg', 3, '6월 이달의 아이템');

-- 홈 > 뉴스 포커스 배너 1st (버전 3)
DELETE FROM ref_focus_banner_info WHERE version = 3 AND banner_type = 2;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(3, 2, 'https://bbscdn.df.nexon.com/data6/commu/202505/03e04d20-3d4d-47d7-851b-bae19b8b4727.jpg', 1, '마일리지샵 2025 시즌5'),
(3, 2, 'https://bbscdn.df.nexon.com/data6/commu/202505/dddeb800-87e7-e1d5-e5b4-131b82c9ca0c.jpg', 2, '아라드 패스 2025 시즌3');

-- 홈 > 뉴스 포커스 배너 2nd (버전 1)
DELETE FROM ref_focus_banner_info WHERE version = 1 AND banner_type = 3;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(1, 3, 'https://bbscdn.df.nexon.com/data6/commu/202411/9323fca1-bdf3-793d-2be0-3ad61ef8f07f.jpg', 1, '던페 쿠폰 보상'),
(1, 3, 'https://bbscdn.df.nexon.com/data6/commu/202411/4297ef52-57ee-be59-2a83-e926ea732041.jpg', 2, '중천 발표 요약'),
(1, 3, 'https://bbscdn.df.nexon.com/data6/commu/202411/693e417e-9076-d8e8-8c77-264cc97a7404.jpg', 3, '중천 쿠폰');

-- 홈 > 뉴스 포커스 배너 2nd (버전 2)
DELETE FROM ref_focus_banner_info WHERE version = 2 AND banner_type = 3;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(2, 3, 'https://bbscdn.df.nexon.com/data6/commu/202506/6a759fe8-c4cc-abe6-6e34-85ccc28b82c7.jpg', 1, '굿즈 제휴 할인'),
(2, 3, 'https://bbscdn.df.nexon.com/data6/commu/202506/7650be64-121c-3eb7-0b97-db5886cef2ba.jpg', 2, '신규 전직 공략 이벤트'),
(2, 3, 'https://bbscdn.df.nexon.com/data6/commu/202506/f928fe9f-abb1-69a3-c107-55b2123256c1.jpg', 3, '레바 중천 Comics 4화');

-- 홈 > 뉴스 포커스 배너 2nd (버전 3)
DELETE FROM ref_focus_banner_info WHERE version = 3 AND banner_type = 3;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(3, 3, 'https://bbscdn.df.nexon.com/data6/commu/202505/92f5caf6-535c-3349-61fc-d9fa7643d47e.jpg', 1, '스노우 메이지 롯데타워'),
(3, 3, 'https://bbscdn.df.nexon.com/data6/commu/202502/390d3199-1f9c-93c5-de5f-680a002cd553.jpg', 2, '달빛 탁자'),
(3, 3, 'https://bbscdn.df.nexon.com/data6/commu/202505/13db9cdc-11c4-a588-d6b4-94bf289ce8ae.jpg', 3, '스노우 메이지 쿠폰');


-- 퍼블리셔 로고 1 ~ 3 버전 정보 추가
DELETE FROM ref_publisher_logo WHERE version >= 1 AND version <= 3;
INSERT INTO ref_publisher_logo (version, img_url, alt) VALUES
(1, 'https://cdn.jsdelivr.net/gh/white-blue-moon/portfolio-assets/weapon-master/assets/publisher-logo/logo_blue_moon.png', 'BLUE MOON'),
(2, 'https://cdn.jsdelivr.net/gh/white-blue-moon/portfolio-assets/weapon-master/assets/publisher-logo/logo_blue_moon_spring2025.png', '2025 봄'),
(3, 'https://cdn.jsdelivr.net/gh/white-blue-moon/portfolio-assets/weapon-master/assets/publisher-logo/logo_blue_moon_children_day_2025.png', '2025 어린이날');


-- 점검 > 메인 포커스 배너 (버전 1)
DELETE FROM ref_focus_banner_info WHERE version = 1 AND banner_type = 1001;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/3e0a78be-a19f-d2d3-5dbf-540aa173cab5.jpg', 1, '겨울 PC방'),
(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/eb467836-7628-ea9b-f49c-db53254e8bf4.jpg', 2, '1월 이달의 아이템'),
(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/9641f064-a335-a7cd-df77-db9abc6ee8d1.png', 3, '마일리지샵 2025 시즌 1'),
(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/a3390e26-c053-759e-e460-486467f9f3e5.jpg', 4, '중천 방어 이벤트'),
(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/0a3b7ce4-cb9c-1144-461d-634afa4f27c7.jpg', 5, '던파ON 눈사람 아바타'),
(1, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202412/9a10acd4-d732-4002-2bec-c0a09bbfff45.jpg', 6, '겨울 폰트 크리쳐 오라');

-- 점검 > 메인 포커스 배너 (버전 2)
DELETE FROM ref_focus_banner_info WHERE version = 2 AND banner_type = 1001;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(2, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202506/e66bdc30-9c1c-ce72-f563-fbd29e204fe2.jpg', 1, '넥슨 캐시 20,000'),
(2, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202506/6c6d5ea6-1c6f-f183-e77f-088004e8cb7c.jpg', 2, '디그밍 시원한 썸머'),
(2, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202506/5d83dbb5-82d5-772c-333c-5972820318b0.png', 3, '여름맞이 안전 증폭 지원'),
(2, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202506/610f8c12-e33c-3c95-55f2-197fe178c495.jpg', 4, '던파ON 포인트 부스트'),
(2, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202506/5c1982c1-4695-ce50-30b0-68a4e997268a.jpg', 5, '진 각성의 서(패러메딕, 키메라)');

-- 점검 > 메인 포커스 배너 (버전 3)
DELETE FROM ref_focus_banner_info WHERE version = 3 AND banner_type = 1001;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(3, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202506/588ebc7c-89b4-5ab9-5f81-d687f3f9b869.jpg', 1, '스킬 개화'),
(3, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202506/29c42126-379a-d15c-5037-575de53589db.jpg', 2, '2025 아라드 패스 아바타'),
(3, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202505/473d91cd-49be-260d-8a67-8bc621157f67.jpg', 3, '마일리지샵 2025 시즌 5'),
(3, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202505/4b93f2b1-6cbc-ca55-797c-5cbedcf2dbb7.jpg', 4, '플래티넘 엠블렘');

-- 점검 > 메인 포커스 배너 (버전 4)
DELETE FROM ref_focus_banner_info WHERE version = 4 AND banner_type = 1001;
INSERT INTO ref_focus_banner_info (version, banner_type, img_url, img_order, img_comment) VALUES
(4, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202506/2cf75d6a-e9ef-8964-a1a5-db57ee12c0e2.jpg', 1, '패러메딕&키메라 미니 콘테스트'),
(4, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202505/62434d95-212a-ba86-5899-e07543f0a538.jpg', 2, '2025 던파로 ON SPECIAL GIFT'),
(4, 1001, 'https://bbscdn.df.nexon.com/data6/commu/202505/7d4eeb52-8d22-0a11-09ad-be886d93bdcc.jpg', 3, '스노우 메이지 롯데타워');


-- 홈 > 캐릭터 배너 (버전 1)
DELETE FROM ref_character_banner WHERE version = 1;
INSERT INTO ref_character_banner (version, character_type, character_desc, name_img_url, thumb_img_url, banner_order) VALUES
(1, 4, '격투가(여)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt04.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_04.png', 1),
(1, 3, '격투가(남)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt03.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_03.png', 2),
(1, 2, '귀검사(여)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt02.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_02.png', 3),
(1, 1, '귀검사(남)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt01.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_01.png', 4);

DELETE FROM ref_character_banner_detail WHERE version = 1 AND character_type = 4;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(1, 4, 1, '넨마스터(여)', '넨의 힘을 활용하기 위한 수련을 한 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg14_3.jpg', '/pg/characters/cim14'),
(1, 4, 2, '스트라이커(여)', '육체를 극한까지 단련한 정통파 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg15_3.jpg', '/pg/characters/cim15'),
(1, 4, 3, '스트리트파이터(여)', '이기는 싸움을 추구하는 실전 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg16_3.jpg', '/pg/characters/cim16'),
(1, 4, 4, '그래플러(여)', '잡기 기술을 극한까지 연마한 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg17_3.jpg', '/pg/characters/cim17');

DELETE FROM ref_character_banner_detail WHERE version = 1 AND character_type = 3;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(1, 3, 1, '넨마스터(남)', '넨의 힘을 육체에 접목한 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg10_3.jpg', '/pg/characters/cim10'),
(1, 3, 2, '스트라이커(남)', '육체를 극한까지 단련한 정통파 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg11_3.jpg', '/pg/characters/cim11'),
(1, 3, 3, '스트리트파이터(남)', '이기는 싸움을 추구하는 실전 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg12_3.jpg', '/pg/characters/cim12'),
(1, 3, 4, '그래플러(남)', '타격에 잡기 기술을 접목한 격투가', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg13_3.jpg', '/pg/characters/cim13');

DELETE FROM ref_character_banner_detail WHERE version = 1 AND character_type = 2;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(1, 2, 1, '소드마스터', '마법을 검술에 접목시켜 사용하는 마검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg06_3.jpg', '/pg/characters/cim06'),
(1, 2, 2, '데몬슬레이어', '마인의 힘을 활용할 수 있게 된 마검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg07_3.jpg', '/pg/characters/cim07'),
(1, 2, 3, '베가본드', '마수에 깃든 마인의 힘을 억누르며 내공을 수련하는 마검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg08_3.jpg', '/pg/characters/cim08'),
(1, 2, 4, '다크템플러', '죽은 자의 신 우시르를 섬기는 마검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg09_3.jpg', '/pg/characters/cim09'),
(1, 2, 5, '블레이드', '비전의 납도술을 구사하는 마검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg62_3.jpg', '/pg/characters/cim62');

DELETE FROM ref_character_banner_detail WHERE version = 1 AND character_type = 1;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(1, 1, 1, '웨펀마스터', '귀수에 깃든 귀신을 억누르며 검술 연마에 매진하는 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg01_3.jpg', '/pg/characters/cim01'),
(1, 1, 2, '소울브링어', '귀신과 소통함으로써 그 힘을 활용할 수 있게 된 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg02_3.jpg', '/pg/characters/cim02'),
(1, 1, 3, '버서커', '강력한 힘을 위해 부작용을 감수하며 카잔증후군을 받아들인 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg03_3.jpg', '/pg/characters/cim03'),
(1, 1, 4, '아수라', '파동의 힘을 느끼기 위해 스스로 시력을 포기한 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg04_3.jpg', '/pg/characters/cim04'),
(1, 1, 5, '검귀', '원귀의 혼과 융합하여 귀신과 인간의 경계에 선 귀검사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg05_3.jpg', '/pg/characters/cim05');

-- 홈 > 캐릭터 배너 (버전 2)
DELETE FROM ref_character_banner WHERE version = 2;
INSERT INTO ref_character_banner (version, character_type, character_desc, name_img_url, thumb_img_url, banner_order) VALUES
(2, 17, '아처', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt17.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_16.png', 1),
(2, 6, '거너(여)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt06.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_06.png', 2),
(2, 5, '거너(남)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt05.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_05.png', 3),
(2, 8, '마법사(여)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt08.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_08.png', 4),
(2, 7, '마법사(남)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt07.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_07.png', 5);

DELETE FROM ref_character_banner_detail WHERE version = 2 AND character_type = 17;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(2, 17, 1, '뮤즈', '선현궁 연주로 아군의 사기를 북돋우는 궁수', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg64_3.jpg', '/pg/characters/cim64'),
(2, 17, 2, '트래블러', '자유 여행자 조합의 신비한 도구를 사용하는 궁수', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg65_3.jpg', '/pg/characters/cim65'),
(2, 17, 3, '헌터', '크로스슈터로 요수와 적들을 사냥하는 궁수', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg66_3.jpg', '/pg/characters/cim66'),
(2, 17, 4, '비질란테', '요기를 받아들여 요수화가 가능해진 궁수', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg67_3.jpg', '/pg/characters/cim67'),
(2, 17, 5, '키메라', '투척 무기와 유물을 활용해 적들을 유린하는 괴짜', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg68_3.jpg', '/pg/characters/cim68');

DELETE FROM ref_character_banner_detail WHERE version = 2 AND character_type = 6;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(2, 6, 1, '레인저(여)', '리볼버 사격과 리볼버 하단에 장착된 건블레이드를 활용한 체술이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg22_3.jpg', '/pg/characters/cim22'),
(2, 6, 2, '런처(여)', '중화기를 이용한 원거리 공격이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg23_3.jpg', '/pg/characters/cim23'),
(2, 6, 3, '메카닉(여)', '로봇과 기계장치를 활용한 공격이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg24_3.jpg', '/pg/characters/cim24'),
(2, 6, 4, '스핏파이어(여)', '다양한 화기를 활용한 공격이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg25_3.jpg', '/pg/characters/cim25'),
(2, 6, 5, '패러메딕', '화력 지원 및 피해 방지가 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg69_3.jpg', '/pg/characters/cim69');

DELETE FROM ref_character_banner_detail WHERE version = 2 AND character_type = 5;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(2, 5, 1, '레인저(남)', '리볼버를 이용한 빠른 사격과 체술이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg18_3.jpg', '/pg/characters/cim18'),
(2, 5, 2, '런처(남)', '중화기를 이용한 원거리 공격이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg19_3.jpg', '/pg/characters/cim19'),
(2, 5, 3, '메카닉(남)', '로봇과 기계장치를 활용한 공격이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg20_3.jpg', '/pg/characters/cim20'),
(2, 5, 4, '스핏파이어(남)', '다양한 화기를 활용한 공격이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg21_3.jpg', '/pg/characters/cim21'),
(2, 5, 5, '어썰트(남)', '개조된 신체와 무기를 활용한 공격이 특기인 거너', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg63_3.jpg', '/pg/characters/cim63');

DELETE FROM ref_character_banner_detail WHERE version = 2 AND character_type = 8;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(2, 8, 1, '엘레멘탈 마스터', '4가지 속성의 원소 마법을 고르게 연구한 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg31_3.jpg', '/pg/characters/cim31'),
(2, 8, 2, '소환사', '소환 마법을 극한까지 연구한 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg32_3.jpg', '/pg/characters/cim32'),
(2, 8, 3, '배틀메이지', '원소의 힘을 응축한 체이서를 이용하는 근접전 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg33_3.jpg', '/pg/characters/cim33'),
(2, 8, 4, '마도학자', '원소의 힘을 다양한 형태로 변환하는 마도학에 집중한 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg34_3.jpg', '/pg/characters/cim34'),
(2, 8, 5, '인챈트리스', '저주와 인형술을 사용하는 변덕쟁이 마녀', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg35_3.jpg', '/pg/characters/cim35');

DELETE FROM ref_character_banner_detail WHERE version = 2 AND character_type = 7;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(2, 7, 1, '엘레멘탈 바머', '어비스의 힘을 빌려 원소 마법의 힘을 극대화 시킨 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg26_3.jpg', '/pg/characters/cim26'),
(2, 7, 2, '빙결사', '수속성 마법을 극한까지 연구한 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg27_3.jpg', '/pg/characters/cim27'),
(2, 7, 3, '블러드 메이지', '혈기 그 자체를 힘의 근원으로 삼은 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg28_3.jpg', '/pg/characters/cim28'),
(2, 7, 4, '스위프트 마스터', '바람의 힘을 자유자재로 다루는 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg29_3.jpg', '/pg/characters/cim29'),
(2, 7, 5, '디멘션워커', '차원 저편의 비밀을 연구하는 마법사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg30_3.jpg', '/pg/characters/cim30');

-- 홈 > 캐릭터 배너 (버전 3)
DELETE FROM ref_character_banner WHERE version = 3;
INSERT INTO ref_character_banner (version, character_type, character_desc, name_img_url, thumb_img_url, banner_order) VALUES
(3, 10, '크루세이더(여)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt10.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_10.png', 1),
(3, 9, '크루세이더(남)', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt09.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_09.png', 2),
(3, 13, '마창사', 'https://bbscdn.df.nexon.com/pg/characters/img/main/mc_txt13.png', 'https://bbscdn.df.nexon.com/pg/characters/img/thum/thum_char_13.png', 3);

DELETE FROM ref_character_banner_detail WHERE version = 3 AND character_type = 13;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(3, 13, 1, '뱅가드', '늘 전투의 최전방에서 다수의 적과 싸우며 파괴하는 마창사', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg52_3.jpg', '/pg/characters/cim52'),
(3, 13, 2, '듀얼리스트', '마창의 힘을 대인 전투에 특화시킨 창술의 달인', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg53_3.jpg', '/pg/characters/cim53'),
(3, 13, 3, '드래고니안 랜서', '서번트 랜스를 활용하여 마수를 사냥하는 마수 사냥꾼', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg54_3.jpg', '/pg/characters/cim54'),
(3, 13, 4, '리퍼', '어둠의 힘을 다루는 강력한 암살자', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg55_3.jpg', '/pg/characters/cim55');

DELETE FROM ref_character_banner_detail WHERE version = 3 AND character_type = 10;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(3, 10, 1, '크루세이더(여)', '신성한 빛의 힘으로 적을 섬멸하고 아군을 지원하는 프리스트', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg40_3.jpg', '/pg/characters/cim40'),
(3, 10, 2, '이단심판관', '교단의 율법에 따라 적을 심판하는 잔혹한 성직자', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg41_3.jpg', '/pg/characters/cim41'),
(3, 10, 3, '무녀', '염주와 부적, 그리고 신룡의 힘으로 마물을 퇴치하는 프리스트', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg42_3.jpg', '/pg/characters/cim42'),
(3, 10, 4, '미스트리스', '원죄의 힘으로 악을 유혹하여 처단하는 프리스트', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg43_3.jpg', '/pg/characters/cim43');

DELETE FROM ref_character_banner_detail WHERE version = 3 AND character_type = 9;
INSERT INTO ref_character_banner_detail (version, character_type, character_detail_type, character_name, character_intro, img_url, homepage_link_url) VALUES
(3, 9, 1, '크루세이더(남)', '신의 가호를 받아 강력한 신성력을 가지게 된 프리스트', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg36_3.jpg', '/pg/characters/cim36'),
(3, 9, 2, '인파이터', '악을 섬멸하기 위해 권격을 극한까지 수련한 프리스트', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg37_3.jpg', '/pg/characters/cim37'),
(3, 9, 3, '퇴마사', '주술과 거병을 활용하여 마물을 퇴치하는 프리스트', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg38_3.jpg', '/pg/characters/cim38'),
(3, 9, 4, '어벤저', '악을 섬멸하기 위해 악마의 힘을 받아들인 프리스트', 'https://bbscdn.df.nexon.com/pg/characters/img/bg/bg39_3.jpg', '/pg/characters/cim39');


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


-----------[ DB 데이터로 게시물 내용을 추가해야 하는 부분 ]--------------

