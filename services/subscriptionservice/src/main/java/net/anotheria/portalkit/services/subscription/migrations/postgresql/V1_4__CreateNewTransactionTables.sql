CREATE TABLE sub_transaction
(
     id BIGSERIAL PRIMARY KEY NOT NULL,
     transactionId VARCHAR(128),
     subscriptionId VARCHAR(128),
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
  subscriptionId VARCHAR(128),
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

