package net.anotheria.portalkit.services.foreignid.persistence;

import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.ForeignId;

/**
 * ForeignId persistence service.
 * 
 * @author dagafonov
 * 
 */
public interface ForeignIdPersistenceService extends Service {

	/**
	 * Returns account ID by foreign ID. If foreign id does not exist - method returns null.
	 * @param foreignId
	 * @return
	 * @throws ForeignIdPersistenceServiceException
	 */
	public AccountId getAccountIdByForeignId(int sid, String fid) throws ForeignIdPersistenceServiceException;
	
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
	public void link(AccountId accountId, int sid, String fid) throws ForeignIdPersistenceServiceException;

	/**
	 * Dissociate all foreign id.
	 * @param foreignId
	 * @throws ForeignIdPersistenceServiceException
	 */
	public void unlink(int sid, String fid) throws ForeignIdPersistenceServiceException;
	
	/**
	 * Dissociate foreign id for specified account.
	 * @param accountId
	 * @param sid
	 * @param fid
	 * @throws ForeignIdPersistenceServiceException
	 */
	public void unlink(AccountId accountId, int sid, String fid) throws ForeignIdPersistenceServiceException;

}