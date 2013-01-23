-- MySQL dump 10.13  Distrib 5.5.29, for osx10.6 (i386)
--
-- Host: localhost    Database: poker
-- ------------------------------------------------------
-- Server version	5.5.29

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
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `board` (
  `board_id` int(11) NOT NULL AUTO_INCREMENT,
  `flop1` varchar(25) DEFAULT NULL,
  `flop2` varchar(25) DEFAULT NULL,
  `flop3` varchar(25) DEFAULT NULL,
  `turn` varchar(25) DEFAULT NULL,
  `river` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`board_id`)
) ENGINE=InnoDB AUTO_INCREMENT=655361 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game` (
  `game_id` int(11) NOT NULL AUTO_INCREMENT,
  `players_left` int(11) NOT NULL,
  `bb_player_id` int(11) DEFAULT NULL,
  `game_type` varchar(15) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `is_started` tinyint(1) NOT NULL,
  `current_hand_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=131075 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hand`
--

DROP TABLE IF EXISTS `hand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hand` (
  `hand_id` int(11) NOT NULL AUTO_INCREMENT,
  `board_id` int(11) DEFAULT NULL,
  `game_id` int(11) NOT NULL,
  `player_to_act_id` int(11) DEFAULT NULL,
  `blind_level` varchar(25) NOT NULL,
  PRIMARY KEY (`hand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=360449 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hand_deck`
--

DROP TABLE IF EXISTS `hand_deck`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hand_deck` (
  `hand_id` int(11) NOT NULL,
  `card` varchar(25) NOT NULL,
  PRIMARY KEY (`hand_id`,`card`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player` (
  `player_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `chips` int(11) DEFAULT NULL,
  `game_position` int(11) NOT NULL,
  `finished_place` int(11) DEFAULT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=98306 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player_hand`
--

DROP TABLE IF EXISTS `player_hand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_hand` (
  `player_hand_id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `hand_id` int(11) NOT NULL,
  `card1` varchar(25) DEFAULT NULL,
  `card2` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`player_hand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-22 19:24:22
