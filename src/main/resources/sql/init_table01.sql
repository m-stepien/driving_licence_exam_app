CREATE TABLE users(
id SERIAL PRIMARY KEY,
username VARCHAR(32) UNIQUE,
email VARCHAR(128) UNIQUE,
password TEXT
);