alter table media add column file_type varchar(8);

update media set file_type='jpg' where file_name like '%.jpg' or file_name  like '%.JPG';
update media set file_type='wmv' where file_name  like '%.wmv';
insert into media(id, file_name, file_type) values (0, 'no-image-found.jpg', 'jpg');


create table score(
id SERIAL primary key,
point integer,
of_point integer,
time_of_exam TIMESTAMP,
user_id integer
);