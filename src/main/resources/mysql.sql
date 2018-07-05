USE ExamSys;

-- 管理员---

create table admin (
	username varchar (20) primary key,
	password varchar (20)
)

insert into admin(username,password) values("admin","admin");

select * from admin

alter table admin add id int 

commit;

-- END---



-- 学生表---
create table student(
	stuNo varchar(20) primary key,
	stuName varchar(20) ,
	stuClass varchar(20),
	stuDepartment varchar(20)
)

alter table student add stuSex varchar(20)
insert into student values('202141522','莫庆根','软嵌142','计算机学院');
update student set stuSex = '男';
commit;

select * from student;
-- 查询列名不重复
select distinct studepartment from student;

-- END---


-- 试题表---
create table question(
	q_Id int primary key ,-- 题目id
	q_Content varchar(500),-- 内容
	q_Answer varchar(300),-- 答案
	q_Type varchar(20) -- 类型
)

-- 试卷表
create table paper (
	p_id int primary key,
	p_name varchar(20)
)

-- 试题-试卷表
create table question_paper(
	id int primary key,
	q_id int,
	p_id int
)



insert into question(q_content,q_answer,q_type) values('计算1+1的值','2','c');
insert into paper(p_name) values('2019-江苏高考试卷');
insert into question_paper(q_id,p_id) values(1,1);

select q.q_Id,q_content,q_answer,q_type,p.p_id,p_name from question q left join question_paper qp on q.q_id = qp.q_id left join paper p on qp.p_id = p.p_id 

-- END---

-- 考试表---
create table exam (
	e_id int primary key,
	e_name varchar(20),
	p_id int ,	-- 试卷id
	e_time int,	-- 考试时间
	stu_list varchar(20),-- 学生名单
	c_score int,-- 单选题分值
	dc_score int,-- 多选题
	j_score int,-- 判断题
	f_score int, -- 填空题
	a_score int -- 简答题
)


drop table exam;


-- 学生登录表---
create table stu_login(
	stu_no varchar(20) primary key,
	password varchar(20),
	status int default 0
)



-- 考试试卷成绩表--
create table grade(
	id int primary key ,
	stu_no varchar(20) not null,
	e_id int not null,
	obj_score int, 
	sub_score int,
	total_score int,
	is_marked int check(is_marked = 0 or is_marked = 1) default 0-- 是否阅卷
)

-- 考试试卷答题表
create table paper_answer(
	id int primary key ,
	stu_no varchar(20) not null,
	e_id int not null,
	obj_answer varchar(2000),
	sub_answer varchar(2000)
)
