CREATE TABLE push_token
(
    accid VARCHAR(128) NOT NULL,
    token VARCHAR      NOT NULL,
    PRIMARY KEY (accid, token)
);
CREATE INDEX push_token_accid_idx ON push_token (accid);