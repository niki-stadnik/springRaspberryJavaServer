CREATE TABLE app_variables
(
    key        VARCHAR(255) PRIMARY KEY,
    value      TEXT,
    updated_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO app_variables (key, value)
VALUES ('bathroom.fan.1.auto', 'false'),
       ('bathroom.fan.1.minHum', '0'),
       ('bathroom.fan.1.maxHum', '0'),
       ('bathroom.fan.2.auto', 'false'),
       ('bathroom.fan.2.minHum', '0'),
       ('bathroom.fan.2.maxHum', '0');