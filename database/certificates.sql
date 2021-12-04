DROP SCHEMA IF EXISTS `certificatesdb`;

CREATE DATABASE IF NOT EXISTS `certificatesdb`;
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

CREATE UNIQUE INDEX `tagName_UNIQUE`   ON `certificatesdb`.`tags` (`tagName` ASC) VISIBLE;

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
CREATE INDEX `fk_gift_certificates_has_tags_tags1_idx` ON `certificatesdb`.`certificates_has_tags` (`tagId` ASC) VISIBLE;
CREATE INDEX `fk_gift_certificates_has_tags_gift_certificates_idx` ON `certificatesdb`.`certificates_has_tags` (`certificateId` ASC) VISIBLE;

CREATE TABLE IF NOT EXISTS `user`
(
    user_id    BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    password   VARCHAR(50) NOT NULL,
    email      VARCHAR(50) NOT NULL,
    active     BOOL default true
);
CREATE UNIQUE INDEX user_email ON user (email ASC);

CREATE TABLE IF NOT EXISTS `orders`
(
    order_id       BIGINT    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id        BIGINT    NOT NULL,
    create_date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_cost     DECIMAL(20, 2),
    active         BOOL               default true,
    CONSTRAINT fk_order_user
        FOREIGN KEY (user_id) REFERENCES user (user_id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `orders_has_gift_certificate`
(
    order_id       BIGINT NOT NULL,
    certificate_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, certificate_id),
    CONSTRAINT fk_orders_has_gift_certificates_order1
        FOREIGN KEY (order_id) REFERENCES `orders` (order_id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_orders_has_gift_certificates_gift_certificates2
        FOREIGN KEY (certificate_id) REFERENCES `gift_certificates` (certificateId)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

