package net.anotheria.portalkit.services.foreignid.persistence;

import java.util.List;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.ForeignId;

/**
 * ForeignId persistence service.
 * 
 * @author dagafonov
 * 
 */
public interface ForeignIdPersistenceService {

	/**
	 * Returns account ID by foreign ID.
	 * @param foreignId
	 * @return
	 * @throws ForeignIdPersistenceServiceException
	 */
	public AccountId getAccountIdByForeignId(ForeignId foreignId) throws ForeignIdPersistenceServiceException;

	/**
	 * Returns list of foreign ids by account ID. All accounts mapped with foreign ids as one to many.
	 * @param accountId
	 * @return
	 * @throws ForeignIdPersistenceServiceException
	 */
	public List<ForeignId> getForeignIdsByAccountId(AccountId accountId) throws ForeignIdPersistenceServiceException;

	/**
	 * Associate foreign ID with account ID.
	 * @param foreignId
	 * @param accountId
	 * @throws ForeignIdPersistenceServiceException
	 */
	public void link(ForeignId foreignId, AccountId accountId) throws ForeignIdPersistenceServiceException;

	/**
	 * Deassociate foreign ID from account.
	 * @param foreignId
	 * @throws ForeignIdPersistenceServiceException
	 */
	public void unlink(ForeignId foreignId) throws ForeignIdPersistenceServiceException;

	/**
	 * Create foreign ID.
	 * @param sourceId
	 * @param id
	 * @throws ForeignIdPersistenceServiceException
	 */
	public void create(int sourceId, String id) throws ForeignIdPersistenceServiceException;

	/**
	 * Delete foreign ID.
	 * @param foreignId
	 * @throws ForeignIdPersistenceServiceException
	 */
	public void delete(ForeignId foreignId) throws ForeignIdPersistenceServiceException;

}
