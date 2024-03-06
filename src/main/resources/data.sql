-- Добавление пользователей
INSERT INTO users (email, login, name, birthday) VALUES
('user1@example.com', 'user1', 'User One', '1990-01-01'),
('user2@example.com', 'user2', 'User Two', '1992-02-02');

-- Добавление фильмов
INSERT INTO films (name, description, release_date, duration) VALUES
('Film One', 'Description of Film One', '2020-01-01', 120),
('Film Two', 'Description of Film Two', '2021-02-02', 100);

-- Добавление отношений дружбы
INSERT INTO friendships (user_id1, user_id2, confirmed) VALUES
(1, 2, TRUE);

-- Добавление лайков к фильмам
INSERT INTO film_likes (film_id, user_id) VALUES
(1, 1),
(2, 2);
