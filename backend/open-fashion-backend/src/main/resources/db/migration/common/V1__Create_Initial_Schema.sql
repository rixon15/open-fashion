-- ==========================================
-- 1. USER MANAGEMENT
-- ==========================================

CREATE TABLE "user" (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255),
                        email VARCHAR(255) UNIQUE,
                        enabled BOOLEAN NOT NULL DEFAULT TRUE,
                        account_not_expired BOOLEAN NOT NULL DEFAULT TRUE,
                        account_not_locked BOOLEAN NOT NULL DEFAULT TRUE,
                        credentials_not_expired BOOLEAN NOT NULL DEFAULT TRUE,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        phone_number VARCHAR(20),
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_address (
                              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              user_id BIGINT,
                              address_line1 VARCHAR(255) NOT NULL,
                              address_line2 VARCHAR(255),
                              city VARCHAR(100) NOT NULL,
                              postal_code VARCHAR(20) NOT NULL,
                              country VARCHAR(100) NOT NULL,
                              is_default BOOLEAN DEFAULT FALSE,

                              CONSTRAINT fk_user_address_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE role (
                      id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_role (
                           user_id BIGINT,
                           role_id BIGINT,

                           CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
                           CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES "user"(id),
                           CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id)
);


-- ==========================================
-- 2. PRODUCT & CATEGORY MANAGEMENT
-- ==========================================

CREATE TABLE category (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          parent_id BIGINT,

                          CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES category(id)
);

CREATE TABLE product (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT NOT NULL,
                         brand VARCHAR(50) NOT NULL,
                         base_price DECIMAL(10,2) NOT NULL,
                         is_active BOOLEAN DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_category (
                                  product_id BIGINT,
                                  category_id BIGINT,

                                  CONSTRAINT pk_product_category PRIMARY KEY (product_id, category_id),
                                  CONSTRAINT fk_prod_cat_product FOREIGN KEY (product_id) REFERENCES product(id),
                                  CONSTRAINT fk_prod_cat_category FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE product_image (
                               id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               product_id BIGINT,
                               url VARCHAR(255) NOT NULL,
                               is_primary BOOLEAN DEFAULT FALSE,

                               CONSTRAINT fk_product_image_product FOREIGN KEY (product_id) REFERENCES product(id)
);


-- ==========================================
-- 3. VARIANTS (Size, Color, SKU)
-- ==========================================

CREATE TABLE color (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       hexcode VARCHAR(50) NOT NULL
);

CREATE TABLE size (
                      id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      name VARCHAR(10) NOT NULL
);

CREATE TABLE product_variant (
                                 id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 product_id BIGINT,
                                 color_id BIGINT,
                                 size_id BIGINT,
                                 stock_quantity INT NOT NULL,
                                 sku VARCHAR(100) NOT NULL UNIQUE,
                                 price_override DECIMAL(10,2),

                                 CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES product(id),
                                 CONSTRAINT fk_variant_color FOREIGN KEY (color_id) REFERENCES color(id),
                                 CONSTRAINT fk_variant_size FOREIGN KEY (size_id) REFERENCES size(id)
);


-- ==========================================
-- 4. ORDER & CART MANAGEMENT
-- ==========================================

CREATE TABLE cart_item (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           user_id BIGINT,
                           product_variant_id BIGINT,
                           quantity INT NOT NULL DEFAULT 1,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_cart_item_user FOREIGN KEY (user_id) REFERENCES "user"(id),
                           CONSTRAINT fk_cart_item_variant FOREIGN KEY (product_variant_id) REFERENCES product_variant(id)
);

CREATE TABLE shop_order (
                            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            user_id BIGINT,
                            order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            shipping_address_id BIGINT,
                            order_total DECIMAL(10,2) NOT NULL,
                            status VARCHAR(20) NOT NULL, -- PENDING, PAID, SHIPPED, DELIVERED

                            CONSTRAINT fk_shop_order_user FOREIGN KEY (user_id) REFERENCES "user"(id),
                            CONSTRAINT fk_shop_order_address FOREIGN KEY (shipping_address_id) REFERENCES user_address(id)
);

CREATE TABLE order_line (
                            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            order_id BIGINT,
                            product_variant_id BIGINT,
                            quantity INT NOT NULL,
                            price_at_purchase DECIMAL(10,2) NOT NULL,

                            CONSTRAINT fk_order_line_order FOREIGN KEY (order_id) REFERENCES shop_order(id),
                            CONSTRAINT fk_order_line_variant FOREIGN KEY (product_variant_id) REFERENCES product_variant(id)
);