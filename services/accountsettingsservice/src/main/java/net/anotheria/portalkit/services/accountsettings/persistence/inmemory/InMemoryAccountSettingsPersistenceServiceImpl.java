package net.anotheria.portalkit.services.accountsettings.persistence.inmemory;

import net.anotheria.portalkit.services.accountsettings.AccountSettingsKey;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceService;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 19.09.13 23:12
 */
public class InMemoryAccountSettingsPersistenceServiceImpl implements AccountSettingsPersistenceService {

	private ConcurrentMap<String, Dataspace> storage = new ConcurrentHashMap<String, Dataspace>();

	@Override
	public void saveDataspace(Dataspace dataspace) throws AccountSettingsPersistenceServiceException {
		storage.put(dataspace.getKey().toString(), dataspace);
	}

	@Override
	public Dataspace loadDataspace(AccountId owner, int dataspaceId) throws AccountSettingsPersistenceServiceException {
		return storage.get(new AccountSettingsKey(owner, dataspaceId).toString());
	}

	@Override
	public Collection<Dataspace> loadDataspaces(AccountId owner) throws AccountSettingsPersistenceServiceException {
		throw new IllegalStateException("Not implemented yet");
	}

	@Override
	public long dataspacesCount() throws AccountSettingsPersistenceServiceException {
		return storage.size();
	}

	@Override
	public boolean deleteDataspace(AccountId owner, int dataspaceId) throws AccountSettingsPersistenceServiceException {
		return storage.remove(new AccountSettingsKey(owner, dataspaceId).toString()) != null;

	}

	@Override
	public boolean deleteDataspaces(AccountId owner) throws AccountSettingsPersistenceServiceException {
		throw new IllegalStateException("Not implemented yet");
	}
}
