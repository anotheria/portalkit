CREATE TABLE coin_transaction (
  id        VARCHAR(256) PRIMARY KEY NOT NULL,
  accountId VARCHAR(256)             NOT NULL,
  amount    INTEGER                  NOT NULL,
  type      VARCHAR(16)              NOT NULL,
  message   TEXT,
  created   BIGINT                   NOT NULL
);

CREATE INDEX coin_transaction_account_id_index
  ON coin_transaction (accountId);
CREATE INDEX coin_transaction_id_index
  ON coin_transaction (id);

CREATE TABLE coin_balance (
  accountId VARCHAR(256) PRIMARY KEY NOT NULL,
  amount    INTEGER                  NOT NULL
);

CREATE INDEX coin_balance_id_index
  ON coin_balance (accountId);
