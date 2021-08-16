create table onlineshop.users (id SERIAL PRIMARY KEY, name varchar(200), password varchar(20));
create table onlineshop.tokens (user_id int, token varchar(200), PRIMARY KEY (user_id, token));