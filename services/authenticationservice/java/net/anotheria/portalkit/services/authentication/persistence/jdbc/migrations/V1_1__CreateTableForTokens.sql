-- creates table for passwords.
CREATE TABLE auth_token (
  accid VARCHAR(128) NOT NULL,
  token VARCHAR(512),
  dao_created bigint,
  dao_updated bigint,
  PRIMARY KEY(token));
CREATE INDEX auth_token_accid_idx ON auth_token (accid);