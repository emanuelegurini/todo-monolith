CREATE TABLE project_tags
(
    project_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,
    CONSTRAINT pk_project_tags PRIMARY KEY (project_id, tag_id)
);

CREATE TABLE tags
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    color         VARCHAR(255) NULL,
    CONSTRAINT pk_tags PRIMARY KEY (id)
);

ALTER TABLE project_tags
    ADD CONSTRAINT fk_protag_on_project FOREIGN KEY (project_id) REFERENCES projects (id);

ALTER TABLE project_tags
    ADD CONSTRAINT fk_protag_on_tag FOREIGN KEY (tag_id) REFERENCES tags (id);