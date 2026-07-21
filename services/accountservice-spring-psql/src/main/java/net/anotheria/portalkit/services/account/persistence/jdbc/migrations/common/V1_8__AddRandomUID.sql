ALTER TABLE account add column randomUID integer;
UPDATE account SET randomUID = 0 WHERE randomUID IS NULL;