package net.anotheria.portalkit.services.foreignid;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;

/**
 * ForeignId service interface.
 * 
 * @author lrosenberg
 * @since 28.12.12 23:44
 */
@DistributeMe
@FailBy(strategyClass=RetryCallOnce.class)
public interface ForeignIdService extends Service {
	/**
	 * Creates a new association between our account and another account.
	 * 
	 * @param accId account id.
	 * @param foreignId foreign id.
	 * @param sourceId source id.
	 * @throws ForeignIdServiceException if error.
	 */
	void addForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Removes previously created association with another account.
	 * 
	 * @param accId account id.
	 * @param foreignId foreign id.
	 * @param sourceId source id.
	 * @throws ForeignIdServiceException if error.
	 */
	void removeForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Returns the associated account id for this source/foreign Id combination.
	 * 
	 * @param foreignId foreign id.
	 * @param sourceId source id.
	 * @return {@link AccountId}
	 * @throws ForeignIdServiceException if error.
	 */
	AccountId getAccountIdByForeignId(String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Returns all associations for given account id.
	 * 
	 * @param accountId account id.
	 * @return list of {@link AccountId}
	 * @throws ForeignIdServiceException if error.
	 */
	List<ForeignId> getForeignIds(AccountId accountId) throws ForeignIdServiceException;
}
