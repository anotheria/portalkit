package net.anotheria.portalkit.services.accountarchive.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.accountarchive.ArchivedAccount;
import net.anotheria.portalkit.services.accountarchive.ArchivedAccountQuery;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.Collection;
import java.util.List;

/**
 * @author VKoulakov
 * @since 21.04.14 19:04
 */
public interface AccountArchivePersistenceService extends Service {
    /**
     * Returns the account with that account id.
     *
     * @param id account identity
     * @return {@link ArchivedAccount}
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    ArchivedAccount getAccount(AccountId id) throws ArchivedAccountPersistenceServiceException;

    /**
     * Returns the list of {@link AccountId} with specified identities.
     * @param identities list of identities to find.
     * @return list of {@link AccountId}
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    List<ArchivedAccount> getAccounts(List<AccountId> identities) throws ArchivedAccountPersistenceServiceException;

    /**
     * @return list of {@link AccountId}
     * */
    List<ArchivedAccount> getAllAccounts() throws  ArchivedAccountPersistenceServiceException;
    /**
     * Saves the account.
     *
     * @param account {@link ArchivedAccount} to be saved
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    void saveAccount(ArchivedAccount account) throws ArchivedAccountPersistenceServiceException;

    /**
     * Deletes the account with submitted id.
     *
     * @param id account identity
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    void deleteAccount(AccountId id) throws ArchivedAccountPersistenceServiceException;

    /**
     * Returns the id of the account with the given name.
     *
     * @param name account name to find
     * @return {@link AccountId}
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    AccountId getIdByName(String name) throws ArchivedAccountPersistenceServiceException;
    /**
     * Returns the custom note of the account with the given id.
     *
     * @param id account id to find
     * @return custom note
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    String getCustomNoteById(AccountId id) throws ArchivedAccountPersistenceServiceException;

    /**
     * Save custom note.
     * @param id account id
     * @param customNote custom note
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    void saveCustomNote(AccountId id, String customNote) throws ArchivedAccountPersistenceServiceException;
    /**
     * Returns the id of the account with the given email.
     *
     * @param email account email to find
     * @return AccountId
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    AccountId getIdByEmail(String email) throws ArchivedAccountPersistenceServiceException;

    /**
     * Get all account id's.
     *
     * @return {@link java.util.Collection} of {@link AccountId}
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    Collection<AccountId> getAllAccountIds() throws ArchivedAccountPersistenceServiceException;

    /**
     *
     * @param id    account id.
     * @return list of {@link AccountId}
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    List<AccountId> getAccountsByType(int id) throws ArchivedAccountPersistenceServiceException;

    /**
     * Get accounts by query.
     *
     * @param query
     *            {@link ArchivedAccountQuery}
     * @return {@link List} of {@link ArchivedAccount}
     * @throws ArchivedAccountPersistenceServiceException if some error occurred
     */
    List<ArchivedAccount> getAccountsByQuery(ArchivedAccountQuery query) throws ArchivedAccountPersistenceServiceException;

}
