-- 관리자 계정은 hanaro@admin.com/12345678
INSERT INTO member (email, password, nickname, role)
VALUES ('hanaro@admin.com', '$2a$10$gdvGsh/6zhBXMJ.QHBotLek/pwP9hY8OqhGgJh.3hHIgGHrvUsbeq', '관리자', 'ADMIN'),
       ('tester@gmail.com', '$2a$10$gdvGsh/6zhBXMJ.QHBotLek/pwP9hY8OqhGgJh.3hHIgGHrvUsbeq', '일반회원', 'USER');


insert into item(price, stock, itemName)
values (40000, 10, '아이템1'), (10000, 20, '아이템2'), (500, 15, '아이템3');

INSERT INTO cart (member, createdAt, updatedAt) VALUES (2, NOW(), NOW());


