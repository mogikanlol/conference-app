INSERT INTO users (id, email, firstname, lastname, password, username, enabled, confirmed)
VALUES
(1, 'creator@test.com', 'Conference', 'Creator',
'$2a$10$vXo124lhpeiifSslvwROqutxcIiyQliIJq4lozW7hlJbk.Zm3xBIa', 'creator@test.com', TRUE, FALSE),
(2, 'plain_user1@test.com', 'Plain1', 'User1',
'$2a$10$vXo124lhpeiifSslvwROqutxcIiyQliIJq4lozW7hlJbk.Zm3xBIa', 'plain_user1@test.com', TRUE, FALSE),
(3, 'first_reviewer@test.com', 'First', 'Reviewer',
'$2a$10$vXo124lhpeiifSslvwROqutxcIiyQliIJq4lozW7hlJbk.Zm3xBIa', 'first_reviewer@test.com', TRUE, FALSE),
(4, 'second_reviewer@test.com', 'Second', 'Reviewer',
'$2a$10$vXo124lhpeiifSslvwROqutxcIiyQliIJq4lozW7hlJbk.Zm3xBIa', 'second_reviewer@test.com', TRUE, FALSE),
(5, 'plain_user2@test.com', 'Plain2', 'User2',
'$2a$10$vXo124lhpeiifSslvwROqutxcIiyQliIJq4lozW7hlJbk.Zm3xBIa', 'plain_user2@test.com', TRUE, FALSE);
ALTER sequence users_id_seq restart with 6;

INSERT INTO conference_request_status (id, name)
VALUES
(1, 'ACCEPTED'),
(2, 'DECLINED'),
(3, 'PENDING');

INSERT INTO submission_status (id, name)
VALUES
(1, 'ACCEPT'),
(2, 'REJECT'),
(3, 'PENDING');

INSERT INTO review_status(id, name)
VALUES
(1, 'REJECT'),
(2, 'PROBABLY_REJECT'),
(3, 'NO_DECISION'),
(4, 'PROBABLY_ACCEPT'),
(5, 'ACCEPT');

INSERT INTO conference (id, acronym, city, country, title, web_page, expiration_date)
VALUES
(1, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-06-20'),
(2, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-05-16'),
(3, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-05-16'),
(4, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-05-16'),
(5, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-05-16'),
(6, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-05-16'),
(7, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-05-16'),
(8, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-05-16'),
(9, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru', '2018-05-16'),
(10, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru','2018-05-16'),
(11, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru','2018-05-16'),
(12, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru','2018-05-16'),
(13, 'MyConf', 'Saratov', 'Russia', 'My conference', 'www.my-conf.ucoz.ru','2018-05-16');
ALTER sequence conference_id_seq restart with 14;


INSERT INTO submission (id, reviewable, title, conference_id, status_id, author_id)
VALUES (1, FALSE, 'Test file', 1, 3, 1);
ALTER sequence submission_id_seq restart with 2;

INSERT INTO authority (id, name)
VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

INSERT INTO role_list (id)
VALUES (1), (2), (3),(4), (5);
ALTER sequence role_list_id_seq restart with 6;

INSERT INTO conference_role (id, name)
VALUES
(1, 'SUBMITTER'),
(2, 'REVIEWER'),
(3, 'CREATOR'),
(4, 'ADMIN');

INSERT INTO submission_role (id, name)
VALUES
(1, 'AUTHOR'),
(2, 'REVIEWER');

INSERT INTO role_list_roles (role_list_id, role_id)
VALUES
(1, 3),
(2, 1),
(3, 2),
(4, 2),
(5, 1);

INSERT INTO conference_user_roles (id, conference_id, role_list_id, user_id)
VALUES
-- 1 - creator
(1, 1, 1, 1),
-- 3 - first author
(2, 1, 3, 3),
-- 4 - second author
(3, 1, 4, 4),
-- 2 - participant
(4, 1, 2, 2),

(5,1, 5, 5);
ALTER sequence conference_user_roles_id_seq restart with 6;


INSERT INTO submission_user_roles (id, role_id, submission_id, user_id)
VALUES (1, 1, 1, 1);
ALTER sequence submission_user_roles_id_seq restart with 2;

INSERT INTO user_authority (user_id, authority_id)
VALUES
(1, 1),
(1, 2),
(3, 1),
(4, 1);

INSERT INTO user_role_in_conf (id, conference_id, role, user_id)
VALUES
-- 1 - creator
(1, 1, 3, 1),
-- 3 - first author
(2, 1, 2, 3),
-- 4 - second author
(3, 1, 2, 4),
-- 2 - participant
(4, 1, 1, 2),

(5,1, 1, 5);
ALTER sequence user_role_in_conf_id_seq restart with 6;


INSERT INTO user_role_in_subm (id, role, submission_id, user_id)
VALUES (1, 'AUTHOR', 1, 1);
ALTER sequence user_role_in_subm_id_seq restart with 2;