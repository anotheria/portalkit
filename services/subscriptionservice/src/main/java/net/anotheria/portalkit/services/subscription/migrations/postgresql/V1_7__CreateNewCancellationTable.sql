CREATE TABLE cancellation
(
  userId VARCHAR(255) NOT NULL,
  cancellationOriginalDate TIMESTAMP,
  cancellationExecutionDate TIMESTAMP,
  cancellationReason VARCHAR(255) NOT NULL,
  created TIMESTAMP
);

CREATE INDEX cancellation_userId_idx ON cancellation (userId);
