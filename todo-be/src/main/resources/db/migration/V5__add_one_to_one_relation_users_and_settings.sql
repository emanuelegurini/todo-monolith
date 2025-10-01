CREATE TABLE settings
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    theme        VARCHAR(255) NULL,
    account_type VARCHAR(255) NULL,
    user_id      BIGINT NOT NULL,
    CONSTRAINT pk_settings PRIMARY KEY (id)
);

ALTER TABLE settings
    ADD CONSTRAINT uc_settings_user UNIQUE (user_id);

ALTER TABLE settings
    ADD CONSTRAINT FK_SETTINGS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);