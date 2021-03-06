CREATE SCHEMA model2 AUTHORIZATION "admin";


create table model2."admin" (
	id serial not null primary key,
	login varchar(100) NOT NULL,
	password varchar(100) not null,
	first_name varchar(100) NOT NULL,
	last_name varchar(100) not NULL,
    age int null
);

create table model2."salary" (
	id serial not null primary key,
	amount int not null,
	dividor int not null
);

alter table model2."salary" add teacher_id int not null;
alter table model2."salary" add constraint fk_salary_teacher foreign key (teacher_id) references model2."teacher"(id);

create table model2."teacher" (
	id serial not null primary key,
    login varchar(100) NOT NULL,
	password varchar(100) not null,
	first_name varchar(100) NOT NULL,
	last_name varchar(100) not NULL,
    age int null
);

create table model2."group" (
	id serial not null primary key,
	teacher_id int references model2."teacher"(id),
	name varchar(100) not null
);

alter table model2."group" add constraint uq_group unique(teacher_id);

create table model2."student" (
	id serial not null primary key,
	login varchar(100) NOT NULL,
	password varchar(100) not null,
	first_name varchar(100) NOT NULL,
	last_name varchar(100) not NULL,
    age int null
);

create table model2."student_group"(
	student_id int references model2."student"(id),
	group_id int references model2."group"(id)
);

alter table model2."student_group" add constraint pk_student_group primary key(student_id, group_id)

create table model2."theme"(
	id serial not null primary key,
	name varchar(255) not null
);

create table model2."theme_group"(
	group_id int references model2."group"(id),
	theme_id int references model2."theme"(id)
);

alter table model2."theme_group" add constraint pk_theme_group primary key(group_id, theme_id);

create table model2."marks" (
	group_id int not null references model2."group"(id),
	theme_id int not null references model2."theme"(id),
	student_id int not null references model2."student"(id),
	mark int not null check (0 <= mark and mark <= 100)
);

alter table model2."marks" add constraint pk_marks primary key(group_id, theme_id, student_id);
----------------------------
insert into model2."student" (login, password, first_name, last_name, age)
values
	('admin', 'ps_admin', 'Petr', 'Petrovich', 35);

insert into model2."student" (login, password, first_name, last_name, age)
values
	('vasja', 'ps_vasja', 'Vasja', 'Pupkin', 23),
	('ivan', 'ps_ivan', 'Ivan', 'Ivanov', null),
	('sergey', 'ps_sergey', 'Sergey', 'Sergeevich', 32);

insert into model2."teacher" (login, password, first_name, last_name, age)
values
	('teacher_login_1', 'ps_teacher_1', 'teacher_name_1', 'teacher_last_name_1', 40),
	('teacher_login_2', 'ps_teacher_2', 'teacher_name_2', 'teacher_last_name_2', 45);

insert into model2."group" (teacher_id, name)
values(1, 'group 1'),
	(2, 'group 2');

insert into model2."group" (teacher_id, name)
values(1, 'group 1'),
	(2, 'group 2');


insert into model2."student_group" (group_id, student_id)
values
	(1, 1),
	(1, 2),
	(2, 3),
	(2, 2);

insert into model2."salary" (teacher_id, amount, dividor)
values
	(1, 100, 1),
	(1, 100, 1),
	(1, 150, 1),
	(2, 200, 1),
	(2, 200, 1);
