package net.anotheria.portalkit.services.accountsettings.persistence;

import java.util.List;

import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.mongo.GenericMongoServiceImpl;
import net.anotheria.portalkit.services.storage.query.CompositeQuery;
import net.anotheria.portalkit.services.storage.query.EqualQuery;
import net.anotheria.portalkit.services.storage.query.QueryBuilder;

/**
 * Mongo implementation of Account settings persistence service.
 * 
 * @author dagafonov
 */
public class MongoAccountSettingsPersistenceServiceImpl extends GenericMongoServiceImpl<Dataspace> implements AccountSettingsPersistenceService {

	/**
	 * Storage configuration name.
	 */
	private static final String MONGO_SERVICE_COLLECTION_CONFIG_NAME = "pk-storage-mongo-accountsettings";
	
	/**
	 * Default constructor.
	 */
	public MongoAccountSettingsPersistenceServiceImpl() {
		super(Dataspace.class, MONGO_SERVICE_COLLECTION_CONFIG_NAME, null, null, null);
	}

	@Override
	public Dataspace loadDataspace(AccountId owner, int dataspaceId) throws AccountSettingsPersistenceServiceException {
		QueryBuilder builder = QueryBuilder.create();
		try {
			builder.add(CompositeQuery.create(EqualQuery.create("key.dataspaceId", dataspaceId), EqualQuery.create("key.accountId", owner.getInternalId())));
			List<Dataspace> dataspaces = find(builder.build());
			if (dataspaces.isEmpty()) {
				return null;
			}
			return dataspaces.get(0);
		} catch (StorageException ex) {
			throw new AccountSettingsPersistenceServiceException("find(" + builder + ") failed", ex);
		}
	}

	@Override
	public void saveDataspace(Dataspace dataspace) throws AccountSettingsPersistenceServiceException {
		try {
			save(dataspace);
		} catch (StorageException ex) {
			throw new AccountSettingsPersistenceServiceException("save(" + dataspace + ") failed", ex);
		}
	}

}
