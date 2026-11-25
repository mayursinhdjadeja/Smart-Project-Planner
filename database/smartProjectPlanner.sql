DROP DATABASE IF EXISTS smartProjectPlanner;

CREATE DATABASE smartProjectPlanner;
USE smartProjectPlanner;

CREATE TABLE projects 
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    start_date DATE NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'TODO',   -- TODO / IN_PROGRESS / DONE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks 
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL,
    priority VARCHAR(10) NOT NULL DEFAULT 'MEDIUM',  -- LOW / MEDIUM / HIGH
    status VARCHAR(20) NOT NULL DEFAULT 'TODO',       -- TODO / IN_PROGRESS / DONE
    due_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_project 
        FOREIGN KEY (project_id) REFERENCES projects(id) 
        ON DELETE CASCADE
);