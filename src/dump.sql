CREATE TABLE users (
                       id INT NOT NULL AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       wallet DECIMAL(10,2) DEFAULT 500.00,
                       PRIMARY KEY (id)
);

CREATE TABLE products (
                          id INT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          price DECIMAL(10,2) NOT NULL,
                          description TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (id)
);

CREATE TABLE orders (
                        id INT NOT NULL AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        product_id INT NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (product_id) REFERENCES products(id)
);
INSERT INTO products (name, price, description) VALUES
                                                    ('Laptop', 750.00, 'A powerful laptop with 16GB RAM and 512GB SSD.'),
                                                    ('Smartphone', 500.00, 'A smartphone with a stunning display and great camera.'),
                                                    ('Headphones', 150.00, 'Noise-canceling over-ear headphones.'),
                                                    ('Smartwatch', 200.00, 'A smartwatch with fitness tracking features.'),
                                                    ('Tablet', 300.00, 'A lightweight tablet with a 10-inch display.');