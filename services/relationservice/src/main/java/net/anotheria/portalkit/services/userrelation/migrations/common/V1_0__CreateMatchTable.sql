CREATE TABLE userrelation
(
  owner VARCHAR(128) NOT NULL,
  partner VARCHAR(128) NOT NULL,
  type VARCHAR(128) NOT NULL,
  dao_created BIGINT,
  dao_updated BIGINT,
  CONSTRAINT userrelation_pkey PRIMARY KEY (owner, partner, type)
);
CREATE INDEX userrelation_owner_index ON userrelation (owner);
CREATE INDEX userrelation_owner_type_index ON userrelation (owner, type);
CREATE INDEX userrelation_partner_index ON userrelation (partner);
CREATE INDEX userrelation_partner_type_index ON userrelation (partner, type);
