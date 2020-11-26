DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

CREATE TABLE tasks(
    id INT AUTO_INCREMENT PRIMARY KEY,
    completed BOOLEAN DEFAULT false,
    task VARCHAR(250) NOT NULL,
    user_id INT NOT NULL
);

INSERT INTO users (name) VALUES
('test user');

INSERT INTO tasks (completed, task, user_id) VALUES
(false, 'test task', 1),
(true, 'other test task', 1);

