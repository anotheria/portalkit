package net.anotheria.portalkit.services.foreignid;

import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;

import org.distributeme.annotation.DistributeMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.12.12 23:44
 */
@DistributeMe()
public interface ForeignIdService extends Service {
	/**
	 * Creates a new association between our account and another account.
	 * @param accId
	 * @param foreignId
	 * @param sourceId
	 * @throws ForeignIdServiceException
	 */
	public void addForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Removes previously created association with another account.
	 * @param accId
	 * @param foreignId
	 * @param sourceId
	 * @throws ForeignIdServiceException
	 */
	public void removeForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Returns the associated account id for this source/foreign Id combination.
	 * @param foreignId
	 * @param sourceId
	 * @return
	 * @throws ForeignIdServiceException
	 */
	public AccountId getAccountIdByForeignId(String foreignId, int sourceId) throws ForeignIdServiceException;

	/**
	 * Returns all associations for given account id.
	 * @param accountId
	 * @return
	 * @throws ForeignIdServiceException
	 */
	public List<ForeignId> getForeignIds(AccountId accountId) throws ForeignIdServiceException;
}
