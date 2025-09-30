CREATE TABLE projects
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    user_id       BIGINT NULL,
    CONSTRAINT pk_projects PRIMARY KEY (id)
);

CREATE TABLE users
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255) NULL,
    surname VARCHAR(255) NULL,
    email   VARCHAR(255) NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE projects
    ADD CONSTRAINT FK_PROJECTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);