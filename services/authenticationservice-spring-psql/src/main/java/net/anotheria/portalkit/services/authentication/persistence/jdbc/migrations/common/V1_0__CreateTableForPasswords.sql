-- creates table for passwords.
CREATE TABLE auth_passwd (
  accid VARCHAR(128) NOT NULL,
  password VARCHAR(256),
  dao_created bigint,
  dao_updated bigint,
  PRIMARY KEY(accid));
