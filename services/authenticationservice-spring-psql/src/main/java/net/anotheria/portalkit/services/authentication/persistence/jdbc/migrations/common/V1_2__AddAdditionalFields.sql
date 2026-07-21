ALTER TABLE auth_token ADD COLUMN expiryTimestamp BIGINT;
ALTER TABLE auth_token ADD COLUMN multiUse BOOLEAN;
ALTER TABLE auth_token ADD COLUMN exclusive BOOLEAN;
ALTER TABLE auth_token ADD COLUMN exclusiveInType BOOLEAN;
ALTER TABLE auth_token ADD COLUMN type INTEGER;