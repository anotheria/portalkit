CREATE TABLE account_note
(
    id BIGSERIAL PRIMARY KEY,
    accountId VARCHAR(256) NOT NULL,
    timestamp BIGINT,
    author VARCHAR(64),
    text TEXT
);

CREATE INDEX account_note_id_idx ON account_note(id);