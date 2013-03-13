create table ticketsapproval (
	ticketid varchar(255) not null,
	status varchar(50) not null,
	referenceid varchar(255) not null,
	referencetype bigint not null,
	ts bigint not null,
	primary key (ticketid)
);

create index referenceid_idx ON ticketsapproval (referenceid, referencetype);
