package net.anotheria.portalkit.services.foreignid;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceService;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceServiceException;

import java.util.ArrayList;
import java.util.List;

/**
 * ForeignId Service implementation.
 * 
 * @author dagafonov
 */
@Monitor(subsystem = "portalkit")
public class ForeignIdServiceImpl implements ForeignIdService {

	/**
	 * Persistence service.
	 */
	private ForeignIdPersistenceService persistenceService;

	/**
	 * Cache by {@link AccountId}.
	 */
	private Cache<AccountId, List<ForeignId>> cacheByAccountId;

	/**
	 * Cache by string representation of all foreign id fields.
	 */
	private Cache<String, AccountId> cacheByForeignId;

	/**
	 * Default Constructor.
	 */
	public ForeignIdServiceImpl() {

		cacheByAccountId = Caches.createHardwiredCache("foreignidservice-cacheaccountid");
		cacheByForeignId = Caches.createHardwiredCache("foreignidservice-cacheforeignid");

		try {
			persistenceService = MetaFactory.get(ForeignIdPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service ", e);
		}
	}

	/**
	 * Returns String representation of all foreign id fields.
	 * 
	 * @param foreignId
	 * @param sourceId
	 * @return
	 */
	private String getForeignIdString(String foreignId, int sourceId) {
		return "fid_" + sourceId + "_" + foreignId;
	}

	@Override
	public void addForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException {
		try {
			persistenceService.link(accId, sourceId, foreignId);
			List<ForeignId> list = cacheByAccountId.get(accId);
			if (list == null) {
				list = new ArrayList<ForeignId>();
				cacheByAccountId.put(accId, list);
			}
			list.add(new ForeignId(accId, sourceId, foreignId));
			cacheByForeignId.put(getForeignIdString(foreignId, sourceId), accId);
		} catch (ForeignIdPersistenceServiceException e) {
			throw new ForeignIdServiceException("persistenceService.link failed", e);
		}
	}

	@Override
	public void removeForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException {
		try {
			persistenceService.unlink(accId, sourceId, foreignId);
			List<ForeignId> list = cacheByAccountId.get(accId);
			list.remove(new ForeignId(accId, sourceId, foreignId));
			cacheByForeignId.remove(getForeignIdString(foreignId, sourceId));
		} catch (ForeignIdPersistenceServiceException e) {
			throw new ForeignIdServiceException("persistenceService.unlink failed", e);
		}
	}

	@Override
	public AccountId getAccountIdByForeignId(String foreignId, int sourceId) throws ForeignIdServiceException {
		if (foreignId == null) {
			throw new IllegalArgumentException("Null parameter foreignId to getForeignIds(foreignId, sourceId)");
		}
		AccountId accId = cacheByForeignId.get(getForeignIdString(foreignId, sourceId));
		if (accId != null) {
			return accId;
		}
		try {
			AccountId fromPersistence = persistenceService.getAccountIdByForeignId(sourceId, foreignId);
			if (fromPersistence != null) {
				cacheByForeignId.put(getForeignIdString(foreignId, sourceId), fromPersistence);
			}
			return fromPersistence;
		} catch (ForeignIdPersistenceServiceException e) {
			throw new ForeignIdServiceException("persistenceService.getAccountIdByForeignId failed", e);
		}
	}

	@Override
	public List<ForeignId> getForeignIds(AccountId accId) throws ForeignIdServiceException {
		if (accId == null) {
			throw new IllegalArgumentException("Null parameter accId to getForeignIds(accId)");
		}
		List<ForeignId> fromCache = cacheByAccountId.get(accId);
		if (fromCache != null) {
			return fromCache;
		}
		try {
			List<ForeignId> fromPersistence = persistenceService.getForeignIdsByAccountId(accId);
			if (fromPersistence == null) {
				fromPersistence = new ArrayList<ForeignId>();
			}
			cacheByAccountId.put(accId, fromPersistence);
			return fromPersistence;
		} catch (ForeignIdPersistenceServiceException e) {
			throw new ForeignIdServiceException("persistenceService.getForeignIdsByAccountId failed", e);
		}
	}

}
