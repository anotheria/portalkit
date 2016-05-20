CREATE TABLE relation
(
  owner VARCHAR(128) NOT NULL,
  partner VARCHAR(128) NOT NULL,
  type VARCHAR(128) NOT NULL,
  dao_created BIGINT,
  dao_updated BIGINT,
  CONSTRAINT relation_pkey PRIMARY KEY (owner, partner, type)
);
CREATE INDEX relation_owner_index ON relation (owner);
CREATE INDEX relation_owner_type_index ON relation (owner, type);
CREATE INDEX relation_partner_index ON relation (partner);
CREATE INDEX relation_partner_type_index ON relation (partner, type);
