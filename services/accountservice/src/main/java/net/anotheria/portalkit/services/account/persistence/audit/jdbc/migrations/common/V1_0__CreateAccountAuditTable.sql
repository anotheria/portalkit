CREATE TABLE account_audit
(
    id BIGSERIAL PRIMARY KEY,
    accountId VARCHAR(256) NOT NULL,
    oldStatus BIGINT,
    newStatus BIGINT,
    statusRemoved BIGINT,
    statusAdded BIGINT,
    timestamp BIGINT
);

CREATE INDEX account_audit_id_idx ON account_audit(id);