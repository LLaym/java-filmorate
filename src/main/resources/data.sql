-- TRUNCATE TABLE mpa_ratings;
-- TRUNCATE TABLE users;
-- TRUNCATE TABLE friendships;
-- TRUNCATE TABLE films;
-- TRUNCATE TABLE film_like;
-- TRUNCATE TABLE genres;
-- TRUNCATE TABLE film_genre;

INSERT INTO mpas (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO genres (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');