-- adds the last used timestamp which backs the token inventory. Null means the token has not been used since
-- last used tracking was introduced, which is not the same as never used, so it is reported as unknown.
ALTER TABLE auth_token ADD COLUMN last_used_at BIGINT;
