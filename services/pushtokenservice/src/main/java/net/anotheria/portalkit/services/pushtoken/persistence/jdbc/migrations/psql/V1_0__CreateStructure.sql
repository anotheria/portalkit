CREATE TABLE push_token
(
    accid VARCHAR(128) NOT NULL,
    token VARCHAR      NOT NULL,
    PRIMARY KEY (accid, token)
);
CREATE INDEX accid_idx ON push_token (accid);