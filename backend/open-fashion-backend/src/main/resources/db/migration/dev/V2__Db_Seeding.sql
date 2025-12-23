INSERT INTO role (name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO "user" (
    username,
    password,
    email,
    first_name,
    last_name,
    phone_number,
    enabled,
    account_not_expired,
    account_not_locked,
    credentials_not_expired
) VALUES (
             'demouser',
             'password',
             'demo@example.com',
             'John',
             'Doe',
             '+1555123456',
             true, true, true, true
         );

INSERT INTO user_role (user_id, role_id)
VALUES (
           (SELECT id FROM "user" WHERE username = 'demouser'),
           (SELECT id FROM role WHERE name = 'ROLE_USER')
       );

INSERT INTO user_address (
    user_id,
    address_line1,
    address_line2,
    city,
    postal_code,
    country,
    is_default
) VALUES (
             (SELECT id FROM "user" WHERE username = 'demouser'),
             '123 Demo Lane',
             'Apt 4B',
             'Tech City',
             '90210',
             'USA',
             true
         );