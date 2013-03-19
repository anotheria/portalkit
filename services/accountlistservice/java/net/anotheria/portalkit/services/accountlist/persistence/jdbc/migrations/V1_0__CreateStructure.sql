create table accountlist (
	owner varchar(128) NOT NULL,
	target varchar(128) NOT NULL,
	listname varchar(256) NOT NULL,
	additionalinfo varchar NULL,
	creationtimestamp bigint not null,
	primary key (owner, target, listname)
);
CREATE INDEX owner_list_idx ON accountlist (owner, listname);
CREATE INDEX reverse_owner_list_idx ON accountlist (target, listname);

