CREATE DATABASE  IF NOT EXISTS `estacion` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `estacion`;
-- MySQL dump 10.13  Distrib 5.1.58, for debian-linux-gnu (i686)
--
-- Host: 127.0.0.1    Database: estacion
-- ------------------------------------------------------
-- Server version	5.1.58-1ubuntu1

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
-- Table structure for table `tablaPrecipitacion2`
--

DROP TABLE IF EXISTS `tablaPrecipitacion2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tablaPrecipitacion2` (
  `anio` int(11) NOT NULL,
  `mes` varchar(20) NOT NULL,
  `dia` int(11) NOT NULL,
  `min5` float DEFAULT '-1',
  `min10` float DEFAULT '-1',
  `min20` float DEFAULT '-1',
  `min60` float DEFAULT '-1',
  `min90` float DEFAULT '-1',
  `min120` float DEFAULT '-1',
  UNIQUE KEY `anio` (`anio`,`mes`,`dia`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tablaPrecipitacion2`
--

LOCK TABLES `tablaPrecipitacion2` WRITE;
/*!40000 ALTER TABLE `tablaPrecipitacion2` DISABLE KEYS */;
INSERT INTO `tablaPrecipitacion2` VALUES (2005,'May',9,4.4,5,5.6,6.2,6.2,6.2),(2006,'July',9,5.6,5.6,5.6,-1,-1,-1),(2002,'July',21,20.2,20.2,20.2,-1,-1,-1),(2003,'June',25,15.4,15.4,15.4,29.2,33.4,37),(2004,'August',19,18.4,18.4,18.4,-1,-1,-1),(2006,'February',9,-1,-1,-1,7.2,9,10.8),(2002,'July',18,-1,-1,-1,27.6,30.4,31),(2004,'September',18,-1,-1,-1,24,26,27.2);
/*!40000 ALTER TABLE `tablaPrecipitacion2` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-02-22 10:39:21
