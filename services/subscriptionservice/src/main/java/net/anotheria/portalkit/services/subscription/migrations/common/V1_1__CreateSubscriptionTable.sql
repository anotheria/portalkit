CREATE TABLE sub_subscription
(
  accountId VARCHAR(255) NOT NULL,
  subscriptionId VARCHAR(255) PRIMARY KEY NOT NULL,
  productId VARCHAR(10),
  expirationTimestamp BIGINT,
  lastProlongationTimestamp BIGINT,
  purchaseTimestamp BIGINT,
  cancelationPeriodInMillis BIGINT,
  cancelationTimestamp BIGINT,
  prolongationCount INT,
  active BOOLEAN,
  preparedForCancelation BOOLEAN,
  amountInCents INT,
  currency VARCHAR(5),
  duration VARCHAR(20),
  dao_created TIMESTAMP,
  dao_updated TIMESTAMP

);

CREATE INDEX subs_accountId_idx ON sub_subscription (accountId);
CREATE INDEX subs_active_idx ON sub_subscription(active);