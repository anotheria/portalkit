CREATE TABLE sub_transaction
(
     transactionId BIGSERIAL PRIMARY KEY NOT NULL,
     subscriptionId VARCHAR(128),
     accountId VARCHAR(128) NOT NULL,
     productId VARCHAR(10),
     transaction_timestamp BIGINT,
     prolongationCount INT,
     amount INT
);

CREATE INDEX subs_transaction_accountId_idx ON sub_transaction (accountId);
CREATE INDEX subs_transaction_subscriptionId_idx ON sub_transaction (subscriptionId);