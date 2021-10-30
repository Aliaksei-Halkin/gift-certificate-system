DROP TABLE IF EXISTS gift_certificates;
CREATE TABLE IF NOT EXISTS gift_certificates
(
    `certificateId`    BIGINT         NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(250)   NOT NULL,
    `description`      VARCHAR(250)   NULL,
    `price`            DECIMAL(10, 2) NOT NULL,
    `duration`         INT            NOT NULL,
    `create_date`      TIMESTAMP      NOT NULL,
    `last_update_date` TIMESTAMP      NOT NULL,
    `active` boolean default true,
    PRIMARY KEY (`certificateId`),
    UNIQUE INDEX name_UNIQUE (name ASC)
);
DROP TABLE IF EXISTS tags;
CREATE TABLE IF NOT EXISTS tags
(
    `tagId`   BIGINT      NOT NULL AUTO_INCREMENT,
    `tagName` VARCHAR(45) NOT NULL,
    `active` boolean default true,
    PRIMARY KEY (`tagId`),
    UNIQUE INDEX tagName_UNIQUE (tagName ASC)
);

DROP TABLE IF EXISTS certificates_has_tags;
CREATE TABLE IF NOT EXISTS certificates_has_tags
(
    certificateId BIGINT NOT NULL,
    tagId         BIGINT NOT NULL,
    PRIMARY KEY (certificateId, tagId),
    CONSTRAINT fk_gift_certificates_has_tags_gift_certificates
        FOREIGN KEY (certificateId)
            REFERENCES gift_certificates (certificateId)
            ON DELETE CASCADE,
    CONSTRAINT fk_gift_certificates_has_tags_tags1
        FOREIGN KEY (tagId)
            REFERENCES tags (tagId)
            ON DELETE CASCADE
);
