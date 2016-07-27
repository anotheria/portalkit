package net.anotheria.portalkit.services.foreignid.persistence.mongo;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseMongoPersistenceServiceImpl;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceService;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceServiceException;
import net.anotheria.portalkit.services.foreignid.persistence.mongo.entity.ForeignIdEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman Stetsiuk on 7/25/16.
 */
public class MongoForeignIdPersistenceServiceImpl  extends BaseMongoPersistenceServiceImpl implements ForeignIdPersistenceService {
    private Logger log = LoggerFactory.getLogger(MongoForeignIdPersistenceServiceImpl.class);

    private final MongoForeignIdDAO foreignIdDAO;

    public MongoForeignIdPersistenceServiceImpl(){
        super("pk-mongo-foreignid");
        foreignIdDAO = new MongoForeignIdDAOImpl();
    }

    @Override
    public void link(AccountId accountId, int sid, String fid) throws ForeignIdPersistenceServiceException {
        try {
            Datastore datastore = connect();
            ForeignIdEntity savedForeignId = getForeignIdBySidAndFid(sid, fid);
            if (savedForeignId == null) {
                //create new entity
                ForeignIdEntity entity = new ForeignIdEntity(new ObjectId());
                entity.setAccountId(accountId);
                entity.setForeignid(fid);
                entity.setSourceId(sid);

                entity.setDaoCreated(System.currentTimeMillis());
                entity.setDaoUpdated(System.currentTimeMillis());

                foreignIdDAO.createEntity(datastore, entity);
            } else {
                throw new ForeignIdPersistenceServiceException("ForeignId entity already exists with sid:"+sid+" fid:"+fid);
            }

        } catch (MongoDaoException e) {
            log.error("Error while saving foreignId to mongo", e);
            throw new ForeignIdPersistenceServiceException("Error while saving foreignId to mongo", e);
        }
    }

    private ForeignIdEntity getForeignIdBySidAndFid(int sid, String fid) throws ForeignIdPersistenceServiceException {
        try {
            Datastore datastore = connect();
            ForeignIdEntity foreignEntity = foreignIdDAO.getForeignIdBySidAndFid(datastore, sid, fid);
            if (foreignEntity == null) return null;

            return foreignEntity;
        } catch (MongoDaoException e) {
            log.error("Can't find account by sid: " + sid +" and fid: "+ fid, e);
            throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
        }
    }


    @Override
    public AccountId getAccountIdByForeignId(int sid, String fid) throws ForeignIdPersistenceServiceException {
        try {
            Datastore datastore = connect();
            AccountId accountId = foreignIdDAO.getAccountIdByForeignId(datastore, sid, fid);
            if (accountId == null) return null;

            return accountId;
        } catch (MongoDaoException e) {
            throw new ForeignIdPersistenceServiceException("Can't find account by sid: " + sid +" and fid: "+ fid, e);
        }
    }

    @Override
    public List<ForeignId> getForeignIdsByAccountId(AccountId accountId) throws ForeignIdPersistenceServiceException {
        try {
            Datastore datastore = connect();
            List<ForeignIdEntity> foreignIds= foreignIdDAO.getForeignIdsByAccountId(datastore, accountId);
            if (foreignIds == null) return null;

            return convertToForeignIds(foreignIds);
        } catch (MongoDaoException e) {
            throw new ForeignIdPersistenceServiceException("Can't find foreignIds by accid: " + accountId, e);
        }
    }

    private List<ForeignId> convertToForeignIds(List<ForeignIdEntity> foreignIds) {
        List<ForeignId> result = new ArrayList<>();
        for (ForeignIdEntity foreignId : foreignIds) {
            result.add(new ForeignId(foreignId.getAccountId(), foreignId.getSourceId(), foreignId.getForeignid()));
        }
        return result;
    }

    @Override
    public void unlink(int sid, String fid) throws ForeignIdPersistenceServiceException {
        try {
            Datastore datastore = connect();
            AccountId accountId = foreignIdDAO.getAccountIdByForeignId(datastore, sid, fid);
            foreignIdDAO.deleteEntity(datastore, accountId, sid, fid);
        } catch (MongoDaoException e) {
            log.error("Can't delete account with sid={}, fid={}", sid, fid);
            throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void unlink(AccountId accountId, int sid, String fid) throws ForeignIdPersistenceServiceException {
        try {
            Datastore datastore = connect();
            foreignIdDAO.deleteEntity(datastore, accountId, sid, fid);
        } catch (MongoDaoException e) {
            log.error("Can't delete account with accid={}, sid={}, fid={}", accountId, sid, fid);
            throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
        }
    }
}
