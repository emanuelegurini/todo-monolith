ALTER TABLE tasks
    ADD priority VARCHAR(255) NULL;

ALTER TABLE tasks
    ADD status VARCHAR(255) NULL;

ALTER TABLE projects
    ADD status VARCHAR(255) NULL;