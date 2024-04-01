# java-filmorate
Template repository for Filmorate project.


SELECT f.name, f.description, f.release_date, f.duration, m.name AS mpa_rating
FROM films f
JOIN mpa m ON f.mpa_id = m.mpa_id
WHERE f.name LIKE '%название фильма%';

SELECT u2.user_id, u2.name, u2.email
FROM friendships f
JOIN users u1 ON f.user_id = u1.user_id
JOIN users u2 ON f.friend_id = u2.user_id
WHERE u1.name = 'имя пользователя';

SELECT fl.name, fl.description
FROM likes l
JOIN films fl ON l.film_id = fl.film_id
WHERE l.user_id = (SELECT user_id FROM users WHERE name = 'имя пользователя');
![Untitled diagram-2024-04-01-202716](https://github.com/OlegSavinIG/java-filmorate/assets/134596179/66d8aea7-a253-4925-bcef-e6dc08b97af3)
