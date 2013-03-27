package net.anotheria.portalkit.services.foreignid;

import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import org.distributeme.annotation.DistributeMe;

/**
 * ForeignId service interface.
 * 
 * @author lrosenberg
 * @since 28.12.12 23:44
 */
@DistributeMe()
public interface ForeignIdService extends Service {
	/**
	 * Creates a new association between our account and another account.
	 * 
	 * @param accId
	 * @param foreignId
	 * @param sourceId
	 * @throws ForeignIdServiceException
	 */
	void addForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Removes previously created association with another account.
	 * 
	 * @param accId
	 * @param foreignId
	 * @param sourceId
	 * @throws ForeignIdServiceException
	 */
	void removeForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Returns the associated account id for this source/foreign Id combination.
	 * 
	 * @param foreignId
	 * @param sourceId
	 * @return {@link AccountId}
	 * @throws ForeignIdServiceException
	 */
	AccountId getAccountIdByForeignId(String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Returns all associations for given account id.
	 * 
	 * @param accountId
	 * @return {@link List<AccountId>}
	 * @throws ForeignIdServiceException
	 */
	List<ForeignId> getForeignIds(AccountId accountId) throws ForeignIdServiceException;
}
