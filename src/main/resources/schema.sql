CREATE TABLE IF NOT EXISTS users
(
    id       int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    varchar(40) NOT NULL,
    login    varchar(20) NOT NULL,
    name     varchar(20) NOT NULL,
    birthday date
);

CREATE TABLE IF NOT EXISTS friendships
(
    id             int GENERATED BY DEFAULT AS IDENTITY UNIQUE,
    first_user_id  int     NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    second_user_id int     NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status         boolean NOT NULL DEFAULT false,
    PRIMARY KEY (first_user_id, second_user_id)
);

CREATE TABLE IF NOT EXISTS mpa_ratings
(
    id   int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    id            int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          varchar(70) NOT NULL,
    description   varchar(200),
    release_date  date        NOT NULL,
    duration      int         NOT NULL,
    mpa_rating_id int REFERENCES mpa_ratings (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS film_like
(
    id      int GENERATED BY DEFAULT AS IDENTITY UNIQUE,
    film_id int NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    user_id int NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres
(
    id   int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genre
(
    id       int GENERATED BY DEFAULT AS IDENTITY UNIQUE,
    film_id  int NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    genre_id int NOT NULL REFERENCES genres (id) ON DELETE CASCADE
);