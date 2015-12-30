create table recordcollection (
	ownerid varchar(128) not null,
	collectionid varchar(128) not null,
	recordid varchar(128) not null,
	recordtype integer not null,
	recordvalue varchar,
	primary key (ownerid, collectionid, recordid)
);

create index recordcollection_idx ON recordcollection (ownerid, collectionid);
