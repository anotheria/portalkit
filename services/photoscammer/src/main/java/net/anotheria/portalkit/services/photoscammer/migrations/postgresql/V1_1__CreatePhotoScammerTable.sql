CREATE TABLE photoscammer
(
    id SERIAL PRIMARY KEY NOT NULL,
    userId VARCHAR (128) NOT NULL,
    photoHash INT,
    perseptiveHash VARCHAR(256) NOT NULL,
    created BIGINT
);

CREATE INDEX photoscammer_id_idx ON photoscammer(id);
CREATE INDEX photoscammer_userId_idx ON photoscammer(userId);
