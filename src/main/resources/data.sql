-- Очистка существующих таблиц

-- Создание таблицы для рейтингов MPA
CREATE TABLE mpa_ratings (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы для жанров
CREATE TABLE genres (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы пользователей
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    birthday DATE NOT NULL
);

-- Создание таблицы фильмов
CREATE TABLE films (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    mpa_rating_id INT,
    FOREIGN KEY (mpa_rating_id) REFERENCES mpa_ratings(id)
);

-- Создание таблицы для связи фильмов и жанров
CREATE TABLE film_genres (
    film_id INT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);

-- Создание таблицы для отношений дружбы между пользователями
CREATE TABLE user_friends (
    user_id INT,
    friend_id INT,
    status INT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id)
);

-- Заполнение таблицы рейтингов MPA
INSERT INTO mpa_ratings (id, name) VALUES
(1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');

-- Заполнение таблицы жанров
INSERT INTO genres (id, name) VALUES
(1, 'Comedy'), (2, 'Drama'), (3, 'Thriller'), (4, 'Action');
