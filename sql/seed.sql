-- MySQL dump 10.13  Distrib 8.4.3, for Win64 (x86_64)
--
-- Host: 34.64.170.40    Database: cwdb
-- ------------------------------------------------------
-- Server version	8.0.41-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
/*!40000 ALTER TABLE `classes` DISABLE KEYS */;
INSERT INTO `classes` VALUES (1,1,'P001','2024-1','월 9:00-11:00',NULL),(2,2,'P001','2024-1','화 14:00-16:00',NULL),(3,3,NULL,'2024-1','수 10:00-12:00',NULL),(4,4,'P002','2024-1','목 13:00-15:00',NULL),(5,5,NULL,'2024-1','금 15:00-17:00',NULL);
/*!40000 ALTER TABLE `classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'자료구조','MAJOR',3,1,NULL,NULL,NULL),(2,'운영체제','MAJOR',3,1,NULL,NULL,NULL),(3,'알고리즘','MAJOR',3,2,NULL,NULL,NULL),(4,'인공지능 개론','MAJOR',3,3,NULL,NULL,NULL),(5,'머신러닝','MAJOR',3,3,NULL,NULL,NULL),(6,'데이터베이스','MAJOR',3,5,NULL,NULL,NULL),(7,'전자회로','MAJOR',3,4,NULL,NULL,NULL),(8,'컴퓨터 개론','LIBERAL',3,NULL,NULL,NULL,NULL),(9,'창의적 문제해결','LIBERAL',3,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES (5,'데이터사이언스학과'),(2,'소프트웨어공학과'),(3,'인공지능학과'),(4,'전자공학과'),(1,'컴퓨터공학과');
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `enrollments`
--

LOCK TABLES `enrollments` WRITE;
/*!40000 ALTER TABLE `enrollments` DISABLE KEYS */;
INSERT INTO `enrollments` VALUES (1,'20241001',1,'2024-01-10'),(2,'20241002',2,'2024-01-12'),(3,'20241003',4,'2024-01-15'),(4,'20241001',3,'2024-01-18'),(5,'20200924',4,'2025-03-14'),(6,'20200924',2,'2025-03-18'),(7,'20200924',1,'2025-03-10');
/*!40000 ALTER TABLE `enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `grades`
--

LOCK TABLES `grades` WRITE;
/*!40000 ALTER TABLE `grades` DISABLE KEYS */;
INSERT INTO `grades` VALUES (1,1,'A0'),(2,2,'B_PLUS'),(3,3,'C0'),(4,4,'A_PLUS'),(5,5,'A_PLUS'),(6,6,'B_PLUS'),(7,7,'B0');
/*!40000 ALTER TABLE `grades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'201319021','수강신청 오류가 발생했어요','수강신청을 했는데, 강의 목록에 표시되지 않습니다. 어떻게 해야 하나요?','2025-03-10','OPEN',10),(2,'20241001','강의자료를 어디서 받을 수 있나요?','이번 주 강의 자료를 어디서 확인할 수 있나요?','2025-03-11','OPEN',5),(3,'20241002','성적 조회가 안됩니다','성적 조회 페이지에 접속했는데, 제 성적이 표시되지 않습니다.','2025-03-12','OPEN',8),(4,'20241003','휴학 신청은 어디서 하나요?','휴학 신청을 하고 싶은데, 학사관리시스템에서 찾을 수가 없어요.','2025-03-13','OPEN',3),(5,'20241001','교양 과목 패스/페일 선택 가능할까요?','교양 과목을 패스/페일로 바꿀 수 있는지 궁금합니다.','2025-03-14','OPEN',2);
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('201319021','서혁빈','STUDENT','1993-09-25','seohyeokbin@naver.com','010-1301-1301','tjgurqls',1,'/uploads/profiles/s1.jpg'),('20200924','이시온','STUDENT','2000-09-24','leesion29@naver.com','010-1010-2929','1',2,'/uploads/profiles/s2.jpg'),('20241001','김철수','STUDENT','2002-03-12','chulsu@example.com','010-1111-2222','hashed_password_1',1,NULL),('20241002','이영희','STUDENT','2001-07-25','younghee@example.com','010-3333-4444','hashed_password_2',2,NULL),('20241003','박민준','STUDENT','2003-05-18','minjun@example.com','010-5555-6666','hashed_password_3',3,NULL),('A001','관리자','ADMIN','1990-01-01','admin@example.com','010-0000-0000','hashed_admin_password',NULL,NULL),('P001','박교수','PROFESSOR','1978-11-03','prof_park@example.com','010-7777-8888','hashed_password_4',1,NULL),('P002','최교수','PROFESSOR','1982-06-15','prof_choi@example.com','010-9999-0000','hashed_password_5',4,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-18 11:51:15
