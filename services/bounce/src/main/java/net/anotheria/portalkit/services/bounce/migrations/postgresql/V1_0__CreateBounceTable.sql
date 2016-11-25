CREATE TABLE bounce
(
    email VARCHAR(256) PRIMARY KEY NOT NULL,
    errorCode INT,
    errorMessage TEXT,
    created BIGINT
);

CREATE INDEX bounce_id_idx ON bounce(email);