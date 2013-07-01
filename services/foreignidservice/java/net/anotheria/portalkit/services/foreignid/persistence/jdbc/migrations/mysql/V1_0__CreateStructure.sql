CREATE TABLE foreignid (
	accid VARCHAR(128) NOT NULL,
	sourceid integer NOT NULL,
	foreignid VARCHAR(256) NOT NULL,
	primary key (sourceid, foreignid)
) default character set latin1;

CREATE INDEX accid_idx ON foreignid (accid);