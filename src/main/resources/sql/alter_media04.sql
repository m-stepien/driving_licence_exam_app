alter table media add column file_type varchar(8);

update media set file_type='jpg' where file_name like '%.jpg' or file_name  like '%.JPG';
update media set file_type='wmv' where file_name  like '%.wmv';