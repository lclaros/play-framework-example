-- MySQL dump 10.13  Distrib 5.6.24, for Win32 (x86)
--
-- Host: localhost    Database: isystem
-- ------------------------------------------------------
-- Server version	5.6.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'1','Activo','ACTIVO',0,'NO','des',0,0,0),(2,'2','Pasivo','PASIVO',0,'NO','',0,0,0),(3,'1.1','Activo Disponible','ACTIVO',1,'NO','ss',0,0,0),(4,'2.1','Obligaciones financieras','PASIVO',2,'NO','ss',1,0,0),(5,'3','Patrimonio','PATRIMONIO',0,'NO','ss',0,0,0),(6,'31','Capital social','PATRIMONIO',5,'NO','22',1,0,0),(7,'410','Costo de ventas','EGRESO',0,'NO','ss',1,0,0),(8,'510','Ventas Netas','INGRESO',0,'NO','ww',1,0,0),(9,'540','Ingreso  de Operaciones','INGRESO',0,'NO','ss',1,0,0),(10,'440','Gastos de operacion','EGRESO',0,'NO','ss',1,0,0),(11,'530','Ingreso 3','INGRESO',0,'NO','sss',1,0,0),(12,'430','Egreso 3','EGRESO',0,'NO','ss',1,0,0),(13,'450','Gastos anticipados','EGRESO',0,'NO','sss',1,0,0),(14,'570','INGRESOS FINANCIEROS','INGRESO',0,'NO','',1,0,0),(15,'470','Egreso 5','EGRESO',0,'NO','sss',1,0,0),(16,'580','Ingreso 6','EGRESO',0,'NO','sss',1,0,0),(17,'480','AJUSTES POR PERIODIFICACIÓN','EGRESO',0,'NO','sss',1,0,0),(18,'ADADC','Egreso ADADC','EGRESO',0,'NO','sss',1,0,0),(19,'ACEI','Egreso ACEI','EGRESO',0,'NO','sss',1,0,0),(20,'460','Egreso 7','EGRESO',0,'NO','ss',1,0,0),(21,'1.1.1','Caja','ACTIVO',3,'NO','Activo disponible',1,0,0);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-27 11:17:08
