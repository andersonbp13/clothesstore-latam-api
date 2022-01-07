DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS visit;

CREATE TABLE product (
                           id bigint NOT NULL AUTO_INCREMENT,
                           name varchar(100) NOT NULL,
                           price double NOT NULL,
                           discount double NOT NULL,
                           section varchar(50) NOT NULL,
                           description varchar(250) NOT NULL,
                           stock bigint NOT NULL,
                           country enum('COLOMBIA','CHILE','MEXICO','PERU') NOT NULL,
                           front_image varchar(350) NOT NULL,
                           back_image varchar(350) NOT NULL,
                           PRIMARY KEY (id)
);

CREATE TABLE visit (
                         id bigint NOT NULL AUTO_INCREMENT,
                         product_id bigint NOT NULL,
                         nro_visit bigint NOT NULL DEFAULT '0',
                         last_view_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (id),
                         UNIQUE (product_id)
);


ALTER TABLE visit ADD FOREIGN KEY (product_id) REFERENCES product(id);
