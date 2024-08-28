DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS film_rating;
DROP TABLE IF EXISTS genre;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(30),
    login VARCHAR(30) NOT NULL,
    name VARCHAR(30),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS film_rating (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id INT PRIMARY KEY AUTO_INCREMENT,
    name_genre VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    film_name VARCHAR(30),
    description VARCHAR(256),
    release_date DATE,
    duration INT,
    rating_id INT,
    FOREIGN KEY (rating_id) REFERENCES film_rating (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT,
    genre_id INT,
    FOREIGN KEY (film_id)  REFERENCES films (film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id)  REFERENCES genre (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT,
    friend_id BIGINT,
    is_accept BOOLEAN,
    FOREIGN KEY (user_id)  REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id)  REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT,
    film_id BIGINT,
    FOREIGN KEY (user_id)  REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id)  REFERENCES films (film_id) ON DELETE CASCADE
);