drop table if exists friendships;
drop table if exists likes;
drop table if exists users;
drop table if exists film_genres;
drop table if exists films;

create table if not exists users (
    user_id BIGINT  auto_increment primary key ,
    email VARCHAR(255) not null ,
    login VARCHAR(255) not null ,
    name VARCHAR(255),
    birthday DATE not null
);
create table if not exists genres (
    genre_id INT not null primary key,
    name VARCHAR(64) not null
);
create table if not exists mpa (
    mpa_id INT not null primary key,
    name VARCHAR(64) not null
);

create table if not exists films (
    film_id BIGINT  auto_increment primary key ,
    name VARCHAR(255) not null,
    description VARCHAR(255),
    release_date DATE not null,
    duration INT not null ,
    rate BIGINT,
    mpa_id INT  references mpa(mpa_id)
);

create table if not exists friendships (
    user_id BIGINT  not null,
    friend_id BIGINT  not null,
    primary key (user_id, friend_id),
    foreign key (user_id) references users(user_id),
    foreign key (friend_id) references users(user_id)
);

create table if not exists likes (
    film_id BIGINT  not null,
    user_id BIGINT  not null,
    primary key (film_id, user_id),
    foreign key (film_id) references films(film_id),
    foreign key (user_id) references users(user_id)
);
create table if not exists film_genres (
    film_id BIGINT not null,
    genre_id INT not null,
    primary key (film_id, genre_id),
    foreign key (film_id) references films(film_id),
    foreign key (genre_id) references genres(genre_id)
);


