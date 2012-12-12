package net.anotheria.portalkit.services.accountsettings.persistence;

import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 09:55
 */
public interface AccountSettingsPersistenceService {
	void saveDataspace(Dataspace dataspace) throws AccountSettingsPersistenceServiceException;

	Collection<Dataspace> loadDataspaces(AccountId owner) throws AccountSettingsPersistenceServiceException;

	Dataspace loadDataspace(AccountId owner, int dataspaceId) throws AccountSettingsPersistenceServiceException;

	Collection<Dataspace> loadDataspaces(AccountId owner, Collection<Integer> dataspaceIds) throws AccountSettingsPersistenceServiceException;

}
