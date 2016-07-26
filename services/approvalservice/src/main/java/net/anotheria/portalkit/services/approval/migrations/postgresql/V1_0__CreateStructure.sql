CREATE TABLE ticketsapproval
(
	ticketid BIGSERIAL PRIMARY KEY NOT NULL,
	status VARCHAR(128) not null,
	type VARCHAR(128) not null,
	agent VARCHAR(128),
	referenceid varchar(255) not null,
	referencetype bigint not null,
	locale VARCHAR(128) not null,
	created bigint not null,
	presentation bigint not null,
	fulfillment bigint not null
);

create index referenceid_idx ON ticketsapproval (referenceid, referencetype);
