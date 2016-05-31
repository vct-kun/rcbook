INSERT INTO user (email, password_hash, role, first_name, last_name, town, country, account)
VALUES ('test@test.com', '$2a$10$ChzgOnCfwBioGU0dbZMj5uzJLsr41kID6xIm1qWkbATWDB/8LvAfC', 'OWNER', 'Van Charles', 'Tran', 'Le Port Marly', 'France', 'PREMIUM');
INSERT INTO user (email, password_hash, role, first_name, last_name, town, country, account)
VALUES ('admin@test.com', '$2a$10$ngCKnYKRRBz3KHjcm2j9HuBCKqA2NdYTNhUtuvgFzFPkJznnjWbv6', 'USER', 'admin', 'admin', 'Paris', 'France', 'FREE');
INSERT INTO user (email, password_hash, role, first_name, last_name, town, country, account)
VALUES ('test2@test.com', '$2a$10$0aE4IlQzZzBPYz5ZSNTlge0vEz.SOoKUBn/6Vze0sboAPLpTslEIu', 'USER', 'Laurent', 'Cobos', 'Poissy', 'France', 'FREE');
INSERT INTO brand (name)
VALUES ('Tamiya');
INSERT INTO brand (name)
VALUES ('Yokomo');
INSERT INTO chassis (brand_id, name)
VALUES (1, 'TRF419');
INSERT INTO chassis (brand_id, name)
VALUES (1, 'TT-01');
INSERT INTO chassis (brand_id, name)
VALUES (2, 'BD7 2016');
INSERT INTO chassis (brand_id, name)
VALUES (2, 'BD7 RS');
INSERT INTO car (chassis_id, user_id)
VALUES (3, 1);
INSERT INTO car (chassis_id, user_id)
VALUES (1, 2);
INSERT INTO club (name, town, country, url, owner_id, logo)
VALUES ('RCA', 'Argenteuil', 'France', 'www.rc-argenteuil.com', 1, 'https://s3.eu-central-1.amazonaws.com/rcbook.bucket/1-Ridelogo_001.jpg');
INSERT INTO race (name, start_date, end_date, track, town, country, club_id, nb_driver, closed, have_fees)
VALUES ('Amicale Spring 2016', '2016-03-23T23:00:00.000Z', '2016-03-24T23:00:00.000Z', 'Gymnase Vallon', 'Argenteuil', 'France', 1, '10', false, false);
INSERT INTO club_users(club_id, user_id)
VALUES (1, 1);
INSERT INTO club_users(club_id, user_id)
VALUES (1, 2);
INSERT INTO club_users(club_id, user_id)
VALUES (1, 3);
INSERT INTO driver(user_id, car_id)
VALUES (2, 2);
INSERT INTO race_joined_driver(race_id, joined_driver_id)
VALUES (1, 1);
-- For MySQL
-- INSERT INTO race_joined_driver(race, joined_driver)
-- VALUES (1, 1);
INSERT INTO motor (name)
VALUES ('GForce - Extreme 13.5T');
INSERT INTO esc (name)
VALUES ('GForce - TS120A');
INSERT INTO setting (id, motor_id, esc_id, car_id, name)
VALUES (1, 1, 1, 1, 'Carpet');