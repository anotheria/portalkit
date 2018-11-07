package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.List;

/**
 * Interface to account archive service which stores deleted accounts.
 * @author VKoulakov
 * @since 21.04.14 18:40
 */
@DistributeMe
@FailBy(strategyClass=RetryCallOnce.class)
public interface AccountArchiveService extends Service {
    /**
     * Returns an account by it's id.
     * @param accountId account id. id of account to find
     * @return archived account
     * @throws AccountArchiveServiceException if error.
     */
    ArchivedAccount getAccount(AccountId accountId) throws AccountArchiveServiceException;

    /**
     * Returns a list of archived accounts for given list of identities.
     *
     * @param accountId account id.
     * @return list of {@link ArchivedAccount}
     * @throws AccountArchiveServiceException if error.
     */
    List<ArchivedAccount> getAccounts(List<AccountId> accountId) throws AccountArchiveServiceException;

    /**
     * Deletes archived account from database.
     * @param accountId account id.
     * @throws AccountArchiveServiceException if error.
     */
    void deleteAccount(AccountId accountId) throws AccountArchiveServiceException;

    /**
     * Deletes archived accounts from database whose emails match pattern.
     * @param pattern email pattern string
     * @throws AccountArchiveServiceException
     */
    void deleteAccountsByEmail(String pattern) throws AccountArchiveServiceException;

    /**
     * Updates acrchived account.
     * @param toUpdate  archive to update.
     * @throws AccountArchiveServiceException if error.
     */
    void updateAccount(ArchivedAccount toUpdate) throws AccountArchiveServiceException;

    /**
     * Creates a new archived account.
     * @param toUpdate  archive to update.
     * @return {@link ArchivedAccount}
     * @throws AccountArchiveServiceException if error.
     */
    ArchivedAccount createAccount(ArchivedAccount toUpdate) throws AccountArchiveServiceException;

    /**
     *
     * @param name  account name.
     * @return  {@link ArchivedAccount}
     * @throws AccountArchiveServiceException if error.
     */
    ArchivedAccount getAccountByName(String name) throws AccountArchiveServiceException;

    /**
     *
     * @param email account email.
     * @return  {@link ArchivedAccount}
     * @throws AccountArchiveServiceException if error.
     */
    ArchivedAccount getAccountByEmail(String email) throws AccountArchiveServiceException;

    /**
     *
     * @return list of {@link ArchivedAccount}
     * @throws AccountArchiveServiceException if error.
     */
    List<ArchivedAccount> getAllAccounts() throws AccountArchiveServiceException;

    /**
     *
     * @param query {@link ArchivedAccountQuery}
     * @return list of {@link ArchivedAccount}
     * @throws AccountArchiveServiceException if error.
     */
    List<ArchivedAccount> getAccountsByQuery(ArchivedAccountQuery query) throws AccountArchiveServiceException;
}
