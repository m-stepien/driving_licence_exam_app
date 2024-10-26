alter table question add column points integer;

update question set points=1 where answers_id is null and media_id is null;

select * into question_temp from question where answers_id is null and media_id is not null limit 804;

update question_temp set points=2;

UPDATE question
SET points = question_temp.points
FROM question_temp
WHERE question.id = question_temp.id;

drop table question_temp;

update question set points=3 where answers_id is null and points is null;

update question set points = 1 where answers_id is not null and media_id is null;

select * into question_temp from question where answers_id is not null and media_id is not null limit 537;

update question_temp set points=2;

UPDATE question
SET points = question_temp.points
FROM question_temp
WHERE question.id = question_temp.id;

drop table question_temp;

update question set points = 3 where points is null;