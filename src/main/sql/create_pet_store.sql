CREATE TABLE `categories` (
  `category_id` BIGINT(20) NOT NULL AUTO_INCREMENT
  ,`category_name` VARCHAR(500) NOT NULL
  ,UNIQUE KEY `Unq_Category` (`category_name`)
  ,PRIMARY KEY (`category_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `tags` (
  `tag_id` BIGINT(20) NOT NULL AUTO_INCREMENT
  ,`tag_name` VARCHAR(500) NOT NULL
  ,PRIMARY KEY (`tag_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `pets` (
  `pet_id` BIGINT(20) NOT NULL AUTO_INCREMENT
  ,`pet_name` VARCHAR(500) NOT NULL
  ,`category_id` BIGINT(20)
  ,`status` VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE'
  ,PRIMARY KEY (`pet_id`)
  ,KEY `FK_petsCategory` (`category_id`)
  ,CONSTRAINT `FK_petsCategory` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `pettags` (
  `tag_id`  BIGINT(20) NOT NULL
  ,`pet_id`  BIGINT(20) NOT NULL
  ,PRIMARY KEY (`pet_id`,`tag_id`)
  ,KEY `FK_PetsTag` (`tag_id`)
  ,KEY `FK_TagsPet` (`pet_id`)
  ,CONSTRAINT `FK_PetsTag` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`tag_id`)
  ,CONSTRAINT `FK_TagsPet` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`pet_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `petPhotos` (
  `pet_photo_id`  BIGINT(20) NOT NULL AUTO_INCREMENT
  ,`pet_id`  BIGINT(20) NOT NULL
  ,`photo_url`  VARCHAR(4000) NOT NULL
  ,PRIMARY KEY (`pet_photo_id`)
  ,KEY `FK_photosPet` (`pet_id`)
  ,CONSTRAINT `FK_photosPet` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`pet_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;