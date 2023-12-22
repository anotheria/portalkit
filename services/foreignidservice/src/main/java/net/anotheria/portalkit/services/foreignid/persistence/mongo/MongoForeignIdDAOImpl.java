package net.anotheria.portalkit.services.foreignid.persistence.mongo;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.mongo.BaseEntity;
import net.anotheria.portalkit.services.foreignid.persistence.mongo.entity.ForeignIdEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Roman Stetsiuk on 7/25/16.
 */
public class MongoForeignIdDAOImpl implements MongoForeignIdDAO {
    private final Logger log;

    public MongoForeignIdDAOImpl() {
        log = LoggerFactory.getLogger(MongoForeignIdDAOImpl.class);
    }

    @Override
    public ForeignIdEntity createEntity(Datastore datastore, ForeignIdEntity entity) throws MongoDaoException {
        if (entity == null) {
            throw new IllegalArgumentException("foreignId is null.");
        }
        try {
            datastore.save(entity);
        } catch (DuplicateKeyException e) {
            log.error("id already exists " + entity.getId());
            throw new MongoDaoException("Id already exists");
        } catch (MongoException e) {
            log.error("Can't store " + entity.toString());
            throw new MongoDaoException("Can't store " + entity.toString());
        }
        return null;
    }

    @Override
    public ForeignIdEntity getForeignIdBySidAndFid(Datastore datastore, int sid, String fid) throws MongoDaoException {
        if (fid == null) {
            throw new IllegalArgumentException("fid is null.");
        }
        try {
            List<ForeignIdEntity> result = datastore
                    .find(ForeignIdEntity.class)
                    .filter(Filters.eq("sourceId", sid), Filters.eq("foreignid", fid))
                    .iterator()
                    .toList();

            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);
        } catch (MongoException e) {
            log.error("Can't find foreignId with sid: " + sid + " fid:" +fid);
            throw new MongoDaoException("Can't find foreignId with sid: " + sid + " fid:" + fid);
        }
    }

    @Override
    public AccountId getAccountIdByForeignId(Datastore datastore, int sid, String fid) throws MongoDaoException {
        if (fid == null) {
            throw new IllegalArgumentException("fid is null.");
        }
        try {
            ForeignIdEntity result = getForeignIdBySidAndFid(datastore, sid, fid);
            return result == null ? null : result.getAccountId();
        } catch (MongoException e) {
            throw new MongoDaoException("Can't find foreignId with sid: " + sid + " fid:" + fid);
        }
    }

    @Override
    public List<ForeignIdEntity> getForeignIdsByAccountId(Datastore datastore, AccountId accId) throws MongoDaoException {
        if (accId == null) {
            throw new IllegalArgumentException("accId is null.");
        }
        try {
            List<ForeignIdEntity> result = datastore
                    .find(ForeignIdEntity.class)
                    .filter(Filters.eq("accountId", accId))
                    .stream().collect(Collectors.toList());

            if (result.isEmpty()) {
                throw new MongoDaoException("Can't find foreignIds with accid: " + accId);
            }
            return result;
        } catch (MongoException e) {
            throw new MongoDaoException("Can't find foreignId with accid: " + accId);
        }
    }

    @Override
    public long getForeignIdsCount(Datastore datastore) throws MongoDaoException {
        try {
            return datastore.createQuery(ForeignIdEntity.class).countAll();
        } catch (MongoException e) {
            throw new MongoDaoException("Can't get foreignIds count");
        }
    }

    @Override
    public void deleteEntity(Datastore datastore, AccountId accountId, int sid, String fid) throws MongoDaoException {
        if (accountId == null) {
            throw new IllegalArgumentException("Entity accid is null.");
        }
        try {
            List<? extends BaseEntity> result = datastore.find(ForeignIdEntity.class)
                    .filter(Filters.eq("accountId", accountId),Filters.eq("sourceId", sid),Filters.eq("foreignid", fid))
                    .stream().collect(Collectors.toList());
            if (result.isEmpty()) {
                return;
            }
            datastore.delete(result.get(0));
        } catch (MongoException e) {
            log.error("Can't delete foreignId with accid={}, fid={}, sid={}", accountId, fid, sid );
            throw new MongoDaoException("Can't delete foreignId");
        }
    }

}
