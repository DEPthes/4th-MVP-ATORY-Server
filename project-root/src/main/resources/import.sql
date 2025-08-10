-- 유저 (갤러리 소유자)  ※ 엔티티가 @Table(name="users") 여야 함
INSERT INTO users (id, username, email, is_social, is_profile_completed, profile_img_url)
VALUES (1001, '갤러리유저', 'gallery@test.com', false, false, 'https://cdn.atory.app/defaults/profile.png');

-- 갤러리
INSERT INTO gallery (id, name, location, registration_number, user_id)
VALUES (1, '테스트 갤러리', '서울 종로구', 'REG-001', 1001);

-- 전시(POST)
INSERT INTO post (id, user_id, name, imageurl, exhibitionurl, description, post_type) VALUES
(2001, 1001, '전시 A', 'https://picsum.photos/seed/a/600/400', NULL, '설명 A', 'EXHIBITION'),
(2002, 1001, '전시 B', 'https://picsum.photos/seed/b/600/400', NULL, '설명 B', 'EXHIBITION');

-- 태그 (H2 전용 MERGE)
MERGE INTO tag KEY(id) VALUES (3001, '현대미술');
MERGE INTO tag KEY(id) VALUES (3002, '회화');

-- 게시글-태그 매핑
INSERT INTO tag_post (post_id, tag_id) VALUES (2001, 3001);
INSERT INTO tag_post (post_id, tag_id) VALUES (2002, 3002);
