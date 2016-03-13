CREATE TABLE sub_transaction_log
(
  accountId VARCHAR(128) NOT NULL,
  subscriptionId VARCHAR(128),
  transactionId VARCHAR(256) NOT NULL,
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

