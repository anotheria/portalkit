package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;

import java.util.List;

/**
 * Interface to account archive service which stores deleted accounts.
 * @author VKoulakov
 * @since 21.04.14 18:40
 */
@DistributeMe
public interface AccountArchiveService extends Service {
    /**
     * Returns an account by it's id.
     * @param accountId id of account to find
     * @return archived account
     * @throws AccountArchiveServiceException
     */
    ArchivedAccount getAccount(AccountId accountId) throws AccountArchiveServiceException;

    /**
     * Returns a list of archived accounts for given list of identities.
     *
     * @param accountId
     * @return
     * @throws AccountArchiveServiceException
     */
    List<ArchivedAccount> getAccounts(List<AccountId> accountId) throws AccountArchiveServiceException;

    /**
     * Deletes archived account from database.
     * @param accountId
     * @throws AccountArchiveServiceException
     */
    void deleteAccount(AccountId accountId) throws AccountArchiveServiceException;

    /**
     * Updates acrchived account.
     * @param toUpdate
     * @throws AccountArchiveServiceException
     */
    void updateAccount(ArchivedAccount toUpdate) throws AccountArchiveServiceException;

    /**
     * Creates a new archived account.
     * @param toUpdate
     * @throws AccountArchiveServiceException
     */
    ArchivedAccount createAccount(ArchivedAccount toUpdate) throws AccountArchiveServiceException;

    /**
     *
     * @param name
     * @return
     * @throws AccountArchiveServiceException
     */
    ArchivedAccount getAccountByName(String name) throws AccountArchiveServiceException;

    /**
     *
     * @param email
     * @return
     * @throws AccountArchiveServiceException
     */
    ArchivedAccount getAccountByEmail(String email) throws AccountArchiveServiceException;

    /**
     *
     * @return
     * @throws AccountArchiveServiceException
     */
    List<ArchivedAccount> getAllAccounts() throws AccountArchiveServiceException;

    /**
     *
     * @param query
     * @return
     * @throws AccountArchiveServiceException
     */
    List<ArchivedAccount> getAccountsByQuery(ArchivedAccountQuery query) throws AccountArchiveServiceException;
}
