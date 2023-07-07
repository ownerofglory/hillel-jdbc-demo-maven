CREATE TABLE `customer` (
                            `name` varchar(64) DEFAULT NULL,
                            `email` varchar(128) DEFAULT NULL,
                            `birth_date` date DEFAULT NULL,
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `password` varchar(256) DEFAULT 'no-password',
                            PRIMARY KEY (`id`),
                            KEY `customer_email_idx` (`email`)
);
CREATE TABLE `t_order` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
                           `total_amount` float DEFAULT '0',
                           `customer_id` int(11) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `customer_id_fk` (`customer_id`),
                           CONSTRAINT `customer_id_fk` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE
                               SET NULL
);
CREATE TABLE `cutomer_tax_info` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `tax_id` varchar(12) DEFAULT NULL,
                                    `customer_id` int(11) DEFAULT NULL,
                                    `expiration_date` date DEFAULT '2030-01-01',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `customer_id` (`customer_id`),
                                    CONSTRAINT `fk_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
);
CREATE TABLE `product` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `name` varchar(100) NOT NULL,
                           `description` text,
                           `price` float DEFAULT '0',
                           `image_url` varchar(1024) DEFAULT NULL,
                           PRIMARY KEY (`id`)
);
CREATE TABLE `order_item` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `order_id` int(11) DEFAULT NULL,
                              `product_id` int(11) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `order_id` (`order_id`),
                              KEY `product_id` (`product_id`),
                              CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`),
                              CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
);