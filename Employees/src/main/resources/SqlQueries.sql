--CREATE TABLE employees (
--    empid VARCHAR(50) PRIMARY KEY,
--    emp_name VARCHAR(100) NOT NULL,
--    emp_dob DATE NOT NULL,
--    emp_address TEXT,
--    emp_email VARCHAR(255) UNIQUE NOT NULL
--	 department_name VARCHAR(100) NOT NULL,
--);
--CREATE TABLE emp_login (
--    empid VARCHAR(50) PRIMARY KEY,
--    emp_password TEXT NOT NULL
--);
--CREATE TABLE EmpRole (
--    empid VARCHAR(50) REFERENCES employees(empid) ON DELETE CASCADE,
--    emprole VARCHAR(50) NOT NULL,
--    PRIMARY KEY (empid, emprole)
--);
--
--SELECT * FROM employees;
--SELECT * FROM EmpRole;
--SELECT * FROM emp_login;
--
--CREATE EXTENSION IF NOT EXISTS pgcrypto;
--insert into employees values('TEK-1','SAI','2003-10-25','HYDERABAD','sai123@gmail.com','developer');
--insert into EmpRole values('TEK-1','ADMIN');
--insert into emp_login values('TEK-1','$2a$10$cPUkSFDh8jAY2tursR5Zs.iPZ0yBp2U0CsFKPivF78hRVQPajl1rm');
--
--DELETE FROM emp_login;
--DELETE FROM employees;
--DELETE FROM EmpRole;
--
--ALTER TABLE employees
--    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT true,
--    ADD COLUMN deleted_at TIMESTAMP NULL;
--

CREATE TABLE employees (
    empid varchar(50) PRIMARY KEY default 'TEK' || nextval('emp_no'),
    emp_name varchar(100) NOT NULL,
    emp_dob DATE NOT NULL,
    emp_address varchar(256) not null,
    emp_email VARCHAR(255) UNIQUE NOT NULL,
	department_name VARCHAR(100) NOT NULL
);
CREATE TABLE emp_login (
    empid VARCHAR(50) PRIMARY KEY,
    emp_password varchar(256) NOT NULL,
	constraint f_key
	foreign key(empid)
	references employees(empid)
	on delete cascade
);
create type roles as enum('ADMIN','MANAGER','EMPLOYEE');

CREATE TABLE EmpRole (
    empid VARCHAR(50) ,
    emprole roles  NOT NULL,
    PRIMARY KEY (empid, emprole),
	constraint f_key
  foreign key (empid)
  references employees(empid)
  on delete cascade
);

SELECT * FROM employees;
SELECT * FROM EmpRole;
SELECT * FROM emp_login;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

create sequence emp_no start 1;

insert into employees values('TEK-1','SAI','2003-10-25','HYDERABAD','sai123@gmail.com','developer');
insert into EmpRole values('TEK-1','ADMIN');
insert into emp_login values('TEK-1','$2a$10$lhEV9G/37JI9imP7KvokPuYsuAIw2CP5lXMcb8erKtERv5BVAlbly');

DELETE FROM emp_login;
DELETE FROM employees;
DELETE FROM EmpRole;

drop table employees ;
drop table emp_login;
drop table EmpRole;

ALTER TABLE employees
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT true,
    ADD COLUMN deleted_at TIMESTAMP NULL;


