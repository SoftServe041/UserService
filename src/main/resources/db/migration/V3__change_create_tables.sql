SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `billing_details`;
CREATE TABLE `billing_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name_on_card` varchar(255) NOT NULL,
  `card_number` varchar(255) NOT NULL,
  `expiration_month` varchar(255) NOT NULL,
  `expiration_year` varchar(255) NOT NULL,
  `billing_address` varchar(255) NOT NULL,
  `users_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Users_billing_details` (`users_id`),
  CONSTRAINT `FK_Users_billing_details` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(120) NOT NULL UNIQUE,
  `encrypted_password` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles` (
  `users_id` bigint(20) NOT NULL,
  `roles_id` bigint(20) NOT NULL,
  KEY `FK_Roles_users_roles` (`roles_id`),
  KEY `FK_Users_users_roles` (`users_id`),
  CONSTRAINT `FK_Roles_users_roles` FOREIGN KEY (`roles_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK_Users_users_roles` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO roles VALUES (DEFAULT, 'ROLE_USER'),
                         (DEFAULT, 'ROLE_ADMIN');