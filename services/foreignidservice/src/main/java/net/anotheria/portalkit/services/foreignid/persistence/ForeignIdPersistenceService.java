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
	 * Returns account ID by foreign ID. If foreign id does not exist - method
	 * returns null.
	 * 
	 * @param sid	source id.
	 * @param fid	foreign id.
	 * @return {@link AccountId}
	 * @throws ForeignIdPersistenceServiceException if error.
	 */
	AccountId getAccountIdByForeignId(int sid, String fid) throws ForeignIdPersistenceServiceException;

	/**
	 * Returns list of foreign ids by account ID. All accounts mapped with
	 * foreign ids as one to many.
	 * 
	 * @param accountId	account id.
	 * @return list of {@link ForeignId}
	 * @throws ForeignIdPersistenceServiceException if error.
	 */
	List<ForeignId> getForeignIdsByAccountId(AccountId accountId) throws ForeignIdPersistenceServiceException;

	/**
	 * Associate foreign ID with account ID.
	 *
	 * @param sid	source id.
	 * @param fid	foreign id.
	 * @param accountId	account id.
	 * @throws ForeignIdPersistenceServiceException if error.
	 */
	void link(AccountId accountId, int sid, String fid) throws ForeignIdPersistenceServiceException;

	/**
	 * Dissociate all foreign id.
	 *
	 * @param sid	source id.
	 * @param fid	foreign id.
	 * @throws ForeignIdPersistenceServiceException if error.
	 */
	void unlink(int sid, String fid) throws ForeignIdPersistenceServiceException;

	/**
	 * Dissociate foreign id for specified account.
	 * 
	 * @param accountId	account id.
	 * @param sid	source id.
	 * @param fid	foreign id.
	 * @throws ForeignIdPersistenceServiceException if error.
	 */
	void unlink(AccountId accountId, int sid, String fid) throws ForeignIdPersistenceServiceException;

}
