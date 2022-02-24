DROP DATABASE IF EXISTS forum;

CREATE DATABASE forum;

USE forum;

CREATE TABLE account(
	id int primary key auto_increment,
    email varchar(50) unique not null,
    password varchar(100) not null,
    name varchar(40) not null,
    photo longblob,
    role enum('User','Admin','Moderator')
);

CREATE TABLE category(
	id int primary key auto_increment,
    name varchar(15) not null unique,
    slug varchar(15) not null,
    description varchar(40)
    );

CREATE TABLE topic(
	id int primary key auto_increment,
    title varchar(20) not null,
	content text,
    views int default 0,
    createdAt TIMESTAMP default current_timestamp,
    user_id int,
    FOREIGN KEY (user_id) REFERENCES account(id)
    ON DELETE CASCADE,
    category_id int,
    FOREIGN KEY (category_id) REFERENCES category(id)
    ON DELETE CASCADE
);

CREATE TABLE reply(
	id int primary key auto_increment,
    content varchar(50) not null,
	created_date TIMESTAMP default current_timestamp,
	user_id int,
    FOREIGN KEY (user_id) REFERENCES account(id)
    ON DELETE CASCADE,
    topic_id int,
    FOREIGN KEY (topic_id) REFERENCES topic(id)
    ON DELETE CASCADE
);


