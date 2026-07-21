-- Regression guard: simulates the embedding host's own Flyway migration on the classpath
-- (like houseid's db/migration/postgresql/V1_0__create_received_events.sql). The account (modern)
-- Flyway must scan ONLY its own classpath location and must NOT pick this up (the old flyway 2.0.3 did,
-- which broke embedding). This file shares version 1.0 with the account V1_0 on purpose.
CREATE TABLE foreign_host_probe (id integer);
