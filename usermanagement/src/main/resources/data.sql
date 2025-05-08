INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');

-- Password for both users is 'password'
INSERT INTO users (username, password) VALUES 
('admin', '$2a$10$X8jIgC15hE9Q1W0.Fv7Bw.1Lr3LcE5k5h5tN5Nc1dZz1lB7yKJjXa'),
('user', '$2a$10$X8jIgC15hE9Q1W0.Fv7Bw.1Lr3LcE5k5h5tN5Nc1dZz1lB7yKJjXa');

INSERT INTO users_roles (user_id, role_id) VALUES 
(1, (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')),
(2, (SELECT id FROM roles WHERE name = 'ROLE_USER'));