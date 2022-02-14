DROP DATABASE IF EXISTS forum;

CREATE DATABASE forum;

USE forum;

CREATE TABLE account(
	id int primary key auto_increment,
    email varchar(30) unique not null,
    password varchar(100) not null,
    name varchar(40) not null
);

CREATE TABLE Category(
	id int primary key auto_increment,
    name varchar(15) not null unique,
    description varchar(40)
);

CREATE TABLE Topic(
	id int primary key auto_increment,
    name varchar(20) not null,
    created_date TIMESTAMP default current_timestamp,
    account_id int,
    FOREIGN KEY (account_id) REFERENCES account(id)
    ON DELETE CASCADE,
    category_id int,
    FOREIGN KEY (category_id) REFERENCES Category(id)
    ON DELETE CASCADE
);

CREATE TABLE Reply(
	id int primary key auto_increment,
    message varchar(50) not null,
	created_date TIMESTAMP default current_timestamp,
	account_id int,
    FOREIGN KEY (account_id) REFERENCES account(id)
    ON DELETE CASCADE,
    topic_id int,
    FOREIGN KEY (topic_id) REFERENCES Topic(id)
    ON DELETE CASCADE
);


