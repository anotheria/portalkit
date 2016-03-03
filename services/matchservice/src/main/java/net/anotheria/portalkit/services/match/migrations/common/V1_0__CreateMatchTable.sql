CREATE TABLE match
(
  owner VARCHAR(255) NOT NULL,
  target VARCHAR(255) NOT NULL,
  type INTEGER NOT NULL,
  created BIGINT,
  CONSTRAINT match_pkey PRIMARY KEY (owner, target)
);
CREATE INDEX match_owner_index ON match (owner);