INSERT INTO departments (department_name) VALUES
('컴퓨터공학과'),
('전자공학과'),
('기계공학과'),
('경영학과'),
('영문학과');

INSERT INTO users (user_id, user_name, user_role, user_birth, user_email, user_phone, user_password, department_id) VALUES
('202400001', '김철수', 'STUDENT', '2000-05-10', 'chulsu@example.com', '010-1234-5678', 'password1', 1),
('202400002', '이영희', 'STUDENT', '1999-12-24', 'younghee@example.com', '010-5678-1234', 'password2', 2),
('202400003', '박지성', 'STUDENT', '2001-07-15', 'jisung@example.com', '010-8765-4321', 'password3', 3),
('PROF1001', '최민호', 'PROFESSOR', '1980-03-20', 'minho@example.com', '010-1111-2222', 'password4', 1),
('PROF1002', '정수빈', 'PROFESSOR', '1975-09-30', 'soobin@example.com', '010-3333-4444', 'password5', 2),
('ADMIN1111', '관리자', 'ADMIN', '1970-02-02', 'admin@example.com', '010-5555-6666', 'password6', null);
INSERT INTO courses (course_name, course_type, credit, department_id) VALUES
('알고리즘', 'MAJOR', 3, 1),
('전자회로', 'MAJOR', 3, 2),
('열역학', 'MAJOR', 3, 3),
('비즈니스 커뮤니케이션', 'LIBERAL', 2, NULL),
('영어 작문', 'LIBERAL', 2, NULL);

INSERT INTO classes (course_id, professor_id, class_semester, class_schedule) VALUES
(1, 'PROF1001', '2024-1', '월 10:00-12:00'),
(2, 'PROF1002', '2024-1', '화 14:00-16:00'),
(3, 'PROF1001', '2024-1', '수 09:00-11:00'),
(4, 'PROF1002', '2024-1', '목 13:00-15:00'),
(5, 'PROF1001', '2024-1', '금 11:00-13:00');

INSERT INTO enrollments (student_id, class_id) VALUES
('202400001', 1),
('202400001', 4),
('202400002', 2),
('202400003', 3),
('202400003', 5);

INSERT INTO grades (enrollment_id, grade_grade) VALUES
(1, 'A+'),
(2, 'B0'),
(3, 'C+'),
(4, 'A0'),
(5, 'B+');

select * from grades;
select * from courses;
select * from users;
select * from enrollments;
select * from classes;
select * from departments;

desc users;
desc classes;
select grade_id as 성적id, enrollment_id as 수강신청id, grade_grade as 성적 from grades;

select course_name as 과목명, credit as 학점 from courses;

select user_id as 사용자id, user_name as 사용자이름, user_role as 사용자역할, 
user_birth as 생년월일, user_email as 이메일, user_phone as 전화번호, 
department_id as 소속학과id from users;

select enrollment_id as 수강신청id, student_id as 학번, class_id as 개설강의id, enrollment_date as 수강신청일 from enrollments;

-- 성적 조회
select course_name as 과목명, credit as 학점, grade_grade as 성적
from enrollments e, grades g, classes c, courses co
where e.enrollment_id = g.enrollment_id 
And e.class_id =  c.class_id
And c.course_id = co.course_id
And student_id = '202400001';

-- 학생 조회
select user_name as 이름, user_id as 학번, department_name as 학과
from users u, departments d
where u.department_id = d.department_id
and user_role = 'STUDENT';


use cwdb;
desc users;
