package net.anotheria.portalkit.services.foreignid;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.moskito.core.entity.EntityManagingService;
import net.anotheria.moskito.core.entity.EntityManagingServices;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdEntity;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdEntityId;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ForeignId Service implementation.
 * 
 * @author dagafonov
 */
@Monitor(subsystem = "portalkit")
public class ForeignIdServiceImpl implements ForeignIdService, EntityManagingService {
    /**
     * {@link Logger} instance.
     */
	private static final Logger log = LoggerFactory.getLogger(ForeignIdServiceImpl.class);
    /**
     * {@link ForeignIdEntityRepository} instance.
     */
    private final ForeignIdEntityRepository repository;
	/**
	 * Cache by {@link AccountId}.
	 */
	private final Cache<AccountId, List<ForeignId>> cacheByAccountId;

	/**
	 * Cache by string representation of all foreign id fields.
	 */
	private final Cache<ForeignIdEntityId, AccountId> cacheByForeignId;

    /**
     * Persistence service instance.
     *
     * @param repository {@link ForeignIdEntityRepository} repo
     */
	public ForeignIdServiceImpl(ForeignIdEntityRepository repository) {
        this.repository = repository;
		cacheByAccountId = Caches.createHardwiredCache("foreignidservice-cacheaccountid");
		cacheByForeignId = Caches.createHardwiredCache("foreignidservice-cacheforeignid");
		EntityManagingServices.createEntityCounter(this, "ForeignIds");
	}

	@Override
	public int getEntityCount(String s) {
		try {
            return (int) repository.count();
        } catch (Exception e) {
            log.error("Unable to count foreign ids", e);
            return -1;
        }
	}

	@Override
	public void addForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException {
        try {
            ForeignIdEntityId id = new ForeignIdEntityId(foreignId, sourceId);
            ForeignIdEntity entity = repository.findById(id).orElse(null);
            if (entity != null)
                throw new ForeignIdAlreadyExistsServiceException("ForeignId already exists: " + foreignId + " for sourceId: " + sourceId);

            entity = new ForeignIdEntity();
            entity.setId(id);
            entity.setAccountId(accId.getInternalId());
            repository.save(entity);

            List<ForeignId> list = cacheByAccountId.get(accId);
			if (list == null) {
				list = new ArrayList<>();
				cacheByAccountId.put(accId, list);
			}
			list.add(new ForeignId(accId, sourceId, foreignId));
            cacheByForeignId.put(id, accId);
		} catch (Exception e) {
			throw new ForeignIdServiceException("persistenceService.link failed", e);
		}
	}

	@Override
	public void removeForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException {
		try {
			ForeignIdEntityId id = new ForeignIdEntityId(foreignId, sourceId);
            repository.deleteById(id);
			List<ForeignId> list = cacheByAccountId.get(accId);

			if (list != null)
				list.remove(new ForeignId(accId, sourceId, foreignId));

			cacheByForeignId.remove(id);
		} catch (Exception e) {
			throw new ForeignIdServiceException("persistenceService.unlink failed", e);
		}
	}

	@Override
	public AccountId getAccountIdByForeignId(String foreignId, int sourceId) throws ForeignIdServiceException {
		if (foreignId == null)
			throw new IllegalArgumentException("Null parameter foreignId to getForeignIds(foreignId, sourceId)");

        ForeignIdEntityId id = new ForeignIdEntityId(foreignId, sourceId);
		AccountId accId = cacheByForeignId.get(id);
		if (accId != null)
			return accId;

		try {
			AccountId fromPersistence = repository.findById(id)
                    .map(entity -> new AccountId(entity.getAccountId()))
                    .orElse(null);

			if (fromPersistence != null)
				cacheByForeignId.put(id, fromPersistence);

			return fromPersistence;
		} catch (Exception e) {
			throw new ForeignIdServiceException("persistenceService.getAccountIdByForeignId failed", e);
		}
	}

	@Override
	public List<ForeignId> getForeignIds(AccountId accId) throws ForeignIdServiceException {
		if (accId == null)
			throw new IllegalArgumentException("Null parameter accId to getForeignIds(accId)");

		List<ForeignId> fromCache = cacheByAccountId.get(accId);
		if (fromCache != null)
			return fromCache;

		try {
            List<ForeignId> foreignIds = new ArrayList<>();
			List<ForeignIdEntity> fromPersistence = repository.findByAccountId(accId.getInternalId());
            if (fromPersistence != null)
                for (ForeignIdEntity entity : fromPersistence)
                    foreignIds.add(new ForeignId(accId, entity.getId().getSourceId(), entity.getId().getForeignId()));

			cacheByAccountId.put(accId, foreignIds);
			return foreignIds;
		} catch (Exception e) {
			throw new ForeignIdServiceException("persistenceService.getForeignIdsByAccountId failed", e);
		}
	}

	@Override
	public void deleteUserData(AccountId accountId) {
		try {
			repository.removeByAccountId(accountId.getInternalId());
			cacheByAccountId.remove(accountId);
		} catch (Exception e) {
			log.error("Unable to delete foreign ids", e);
		}
	}

	@Override
	public String describeData() {
		return "foreignIdService";
	}
}
