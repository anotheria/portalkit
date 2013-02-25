create table ticketsapproval (
	ticketid varchar(255) NOT NULL,
	status varchar(50) NOT NULL,
	referenceid varchar(255) NOT NULL,
	referencetype bigint,
	primary key (ticketid)
);

create index referenceid_idx ON ticketsapproval (referenceid);

