-- Disable foreign key checks for the duration of the script to prevent issues
-- with self-referencing tables or out-of-order creation.
SET FOREIGN_KEY_CHECKS = 0;

-- Create db
CREATE DATABASE IF NOT EXISTS `openFashion_db`;
USE `openFashion_db`;

/*--------------------------------------------------------
-- USERS AND AUTHENTICATION TABLES
----------------------------------------------------------*/

CREATE TABLE `user` ( -- 'user' is a reserved keyword in MySQL, so backticks are necessary
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL, -- Stores the hashed password
                        email VARCHAR(255) UNIQUE,
                        enabled BOOLEAN NOT NULL DEFAULT TRUE,
                        account_not_expired BOOLEAN NOT NULL DEFAULT TRUE,
                        account_not_locked BOOLEAN NOT NULL DEFAULT TRUE,
                        credentials_not_expired BOOLEAN NOT NULL DEFAULT TRUE,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- MySQL equivalent of NOW()
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Auto-update timestamp
);

-- Index on email for faster lookup, especially if it's used for login
CREATE INDEX idx_user_email ON `user` (email);

/*----------------------------------------------------------
-- SOCIAL ACCOUNTS TABLE
----------------------------------------------------------*/

CREATE TABLE user_social_account (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     user_id BIGINT NOT NULL,
                                     provider_name VARCHAR(50) NOT NULL,
                                     provider_user_id VARCHAR(255) NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_user_social_account_user_id FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

-- Unique index for provider and provider_user_id to ensure a social account is linked only once
CREATE UNIQUE INDEX uix_user_social_account ON user_social_account (provider_name, provider_user_id);

/*----------------------------------------------------------
-- ROLES AND USER_ROLES TABLES
----------------------------------------------------------*/

CREATE TABLE role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE -- e.g., 'ROLE_USER', 'ROLE_ADMIN'
);

CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role_id BIGINT NOT NULL,

                           CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
                           CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE,
                           CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

/*----------------------------------------------------------
-- PRODUCT CATALOG TABLES
----------------------------------------------------------*/

CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         price DECIMAL(10, 2) NOT NULL, -- Use DECIMAL for monetary values to avoid floating-point inaccuracies
                         brand VARCHAR(50) NOT NULL,
                         name VARCHAR(255) NOT NULL, -- Increased length for product names
                         description TEXT NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE color (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       hex_code VARCHAR(7) NOT NULL UNIQUE -- e.g., '#RRGGBB'
);

CREATE TABLE size (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(10) NOT NULL UNIQUE -- e.g., 'S', 'M', 'L', 'XL', 'US 10'
);

CREATE TABLE product_variant (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 product_id BIGINT NOT NULL,
                                 color_id BIGINT NOT NULL,
                                 size_id BIGINT NOT NULL,
                                 stock_quantity INT NOT NULL DEFAULT 0,
                                 sku VARCHAR(100) NOT NULL UNIQUE, -- Stock Keeping Unit
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_product_variant_product_id FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_product_variant_color_id FOREIGN KEY (color_id) REFERENCES color(id) ON DELETE RESTRICT,
                                 CONSTRAINT fk_product_variant_size_id FOREIGN KEY (size_id) REFERENCES size(id) ON DELETE RESTRICT,

    -- Ensure uniqueness for a product with a specific color and size combination
                                 CONSTRAINT uix_product_variant_options UNIQUE (product_id, color_id, size_id)
);

/*----------------------------------------------------------
-- CATEGORIES TABLES
-----------------------------------------------------------*/

CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(50) NOT NULL UNIQUE,
                          parent_id BIGINT, -- Self-referencing foreign key for hierarchical categories
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          CONSTRAINT fk_category_parent_id FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE RESTRICT
);

CREATE TABLE product_category (
                                  product_id BIGINT NOT NULL,
                                  category_id BIGINT NOT NULL,

                                  CONSTRAINT pk_product_category PRIMARY KEY (product_id, category_id),
                                  CONSTRAINT fk_product_category_product_id FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_product_category_category_id FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

/*----------------------------------------------------------
-- SHOPPING CART TABLES
----------------------------------------------------------*/

CREATE TABLE cart (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL UNIQUE, -- Ensures a user has only one cart
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      CONSTRAINT fk_cart_user_id FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE cart_item (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           cart_id BIGINT NOT NULL,
                           product_variant_id BIGINT NOT NULL,
                           quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           CONSTRAINT fk_cart_item_cart_id FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
                           CONSTRAINT fk_cart_item_product_variant_id FOREIGN KEY (product_variant_id) REFERENCES product_variant(id) ON DELETE RESTRICT,
    -- Ensures a product variant is only listed once per cart
                           CONSTRAINT uix_cart_item_variant UNIQUE (cart_id, product_variant_id)
);

/*----------------------------------------------------------
-- ORDERS TABLES
----------------------------------------------------------*/
CREATE TABLE `order` (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- e.g., 'PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'
                        total_price DECIMAL(10, 2) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key to link the order to a user
                        CONSTRAINT fk_orders_user_id FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE order_item (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_variant_id BIGINT NOT NULL,
                             quantity INT NOT NULL,
                             price_at_purchase DECIMAL(10, 2) NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign keys to link the item to an order and a product variant
                             CONSTRAINT fk_order_items_order_id FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE,
                             CONSTRAINT fk_order_items_product_variant_id FOREIGN KEY (product_variant_id) REFERENCES product_variant(id) ON DELETE RESTRICT
);

CREATE TABLE shipping_detail (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  order_id BIGINT UNIQUE NOT NULL, -- Each order has one set of shipping details
                                  address_line_1 VARCHAR(255) NOT NULL,
                                  address_line_2 VARCHAR(255),
                                  city VARCHAR(100) NOT NULL,
                                  state_province VARCHAR(100),
                                  postal_code VARCHAR(20) NOT NULL,
                                  country VARCHAR(100) NOT NULL,

                                  CONSTRAINT fk_shipping_details_order_id FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE
);

/*----------------------------------------------------------
-- JWT TOKEN BLACKLIST TABLES
----------------------------------------------------------*/

CREATE TABLE jwt_blacklist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token TEXT
);

-- Re-enable foreign key checks after all tables are created
SET FOREIGN_KEY_CHECKS = 1;