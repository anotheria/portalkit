package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceService;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

/**
 * Account settings service implementation.
 * 
 * @author lrosenberg
 * @author dagafonov
 * 
 * @since 12.12.12 10:14
 */
public class AccountSettingsServiceImpl implements AccountSettingsService {

	/**
	 * {@link AccountSettingsPersistenceService} instance.
	 */
	private AccountSettingsPersistenceService persistence;
	
	/**
	 * Lock manager.
	 */
	private IdBasedLockManager<AccountSettingsKey> accountsLockManager = new SafeIdBasedLockManager<AccountSettingsKey>();
	
	/**
	 * 
	 */
	public AccountSettingsServiceImpl() {
		try {
			persistence = MetaFactory.get(AccountSettingsPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public Dataspace getDataspace(AccountId accountId, DataspaceType domain) throws AccountSettingsServiceException {
		AccountSettingsKey key = new AccountSettingsKey();
		IdBasedLock<AccountSettingsKey> lock = accountsLockManager.obtainLock(key);
		lock.lock();
		try {
			Dataspace ds = persistence.loadDataspace(accountId, domain.getId());
			if (ds == null) {
				throw new AccountSettingsServiceException("dataspace does not exist");
			}
			return ds;
		} catch (AccountSettingsPersistenceServiceException e) {
			throw new AccountSettingsServiceException("persistence.loadDataspace failed", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void saveDataspace(Dataspace dataspace) throws AccountSettingsServiceException {
		IdBasedLock<AccountSettingsKey> lock = accountsLockManager.obtainLock(dataspace.getKey());
		lock.lock();
		try {
			persistence.saveDataspace(dataspace);
		} catch (AccountSettingsPersistenceServiceException e) {
			throw new AccountSettingsServiceException("persistence.saveDataspace failed", e);
		} finally {
			lock.unlock();
		}
	}

}
