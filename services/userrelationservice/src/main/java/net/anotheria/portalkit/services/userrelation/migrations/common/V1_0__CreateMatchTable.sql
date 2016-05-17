CREATE TABLE userrelation
(
  owner VARCHAR(128) NOT NULL,
  target VARCHAR(128) NOT NULL,
  type VARCHAR(128) NOT NULL,
  dao_created BIGINT,
  dao_updated BIGINT,
  CONSTRAINT userrelation_pkey PRIMARY KEY (owner, target, type)
);
CREATE INDEX userrelation_owner_index ON userrelation (owner);
CREATE INDEX userrelation_owner_type_index ON userrelation (owner, type);
CREATE INDEX userrelation_target_index ON userrelation (target);
CREATE INDEX userrelation_target_type_index ON userrelation (target, type);
