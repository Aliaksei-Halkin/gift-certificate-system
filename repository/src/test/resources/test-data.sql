INSERT INTO gift_certificates (certificateId, name, description, price, duration,   create_date,
                               last_update_date)
VALUES (1, 'Fly on a helicopter', 'It is  a wonderful adventure', 899.99, 1, '2021-10-15 15:48:24',
        '2021-10-15 15:48:24');
INSERT INTO gift_certificates (certificateId, name, description, price, duration, create_date,
                               last_update_date)
VALUES (2, 'Massage', 'All body', 49.99, 1, '2021-10-15 15:48:24',
        '2021-10-15 15:48:24');
INSERT INTO gift_certificates (certificateId, name, description, price, duration, create_date,
                               last_update_date)
VALUES (3, 'Skydiving', 'Skydiving from a skyscraper', 199.99, 2, '2021-10-15 15:48:24',
        '2021-10-15 15:48:24');
INSERT INTO gift_certificates (certificateId, name, description, price, duration, create_date,
                               last_update_date)
VALUES (4, 'Cinema', 'You can choose 10 movie per month 10/2021', 5, 50, '2021-10-15 15:48:24',
        '2021-10-15 15:48:24');
INSERT INTO gift_certificates (certificateId, name, description, price, duration, create_date,
                               last_update_date)
VALUES (5, 'Tourist equipment', ' you will choose one thing for free for 50 euros', 30, 1, '2021-10-15 15:48:24',
        '2021-10-15 15:48:24');



INSERT INTO tags (tagId, tagName)
VALUES (1, 'rest');
INSERT INTO tags (tagId, tagName)
VALUES (2, 'entertainment');
INSERT INTO tags (tagId, tagName)
VALUES (3, 'vacation');
INSERT INTO tags (tagId, tagName)
VALUES (4, 'tourism');
INSERT INTO tags (tagId, tagName)
VALUES (5, 'hike');
INSERT INTO tags (tagId, tagName)
VALUES (6, 'health');
INSERT INTO tags (tagId, tagName)
VALUES (7, 'extreme');
INSERT INTO tags (tagId, tagName)
VALUES (8, 'massage');

INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (1, 1);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (1, 2);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (1, 7);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (2, 1);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (2, 6);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (2, 8);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (3, 2);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (3, 7);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (4, 1);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (4, 2);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (4, 3);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (5, 1);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (5, 3);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (5, 4);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (5, 5);
INSERT INTO certificates_has_tags (certificateId, tagId)
VALUES (5, 7);