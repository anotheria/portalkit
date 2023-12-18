package net.anotheria.portalkit.services.foreignid.persistence.mongo;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.persistence.mongo.entity.ForeignIdEntity;
import org.mongodb.morphia.Datastore;

import java.util.List;

public interface MongoForeignIdDAO {

    ForeignIdEntity createEntity(Datastore datastore, ForeignIdEntity entity) throws MongoDaoException;

    AccountId getAccountIdByForeignId(Datastore datastore, int sid, String fid) throws MongoDaoException;

    List<ForeignIdEntity> getForeignIdsByAccountId(Datastore datastore, AccountId accId) throws MongoDaoException;

    long getForeignIdsCount(Datastore datastore) throws MongoDaoException;

    void deleteEntity(Datastore datastore, AccountId accountId, int sid, String fid) throws MongoDaoException;

    ForeignIdEntity getForeignIdBySidAndFid(Datastore datastore, int sid, String fid) throws MongoDaoException;
}
