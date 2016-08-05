CREATE TABLE user_scam_record
(
    id VARCHAR(256) PRIMARY KEY NOT NULL,
    total_score INT NOT NULL,
    created BIGINT,
    updated BIGINT
);

CREATE INDEX user_scam_record_id_idx ON user_scam_record(id);