CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS friendships (
    user_id1 BIGINT NOT NULL,
    user_id2 BIGINT NOT NULL,
    confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (user_id1, user_id2),
    FOREIGN KEY (user_id1) REFERENCES users(user_id),
    FOREIGN KEY (user_id2) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
