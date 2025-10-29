package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.anoprise.metafactory.Service;

import net.anotheria.portalkit.services.common.integrity.UserDataManagingServiceWithIntegrityCheck;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;

/**
 * 
 * 
 * @author lrosenberg
 * @since 11.12.12 13:16
 */
@DistributeMe
@FailBy(strategyClass=RetryCallOnce.class)
public interface AccountSettingsService extends Service, UserDataManagingServiceWithIntegrityCheck {

	/**
	 * Loads dataspace from persistence by given userId and dataspaceId.
	 * 
	 * @param accountId account id.
	 * @return {@link Dataspace}
	 * @throws AccountSettingsServiceException
	 */
	Dataspace getDataspace(AccountId accountId, DataspaceType domain) throws AccountSettingsServiceException;

	/**
	 * Loads all dataspaces from persistence by given userId
	 * @param accountId account id
	 * @return {@link Dataspace}
	 */
	Collection<Dataspace> getAllDataspaces(AccountId accountId) throws AccountSettingsServiceException;

	/**
	 * Saves given dataspace in persistence.
	 * 
	 * @param dataspace
	 * @throws AccountSettingsServiceException
	 */
	void saveDataspace(Dataspace dataspace) throws AccountSettingsServiceException;

	/**
	 * Deletes a dataspace of user.
	 * @param accountId account id.
	 * @param dataspaceType dataspace type.
	 * @return	boolean
	 * @throws AccountSettingsServiceException
	 */
	boolean deleteDataspace(AccountId accountId, DataspaceType dataspaceType) throws AccountSettingsServiceException;

	/**
	 * Deletes a dataspace of user by dataspaceId.
	 * @param accountId account id
	 * @param dataspaceId dataspace id
	 * @return boolean
	 * @throws AccountSettingsServiceException
	 */
	boolean deleteDataspace(AccountId accountId, int dataspaceId) throws AccountSettingsServiceException;
}
