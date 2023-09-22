-- All API Roles Example Accoutns
-- (Hash equals role, via BcryptUtil.bcrypthash())
INSERT INTO test_user (id, password, role, username)
VALUES (nextval('test_user_seq'), '$2a$10$TEEl8q8HYAt./K12Po9Mne3kK.5vdE71oMzmEALjL4VP/.XdS3jAW', 'admin', 'admin');

INSERT INTO test_user (id, password, role, username)
VALUES (nextval('test_user_seq'), '$2a$10$hfZGBiiTWvyfmXHskEklzOOcVSD/rj2vRZJqHHhEF69SutOTAuKhm', 'moderator', 'moderator');

INSERT INTO test_user (id, password, role, username)
VALUES (nextval('test_user_seq'), '$2a$10$WHAVsEibc3WtvbDW0wZImu0iI2D3CnibDQ7Bj43Feaz3N6D309v5K', 'user', 'user');

INSERT INTO test_user (id, password, role, username)
VALUES (nextval('test_user_seq'), '$2a$10$KVAO9nzNMtYlxUiC1ZNNTOyaf4kPd1A4zJBnP7ZHsUAhtpzWnYA6e', 'user', 'DanielUser');
