ALTER TABLE user_scam_record ADD COLUMN checked BOOLEAN;
UPDATE user_scam_record SET checked = FALSE WHERE checked IS NULL;