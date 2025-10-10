ALTER TABLE shipping_detail
    ADD address_line1 VARCHAR(255) NULL;

ALTER TABLE shipping_detail
    ADD address_line2 VARCHAR(255) NULL;

ALTER TABLE shipping_detail
    MODIFY address_line1 VARCHAR(255) NOT NULL;

ALTER TABLE shipping_detail
    DROP COLUMN address_line_1;

ALTER TABLE shipping_detail
    DROP COLUMN address_line_2;

ALTER TABLE cart
    DROP COLUMN created_at;

ALTER TABLE cart
    DROP COLUMN updated_at;

ALTER TABLE cart_item
    DROP COLUMN created_at;

ALTER TABLE cart_item
    DROP COLUMN updated_at;

ALTER TABLE category
    DROP COLUMN created_at;

ALTER TABLE category
    DROP COLUMN updated_at;

ALTER TABLE `order`
    DROP COLUMN created_at;

ALTER TABLE `order`
    DROP COLUMN updated_at;

ALTER TABLE order_item
    DROP COLUMN created_at;

ALTER TABLE product
    DROP COLUMN created_at;

ALTER TABLE product
    DROP COLUMN updated_at;

ALTER TABLE product_variant
    DROP COLUMN created_at;

ALTER TABLE product_variant
    DROP COLUMN updated_at;

ALTER TABLE user
    DROP COLUMN created_at;

ALTER TABLE user
    DROP COLUMN credentials_not_expired;

ALTER TABLE user
    DROP COLUMN updated_at;

ALTER TABLE user_social_account
    DROP COLUMN created_at;

ALTER TABLE user_social_account
    DROP COLUMN updated_at;

ALTER TABLE shipping_detail
    MODIFY city VARCHAR(255);

ALTER TABLE shipping_detail
    MODIFY country VARCHAR(255);

ALTER TABLE shipping_detail
    MODIFY postal_code VARCHAR(255);

ALTER TABLE order_item
    MODIFY price_at_purchase DECIMAL;

ALTER TABLE cart_item
    MODIFY product_variant_id BIGINT NULL;

ALTER TABLE shipping_detail
    MODIFY state_province VARCHAR(255);

ALTER TABLE `order`
    MODIFY status VARCHAR(255);

ALTER TABLE jwt_blacklist
    MODIFY token VARCHAR(255);

ALTER TABLE `order`
    MODIFY total_price DECIMAL;

ALTER TABLE user_social_account
    MODIFY user_id BIGINT NULL;