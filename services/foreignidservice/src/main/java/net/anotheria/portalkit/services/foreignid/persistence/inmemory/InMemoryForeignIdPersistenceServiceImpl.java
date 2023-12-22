package net.anotheria.portalkit.services.foreignid.persistence.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceService;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceServiceException;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

/**
 * In Memory ForeignId Persistence Service implementation.
 * 
 * @author dagafonov
 * 
 */
public class InMemoryForeignIdPersistenceServiceImpl implements ForeignIdPersistenceService {

	/**
	 * Storage instance.
	 */
	private ConcurrentHashMap<AccountId, List<ForeignId>> storage = new ConcurrentHashMap<AccountId, List<ForeignId>>();
	
	/**
	 * Cache of foreign id's.
	 */
	private ConcurrentHashMap<String, ForeignId> foreignIdCache = new ConcurrentHashMap<String, ForeignId>();

	/**
	 * Lock manager.
	 */
	private final IdBasedLockManager<AccountId> lockManager = new SafeIdBasedLockManager<AccountId>();

	private String getKey(int sid, String fid) {
		return "key_" + sid + "_" + fid;
	}

	@Override
	public AccountId getAccountIdByForeignId(int sid, String fid) throws ForeignIdPersistenceServiceException {
		ForeignId foreignId = foreignIdCache.get(getKey(sid, fid));
		if (foreignId == null) {
			return null;
		}
		return foreignId.getAccountId();
	}

	@Override
	public List<ForeignId> getForeignIdsByAccountId(AccountId accountId) throws ForeignIdPersistenceServiceException {
		if (accountId == null) {
			throw new ForeignIdPersistenceServiceException("accountId is null");
		}
		final IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
		try {
			lock.lock();
			return storage.get(accountId);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void link(AccountId accId, int sid, String fid) throws ForeignIdPersistenceServiceException {
		if (accId == null) {
			throw new ForeignIdPersistenceServiceException("accountId is null");
		}
		if (fid == null) {
			throw new ForeignIdPersistenceServiceException("foreignId is null");
		}

		final IdBasedLock<AccountId> lock = lockManager.obtainLock(accId);
		try {
			lock.lock();
			List<ForeignId> fids = storage.get(accId);
			ForeignId foreignId = new ForeignId(accId, sid, fid);
			if (fids == null) {
				fids = new ArrayList<ForeignId>();
				fids.add(foreignId);
				storage.put(accId, fids);
			} else {
				fids.add(foreignId);
			}
			foreignIdCache.putIfAbsent(getKey(sid, fid), foreignId);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void unlink(int sid, String fid) throws ForeignIdPersistenceServiceException {
		AccountId accId = getAccountIdByForeignId(sid, fid);
		unlink(accId, sid, fid);
	}

	@Override
	public void unlink(AccountId accId, int sid, String fid) throws ForeignIdPersistenceServiceException {
		if (accId == null) {
			throw new ForeignIdPersistenceServiceException("accountId is null");
		}
		if (fid == null) {
			throw new ForeignIdPersistenceServiceException("foreignId is null");
		}
		final IdBasedLock<AccountId> lock = lockManager.obtainLock(accId);
		try {
			lock.lock();
			List<ForeignId> fids = storage.get(accId);
			if (fids != null) {
				ForeignId currFid = new ForeignId(accId, sid, fid);
				fids.remove(currFid);
				foreignIdCache.remove(getKey(sid, fid));
			} else {
				storage.remove(accId);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public long getForeignIdsCount() throws ForeignIdPersistenceServiceException {
		return storage.size();
	}
}
