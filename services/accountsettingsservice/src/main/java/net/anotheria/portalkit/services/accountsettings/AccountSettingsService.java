package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import net.anotheria.portalkit.services.common.DeletionService;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

/**
 * 
 * 
 * @author lrosenberg
 * @since 11.12.12 13:16
 */
@DistributeMe
@FailBy(strategyClass=RetryCallOnce.class)
public interface AccountSettingsService extends Service, DeletionService {

	/**
	 * Loads dataspace from persistence by given userId and dataspaceId.
	 * 
	 * @param accountId account id.
	 * @return {@link Dataspace}
	 * @throws AccountSettingsServiceException
	 */
	Dataspace getDataspace(AccountId accountId, DataspaceType domain) throws AccountSettingsServiceException;

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
	 * Deletes all dataspaces of user. Returns amount of dataspaces deleted.
	 * @param accountId account id.
	 * @return	int
	 * @throws AccountSettingsServiceException
	 */
	int deleteDataspaces(AccountId accountId) throws AccountSettingsServiceException;
}
