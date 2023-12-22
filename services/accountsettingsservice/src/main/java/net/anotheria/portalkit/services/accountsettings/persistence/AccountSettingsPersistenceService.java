package net.anotheria.portalkit.services.accountsettings.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;

/**
 * AccountSettingsPersistenceService.
 * 
 * @author lrosenberg
 * @since 12.12.12 09:55
 */
public interface AccountSettingsPersistenceService extends Service {

	/**
	 * 
	 * @param dataspace
	 * @throws AccountSettingsPersistenceServiceException
	 */
	void saveDataspace(Dataspace dataspace) throws AccountSettingsPersistenceServiceException;

	/**
	 * 
	 * @param owner
	 * @param dataspaceId
	 * @return {@code Dataspace}
	 * @throws AccountSettingsPersistenceServiceException
	 */
	Dataspace loadDataspace(AccountId owner, int dataspaceId) throws AccountSettingsPersistenceServiceException;

	boolean deleteDataspace(AccountId owner, int dataspaceId) throws AccountSettingsPersistenceServiceException;

	boolean deleteDataspaces(AccountId owner) throws AccountSettingsPersistenceServiceException;

	/**
	 *
	 * @param owner
	 * @return {@code Collection<Dataspace>}
	 * @throws AccountSettingsPersistenceServiceException
	 */
	Collection<Dataspace> loadDataspaces(AccountId owner) throws AccountSettingsPersistenceServiceException;

	long dataspacesCount() throws AccountSettingsPersistenceServiceException;
//
//	/**
//	 * 
//	 * @param owner
//	 * @param dataspaceIds
//	 * @return {@code Collection<Dataspace>}
//	 * @throws AccountSettingsPersistenceServiceException
//	 */
//	Collection<Dataspace> loadDataspaces(AccountId owner, Collection<Integer> dataspaceIds) throws AccountSettingsPersistenceServiceException;

}
