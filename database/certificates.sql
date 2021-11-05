DROP SCHEMA IF EXISTS `certificatesdb`;

CREATE SCHEMA IF NOT EXISTS `certificatesdb`;
USE `certificatesdb`;

CREATE TABLE IF NOT EXISTS `certificatesdb`.`gift_certificates`
(
    `certificateId`    BIGINT         NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(250)   NOT NULL,
    `description`      VARCHAR(250)   NULL     DEFAULT NULL,
    `price`            DECIMAL(10, 2) NOT NULL,
    `duration`         INT            NOT NULL,
    `create_date`      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date` TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `active`           boolean                 default true,
    PRIMARY KEY (`certificateId`)
);

CREATE UNIQUE INDEX `name_UNIQUE` ON `certificatesdb`.`gift_certificates` (`name` ASC) VISIBLE;

CREATE TABLE IF NOT EXISTS `certificatesdb`.`tags`
(
    `tagId`   BIGINT      NOT NULL AUTO_INCREMENT,
    `tagName` VARCHAR(45) NOT NULL,
    `active`  boolean default true,
    PRIMARY KEY (`tagId`)
);

CREATE UNIQUE INDEX `tagName_UNIQUE` USING HASH ON `certificatesdb`.`tags` (`tagName` ASC) VISIBLE;

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
);
CREATE INDEX `fk_gift_certificates_has_tags_tags1_idx` USING HASH ON `certificatesdb`.`certificates_has_tags` (`tagId` ASC) VISIBLE;
CREATE INDEX `fk_gift_certificates_has_tags_gift_certificates_idx` USING HASH ON `certificatesdb`.`certificates_has_tags` (`certificateId` ASC) VISIBLE;

CREATE TABLE IF NOT EXISTS user
(
    user_id    BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    login      VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    email      VARCHAR(50) NOT NULL,
    active     BOOL default true
);
CREATE UNIQUE INDEX user_login USING HASH ON user (login ASC);

CREATE TABLE IF NOT EXISTS order
(
    order_id    BIGINT    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT    NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cost        DECIMAL(20, 2),
    active      BOOL               default true,
    CONSTRAINT fk_order_user
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS orders_has_gift_certificate
(
    order_id       BIGINT NOT NULL,
    certificate_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, certificate_id),
    CONSTRAINT fk_orders_has_gift_certificates_order1
        FOREIGN KEY (order_id) REFERENCES order (order_id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_orders_has_gift_certificates_gift_certificates2
        FOREIGN KEY (certificate_id) REFERENCES gift_certificates (certificateId)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Data for table `certificatesdb`.`gift_certificates`
-- -----------------------------------------------------
START TRANSACTION;
USE `certificatesdb`;
INSERT INTO `certificatesdb`.`gift_certificates` (`name`, `description`, `price`, `duration`)
VALUES ('Fly on a helicopter', 'It is  a wonderful adventure', 899.99, 1);
INSERT INTO `certificatesdb`.`gift_certificates` (`name`, `description`, `price`, `duration`)
VALUES ('Massage', 'All body', 49.99, 1);
INSERT INTO `certificatesdb`.`gift_certificates` (`name`, `description`, `price`, `duration`)
VALUES ('Skydiving', 'Skydiving from a skyscraper', 199.99, 2);
INSERT INTO `certificatesdb`.`gift_certificates` (`name`, `description`, `price`, `duration`)
VALUES ('Cinema', 'You can choose 10 movie per month 10/2021', 5, 50);
INSERT INTO `certificatesdb`.`gift_certificates` (`name`, `description`, `price`, `duration`)
VALUES ('Tourist equipment', ' you will choose one thing for free for 50 euros', 30, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `certificatesdb`.`tags`
-- -----------------------------------------------------
START TRANSACTION;
USE `certificatesdb`;
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`)
VALUES (1, 'rest');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`)
VALUES (2, 'entertainment');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`)
VALUES (3, 'vacation');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`)
VALUES (4, 'tourism');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`)
VALUES (5, 'hike');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`)
VALUES (6, 'health');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`)
VALUES (7, 'extreme');
INSERT INTO `certificatesdb`.`tags` (`tagId`, `tagName`)
VALUES (8, 'massage');

COMMIT;


-- -----------------------------------------------------
-- Data for table `certificatesdb`.`certificates_has_tags`
-- -----------------------------------------------------
START TRANSACTION;
USE `certificatesdb`;
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (1, 1);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (1, 2);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (1, 7);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (2, 1);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (2, 6);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (2, 8);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (3, 2);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (3, 7);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (4, 1);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (4, 2);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (4, 3);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (5, 1);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (5, 3);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (5, 4);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (5, 5);
INSERT INTO `certificatesdb`.`certificates_has_tags` (`certificateId`, `tagId`)
VALUES (5, 7);

COMMIT;

