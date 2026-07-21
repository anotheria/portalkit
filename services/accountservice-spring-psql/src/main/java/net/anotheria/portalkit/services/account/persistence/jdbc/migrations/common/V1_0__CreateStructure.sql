CREATE TABLE account (
    id VARCHAR(128) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    type integer,
    regts bigint,
    PRIMARY KEY(id)
);
