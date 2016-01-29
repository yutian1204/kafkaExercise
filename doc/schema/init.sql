set names utf8mb5;
use test;
create table visit_info (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
`ip` varchar(64) NOT NULL COMMENT 'ip',
`time` date NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'visit time',
PRIMARY KEY (`id`)
)