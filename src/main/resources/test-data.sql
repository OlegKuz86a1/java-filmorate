INSERT INTO film_rating (name)
VALUES ('G');
INSERT INTO film_rating (name)
VALUES ('PG');
INSERT INTO film_rating (name)
VALUES ('PG-13');
INSERT INTO film_rating (name)
VALUES ('R');
INSERT INTO film_rating (name)
VALUES ('NC-17');

INSERT INTO genre (name_genre)
VALUES ('Комедия');
INSERT INTO genre (name_genre)
VALUES ('Драма');
INSERT INTO genre (name_genre)
VALUES ('Мультфильм');
INSERT INTO genre (name_genre)
VALUES ('Триллер');
INSERT INTO genre (name_genre)
VALUES ('Документальный');
INSERT INTO genre (name_genre)
VALUES ('Боевик');

INSERT INTO users (name, email, login, birthday)
VALUES ('User_andrew', 'andrew@yandex.ru', 'andrew', '1990-01-01');
INSERT INTO users (name, email, login, birthday)
VALUES ('User_petr', 'petr@yandex.ru', 'petr', '1993-03-13');
INSERT INTO users (name, email, login, birthday)
VALUES ('User_ivan', 'ivan@yandex.ru', 'ivan', '1992-02-02');
INSERT INTO users (name, email, login, birthday)
VALUES ('User_gena', 'gena@yandex.ru', 'gena', '1999-09-19');
INSERT INTO users (name, email, login, birthday)
VALUES ('User_drani', 'drani@yandex.ru', 'drani', '2000-11-11');

INSERT INTO films (film_name, description, release_date, duration, rating_id)
VALUES ('comedy', 'comedy description', '2022-01-01', 120, 5);
INSERT INTO films (film_name, description, release_date, duration, rating_id)
VALUES ('drama', 'drama description', '2022-02-02', 90, 4);
INSERT INTO films (film_name, description, release_date, duration, rating_id)
VALUES ('thriller', 'thriller description', '2022-03-03', 150, 3);
INSERT INTO films (film_name, description, release_date, duration, rating_id)
VALUES ('arena', 'arena description', '2022-04-04', 120, 2);
INSERT INTO films (film_name, description, release_date, duration, rating_id)
VALUES ('Put', 'put description', '2022-05-05', 70, 1);

INSERT INTO film_genre (film_id, genre_id)
VALUES (1, 1);
INSERT INTO film_genre (film_id, genre_id)
VALUES (1, 2);
INSERT INTO film_genre (film_id, genre_id)
VALUES (1, 4);
INSERT INTO film_genre (film_id, genre_id)
VALUES (3, 3);
INSERT INTO film_genre (film_id, genre_id)
VALUES (3, 6);
INSERT INTO film_genre (film_id, genre_id)
VALUES (4, 1);
INSERT INTO film_genre (film_id, genre_id)
VALUES (3, 2);
INSERT INTO film_genre (film_id, genre_id)
VALUES (5, 5);
