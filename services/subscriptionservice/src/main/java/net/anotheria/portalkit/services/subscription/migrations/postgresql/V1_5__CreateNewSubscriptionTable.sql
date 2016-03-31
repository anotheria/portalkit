drop table sub_subscription, sub_transaction, sub_transaction_log;

CREATE TABLE sub_subscription
(
  accountId VARCHAR(255) NOT NULL,
  subscriptionId BIGSERIAL PRIMARY KEY NOT NULL,
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

CREATE TABLE sub_transaction
(
     id BIGSERIAL PRIMARY KEY NOT NULL,
     transactionId VARCHAR(128),
     subscriptionId BIGINT,
     accountId VARCHAR(128) NOT NULL,
     productId VARCHAR(10),
     transaction_timestamp BIGINT,
     prolongationCount INT,
     amount INT
);

CREATE INDEX subs_transaction_accountId_idx ON sub_transaction (accountId);
CREATE INDEX subs_transaction_subscriptionId_idx ON sub_transaction (subscriptionId);

CREATE TABLE sub_transaction_log
(
  technicalId BIGSERIAL PRIMARY KEY NOT NULL,
  accountId VARCHAR(128) NOT NULL,
  subscriptionId BIGINT,
  transactionId VARCHAR(128) NOT NULL,
  productId VARCHAR(10),
  timestamp BIGINT,
  action VARCHAR(20),
  message VARCHAR,
  dao_created TIMESTAMP,
  dao_updated TIMESTAMP

);

CREATE INDEX sub_accountId_idx ON sub_transaction_log (accountId);
CREATE INDEX sub_subscriptionId_idx ON sub_transaction_log (subscriptionId);
CREATE INDEX sub_transactionId_idx ON sub_transaction_log (transactionId);

