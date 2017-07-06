CREATE TABLE cancellation
(
  userId VARCHAR(255) PRIMARY KEY NOT NULL,
  cancellationOriginalDate BIGINT,
  cancellationExecutionDate BIGINT,
  cancellationReason VARCHAR(255) NOT NULL,
  created BIGINT
);

CREATE INDEX cancellation_userId_idx ON cancellation (userId);
