create table accountlist (
	owner varchar(50) NOT NULL,
	target varchar(50) NOT NULL,
	listname varchar(128) NOT NULL,
	additionalinfo longvarchar,
	primary key (owner, target, listname)
);
CREATE INDEX owner_list_idx ON accountlist (owner, listname);
CREATE INDEX reverse_owner_list_idx ON accountlist (target, listname);

