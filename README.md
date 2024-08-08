# java-filmorate
Template repository for Filmorate project.  
Scheme DB
![scheme DB](src/main/resources/scheme.png)  
Описание  взаимо связи с таблицами.  
Table films {  
film_id integer [primary key]  
film_name varchar  
description varchar  
release_date date  
duration integer  
rating varchar  
}

Table genre {  
genre_id integer [primary key]  
name_genre varchar  
}  

Table film_genre {  
film_id integer  
genre_id integer  
}  

Table users {  
user_id integer [primary key]  
email varchar  
login varchar  
user_name varchar  
birthday date  
}  

Table friend {
friend_id integer  
user_id integer
}  

Table like {  
user_id integer  
film_id integer  
}  

Ref: films.film_id - film_genre.film_id  
Ref: film_genre.genre_id - genre.genre_id  
Ref: films.film_id > like.film_id  
Ref: users.user_id <> like.user_id  
Ref: users.user_id < friend.user_id  
Ref: friend.friend_id - users.user_id  

