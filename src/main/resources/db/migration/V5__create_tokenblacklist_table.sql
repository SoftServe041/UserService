DROP TABLE IF EXISTS `jwtBlackList`;
CREATE TABLE `jwtblacklist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `date_of_adding` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
delete from jwtblacklist where date_of_adding < now() - interval 5000 second;