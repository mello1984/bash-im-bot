TRUNCATE max_quote_number, strip, users;

INSERT INTO max_quote_number(id, max_quote_number)
VALUES (1, 100);


INSERT INTO strip (id, link, number)
VALUES (1, 'image1.jpg', 20110101),
       (2, 'image2.jpg', 20120202),
       (3, 'image3.jpg', 20130303),
       (4, 'image4.jpg', 20140404),
       (5, 'image5.jpg', 20150505);

ALTER SEQUENCE strip_id_seq RESTART WITH 10;


INSERT INTO users (id)
VALUES (100),
       (200);