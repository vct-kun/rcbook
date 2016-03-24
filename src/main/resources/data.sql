INSERT INTO user (email, password_hash, role, first_name, last_name, town, country)
VALUES ('demo@localhost', '$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C', 'ADMIN', 'demo', 'demo', 'Paris', 'France');
INSERT INTO user (email, password_hash, role, first_name, last_name, town, country)
VALUES ('test@test.com', '$2a$10$ChzgOnCfwBioGU0dbZMj5uzJLsr41kID6xIm1qWkbATWDB/8LvAfC', 'OWNER', 'Van Charles', 'Tran', 'Le Port Marly', 'France');
INSERT INTO user (email, password_hash, role, first_name, last_name, town, country)
VALUES ('admin@test.com', '$2a$10$ngCKnYKRRBz3KHjcm2j9HuBCKqA2NdYTNhUtuvgFzFPkJznnjWbv6', 'USER', 'admin', 'admin', 'Paris', 'France');
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
VALUES (3, 2);
INSERT INTO club (name, town, country, url, owner_id)
VALUES ('RCA', 'Argenteuil', 'France', 'www.rc-argenteuil.com', 2);
-- INSERT INTO race (startDate, endDate, track, town, country, club_id, nbDriver)
-- VALUES ('2016-03-23T23:00:00.000Z', '2016-03-24T23:00:00.000Z', 'Gymnase Vallon', 'Argenteuil', 'France', '1', '10');