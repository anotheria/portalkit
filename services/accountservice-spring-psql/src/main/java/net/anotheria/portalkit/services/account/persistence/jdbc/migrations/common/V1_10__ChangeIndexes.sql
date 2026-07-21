DROP INDEX email_idx;
DROP INDEX name_idx;
CREATE INDEX email_idx ON account (email);
CREATE INDEX name_idx ON account (name);