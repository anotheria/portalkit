CREATE TABLE photodata
(
    id SERIAL PRIMARY KEY NOT NULL,
    userId VARCHAR (128) NOT NULL,
    photoHash INT,
    perseptiveHash VARCHAR(256) NOT NULL,
    photoId BIGINT
);

CREATE INDEX photodata_id_idx ON photodata(id);
CREATE INDEX photodata_userId_idx ON photodata(userId);
CREATE INDEX photodata_photoId_idx ON photodata(photoId);
