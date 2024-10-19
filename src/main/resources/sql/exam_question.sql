create table qustion_temp(
question varchar,
answer_a varchar,
answer_b varchar,
answer_c varchar,
answer_correct varchar,
media varchar,
category varchar
);


copy qustion_temp (question, answer_a, answer_b, answer_c, answer_correct, media, category) FROM '/docker-entrypoint-initdb.d/question_mod.csv' DELIMITER ',' CSV HEADER;


update qustion_temp set answer_correct = 'y' where answer_correct = 'Tak';
update qustion_temp set answer_correct = 'n' where answer_correct = 'Nie';
ALTER TABLE qustion_temp
ADD COLUMN answer_id SERIAL;

ALTER TABLE qustion_temp
ADD COLUMN question_id SERIAL;

ALTER TABLE qustion_temp
ADD COLUMN media_id SERIAL;

select * from qustion_temp;
create table answers(
id SERIAL primary key,
answer_a varchar(64),
answer_b varchar(64),
answer_c varchar(64)
)

alter table answers
alter column answer_c type varchar(255);

create table media(
id SERIAL primary key,
file_name varchar(255)
)

create table question(
	id SERIAL PRIMARY KEY,
	question text,
	answer_correct varchar(1),
	answers_id integer,
	media_id integer,
	FOREIGN KEY (answers_id) REFERENCES answers(id),
	FOREIGN KEY (media_id) REFERENCES media(id)
);



insert into question(id,question, answer_correct) select question_id,question, answer_correct from qustion_temp;
insert into answers(id, answer_a, answer_b, answer_c) select answer_id, answer_a, answer_b, answer_c from qustion_temp where answer_a is not null;
update question set answers_id = qustion_temp.answer_id from qustion_temp where qustion_temp.question_id = question.id and qustion_temp.answer_a is not null;
insert into media(id, file_name) select media_id, media from qustion_temp where media is not null;
update question set media_id = qustion_temp.media_id from qustion_temp where qustion_temp.question_id = question.id and qustion_temp.media is not null;

drop table qustion_temp;