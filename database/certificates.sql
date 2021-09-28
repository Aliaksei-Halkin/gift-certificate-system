-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema certificatesdb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `certificatesdb`;

-- -----------------------------------------------------
-- Schema certificatesdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `certificatesdb` DEFAULT CHARACTER SET utf8;
USE `certificatesdb`;

-- -----------------------------------------------------
-- Table `certificatesdb`.`gift_certificates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificatesdb`.`gift_certificates`
(
    `certificateId`    BIGINT         NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(250)   NOT NULL,
    `description`      VARCHAR(250)   NULL     DEFAULT NULL,
    `price`            DECIMAL(10, 2) NOT NULL,
    `duration`         INT            NOT NULL,
    `create_date`      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date` TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`certificateId`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 6
    DEFAULT CHARACTER SET = utf8mb3;

CREATE UNIQUE INDEX `name_UNIQUE` ON `certificatesdb`.`gift_certificates` (`name` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `certificatesdb`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificatesdb`.`tags`
(
    `tagId`   BIGINT      NOT NULL AUTO_INCREMENT,
    `tagName` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`tagId`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 9
    DEFAULT CHARACTER SET = utf8mb3;

CREATE UNIQUE INDEX `tagName_UNIQUE` ON `certificatesdb`.`tags` (`tagName` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `certificatesdb`.`certificates_has_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificatesdb`.`certificates_has_tags`
(
    `certificateId` BIGINT NOT NULL,
    `tagId`         BIGINT NOT NULL,
    PRIMARY KEY (`certificateId`, `tagId`),
    CONSTRAINT `fk_gift_certificates_has_tags_gift_certificates`
        FOREIGN KEY (`certificateId`)
            REFERENCES `certificatesdb`.`gift_certificates` (`certificateId`)
            ON DELETE CASCADE,
    CONSTRAINT `fk_gift_certificates_has_tags_tags1`
        FOREIGN KEY (`tagId`)
            REFERENCES `certificatesdb`.`tags` (`tagId`)
            ON DELETE CASCADE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_gift_certificates_has_tags_tags1_idx` ON `certificatesdb`.`certificates_has_tags` (`tagId` ASC) VISIBLE;

CREATE INDEX `fk_gift_certificates_has_tags_gift_certificates_idx` ON `certificatesdb`.`certificates_has_tags` (`certificateId` ASC) VISIBLE;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `certificatesdb`.`gift_certificates`
-- -----------------------------------------------------
START TRANSACTION;
USE `certificatesdb`;
INSERT INTO `certificatesdb`.`gift_certificates` ( `name`, `description`, `price`, `duration` ) VALUES ( 'Fly on helicopter', 'It is wonderful adventure', 899.99, 1);
INSERT INTO `certificatesdb`.`gift_certificates` ( `name`, `description`, `price`, `duration`) VALUES ( 'Massage', 'All body', 49.99, 1);
INSERT INTO `certificatesdb`.`gift_certificates` (`name`, `description`, `price`, `duration`) VALUES ('Skydiving', 'Skydiving from a skyscraper', 199.99, 2);
INSERT INTO `certificatesdb`.`gift_certificates` ( `name`, `description`, `price`, `duration`) VALUES ( 'Cinema', 'You can choose 10 movie per month 10/2021', 5, 50);
INSERT INTO `certificatesdb`.`gift_certificates` ( `name`, `description`, `price`, `duration`) VALUES ( 'Tourist equipment', ' you will choose one thing for free for 50 euros', 30, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `certificatesdb`.`tags`
-- -----------------------------------------------------
START TRANSACTION;
USE `certificatesdb`;
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`) VALUES (1, 'est');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`) VALUES (2, 'entertainment');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`) VALUES (3, 'vacation');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`) VALUES (4, 'tourism');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`) VALUES (5, 'hike');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`) VALUES (6, 'helth');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`) VALUES (7, 'extreme');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`) VALUES (8, 'massage');

COMMIT;


-- -----------------------------------------------------
-- Data for table `certificatesdb`.`certificates_has_tags`
-- -----------------------------------------------------
START TRANSACTION;
USE `certificatesdb`;
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (1, 1);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (1, 2);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (1, 7);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (2, 1);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (2, 6);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (2, 8);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (3, 2);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (3, 7);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (4, 1);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (4, 2);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (4, 3);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (5, 1);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (5, 3);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (5, 4);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (5, 5);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`) VALUES (5, 7);

COMMIT;

