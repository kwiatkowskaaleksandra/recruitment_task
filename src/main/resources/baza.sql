-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 21, 2024 at 04:58 PM
-- Wersja serwera: 10.4.28-MariaDB
-- Wersja PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `flight`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `flights`
--

CREATE TABLE `flights` (
  `id_flight` bigint(20) NOT NULL,
  `available_seats` int(11) NOT NULL,
  `departure_date` datetime DEFAULT NULL,
  `flight_duration` time DEFAULT NULL,
  `flight_number` varchar(255) DEFAULT NULL,
  `flight_route` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flights`
--

INSERT INTO `flights` (`id_flight`, `available_seats`, `departure_date`, `flight_duration`, `flight_number`, `flight_route`) VALUES
(1, 28, '2025-03-20 06:30:00', '10:20:00', 'LO126', 7),
(2, 130, '2025-03-20 08:30:00', '09:45:00', 'LO126', 4),
(3, 100, '2024-04-29 15:00:00', '03:45:00', 'LO178', 3),
(4, 100, '2024-04-29 04:30:00', '01:30:00', 'LO333', 8);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `flight_routes`
--

CREATE TABLE `flight_routes` (
  `id_flight_route` bigint(20) NOT NULL,
  `arrival_city` varchar(255) DEFAULT NULL,
  `departure_city` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flight_routes`
--

INSERT INTO `flight_routes` (`id_flight_route`, `arrival_city`, `departure_city`) VALUES
(1, 'Opole', 'Kielce'),
(2, 'Opole', 'Kielce'),
(3, 'Madryt', 'Radom'),
(4, 'Opole', 'Kielce'),
(5, 'Opole', 'Kielce'),
(6, 'Opole', 'Kielce'),
(7, 'Opole', 'Kielce'),
(8, 'Madryt', 'Masłów');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `intermediate_airports`
--

CREATE TABLE `intermediate_airports` (
  `id_intermediate_airport` bigint(20) NOT NULL,
  `airport_name` varchar(255) DEFAULT NULL,
  `id_flight_route` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `intermediate_airports`
--

INSERT INTO `intermediate_airports` (`id_intermediate_airport`, `airport_name`, `id_flight_route`) VALUES
(1, 'Okecie Warszawa', 1),
(2, 'Okecie Warszawa', 2),
(3, 'Okęcie Warszawa', 4),
(4, 'Okecie Warszawa', 5),
(5, 'Okecie Warszawa', 6),
(6, 'Okecie Warszawa', 7),
(7, 'Warszawa', 8);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `passengers`
--

CREATE TABLE `passengers` (
  `id_passenger` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `id_flight` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `passengers`
--

INSERT INTO `passengers` (`id_passenger`, `email`, `firstname`, `lastname`, `phone_number`, `id_flight`) VALUES
(1, 'john@example.pl', 'John', 'Doe', '123456789', 1),
(2, 'johnDoe@example.pl', 'John22', 'Doe22', '123456789', 1),
(3, 'marta@gmail.com', 'Marta', 'Janoszek', '123444222', 1);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `flights`
--
ALTER TABLE `flights`
  ADD PRIMARY KEY (`id_flight`),
  ADD KEY `FK1dx4kvq1u1avoov4gwb1dt4af` (`flight_route`);

--
-- Indeksy dla tabeli `flight_routes`
--
ALTER TABLE `flight_routes`
  ADD PRIMARY KEY (`id_flight_route`);

--
-- Indeksy dla tabeli `intermediate_airports`
--
ALTER TABLE `intermediate_airports`
  ADD PRIMARY KEY (`id_intermediate_airport`),
  ADD KEY `FKooe31c4dtu4r4h5a0fg7u0jga` (`id_flight_route`);

--
-- Indeksy dla tabeli `passengers`
--
ALTER TABLE `passengers`
  ADD PRIMARY KEY (`id_passenger`),
  ADD KEY `FK7yotgr5ptn58myjyk7g18jdrx` (`id_flight`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `flights`
--
ALTER TABLE `flights`
  MODIFY `id_flight` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `flight_routes`
--
ALTER TABLE `flight_routes`
  MODIFY `id_flight_route` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `intermediate_airports`
--
ALTER TABLE `intermediate_airports`
  MODIFY `id_intermediate_airport` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `passengers`
--
ALTER TABLE `passengers`
  MODIFY `id_passenger` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `flights`
--
ALTER TABLE `flights`
  ADD CONSTRAINT `FK1dx4kvq1u1avoov4gwb1dt4af` FOREIGN KEY (`flight_route`) REFERENCES `flight_routes` (`id_flight_route`);

--
-- Constraints for table `intermediate_airports`
--
ALTER TABLE `intermediate_airports`
  ADD CONSTRAINT `FKooe31c4dtu4r4h5a0fg7u0jga` FOREIGN KEY (`id_flight_route`) REFERENCES `flight_routes` (`id_flight_route`);

--
-- Constraints for table `passengers`
--
ALTER TABLE `passengers`
  ADD CONSTRAINT `FK7yotgr5ptn58myjyk7g18jdrx` FOREIGN KEY (`id_flight`) REFERENCES `flights` (`id_flight`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
