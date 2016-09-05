CREATE TABLE detection_score
(
    id BIGINT auto_increment PRIMARY KEY NOT NULL,
    detector_name VARCHAR(256) NOT NULL,
    user_record_id VARCHAR(256) NOT NULL,
    score INT NOT NULL,
    timestamp BIGINT
);

CREATE INDEX detection_score_id_idx ON detection_score(id);