-- phpMyAdmin SQL Dump
-- version 4.1.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Giu 29, 2020 alle 22:09
-- Versione del server: 5.6.33-log
-- PHP Version: 5.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `my_sms1920salagiochi`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `Scoreboard`
--

CREATE TABLE IF NOT EXISTS `Scoreboard` (
  `IdScore` int(11) NOT NULL AUTO_INCREMENT,
  `Nickname` varchar(50) NOT NULL,
  `FlappyPlanet` decimal(10,2) NOT NULL DEFAULT '0.00',
  `MeteorDodge` decimal(10,2) NOT NULL DEFAULT '0.00',
  `SpaceShooter` decimal(10,2) NOT NULL DEFAULT '0.00',
  `Breakout` decimal(10,2) NOT NULL DEFAULT '0.00',
  `Globale` decimal(12,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`IdScore`),
  UNIQUE KEY `IdScore_2` (`IdScore`),
  KEY `IdScore` (`IdScore`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=61 ;

--
-- Dump dei dati per la tabella `Scoreboard`
--

INSERT INTO `Scoreboard` (`IdScore`, `Nickname`, `FlappyPlanet`, `MeteorDodge`, `SpaceShooter`, `Breakout`, `Globale`) VALUES
(33, 'ivano', 149.00, 0.00, 0.00, 0.00, 149.00),
(58, 'Anto', 2014.00, 0.00, 1500.00, 3700.00, 7214.00),
(36, 'Vincenzo', 1948.00, 1804.00, 800.00, 1350.00, 5902.00),
(37, 'Flavio', 101.00, 0.00, 0.00, 0.00, 101.00),
(39, 'Daniele', 444.00, 502.00, 600.00, 9025.00, 10571.00),
(43, 'Tommy', 92.00, 0.00, 0.00, 0.00, 92.00),
(44, 'Guglio', 396.00, 234.00, 2200.00, 5825.00, 8655.00),
(45, 'giopalmi', 120.00, 0.00, 0.00, 3575.00, 3695.00),
(46, 'Roberto', 83.00, 0.00, 0.00, 850.00, 933.00),
(47, 'roby_laga', 942.00, 703.00, 1350.00, 5250.00, 8245.00),
(60, 'last_test', 334.00, 0.00, 150.00, 0.00, 484.00),
(49, 'Test pixel', 0.00, 0.00, 0.00, 725.00, 725.00),
(50, 'Drake_D22 ', 211.00, 0.00, 0.00, 2050.00, 2261.00),
(51, 'Tommycomanda', 1493.00, 772.00, 2500.00, 7025.00, 11790.00);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
