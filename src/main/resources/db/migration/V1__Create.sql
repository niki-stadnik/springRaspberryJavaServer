CREATE TABLE storage (
    id SERIAL PRIMARY KEY,
    timestamp timestamp default current_timestamp,
    bath_temp decimal,
    bath_hum decimal,
    bath_light decimal,
    bath_fan boolean
);