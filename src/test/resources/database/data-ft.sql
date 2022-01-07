SET DATABASE REFERENTIAL INTEGRITY FALSE;
DELETE FROM product;
DELETE FROM visit;
SET DATABASE REFERENTIAL INTEGRITY TRUE;

INSERT INTO product (id, name, price, discount, section, description, stock, country, front_image, back_image) VALUES

('1', 'adasd', '5151', '31', 'asdasdasd', 'asdasdad', '25', 'COLOMBIA', '1641501773276-Foto_2021.png', '1641501775095-IMG_234234.png'),
('2', 'adasd', '5151', '31', 'asdasdasd', 'asdasdad', '25', 'COLOMBIA', '1641501773276-Foto_2021.png', '1641501775095-IMG_234234.png'),
('3', 'adasd', '5151', '31', 'asdasdasd', 'asdasdad', '25', 'COLOMBIA', '1641501773276-Foto_2021.png', '1641501775095-IMG_234234.png'),
('4', 'adasd', '5151', '31', 'asdasdasd', 'asdasdad', '25', 'COLOMBIA', '1641501773276-Foto_2021.png', '1641501775095-IMG_234234.png'),
('5', 'adasd', '5151', '31', 'asdasdasd', 'asdasdad', '25', 'COLOMBIA', '1641501773276-Foto_2021.png', '1641501775095-IMG_234234.png');


INSERT INTO visit (id, product_id, nro_visit, last_view_date) VALUES

('1', '1', '1', '2022-01-06 13:10:29'),
('2', '2', '5', '2022-01-06 13:10:29'),
('3', '3', '17', '2022-01-06 13:10:29'),
('4', '4', '9', '2022-01-06 13:10:29'),
('5', '5', '10', '2022-01-06 13:10:29');
