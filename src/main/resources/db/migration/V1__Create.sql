CREATE TABLE storage (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    timestamp timestamp default current_timestamp,
    bath_temp_1 decimal,
    bath_temp_2 decimal,
    bath_hum_1 decimal,
    bath_hum_2 decimal,
    bath_fan boolean,
    light_0 boolean,
    light_1 boolean,
    light_2 boolean,
    light_3 boolean,
    light_4 boolean,
    light_5 boolean,
    light_6 boolean
);