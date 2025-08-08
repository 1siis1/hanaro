-- 관리자 계정은 요구사항에 명시된 hanaro/12345678
INSERT INTO member (email, password, nickname, role)
VALUES ('hanaro', '{bcrypt}$2a$10$T2J9sD1d.f/lX.iJ5.FLjeLp3u3sXv9y8prE5MvMhGipbPsO8j9eS', '관리자', 'ADMIN');
