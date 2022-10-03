package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.accountarchive.event.AccountArchiveServiceEventSupplier;
import net.anotheria.portalkit.services.accountarchive.persistence.AccountArchivePersistenceService;
import net.anotheria.portalkit.services.accountarchive.persistence.ArchivedAccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author VKoulakov
 * @since 21.04.14 19:03
 */
@Monitor(subsystem = "portalkit")
public class AccountArchiveServiceImpl implements AccountArchiveService {

    private AccountArchivePersistenceService persistenceService;
    private AccountArchiveServiceEventSupplier eventSupplier = new AccountArchiveServiceEventSupplier();

    public AccountArchiveServiceImpl() {
        try {
            persistenceService = MetaFactory.get(AccountArchivePersistenceService.class);
        } catch (MetaFactoryException e) {
            throw new IllegalStateException("Can't start without persistence service ", e);
        }
    }

    @Override
    public ArchivedAccount getAccount(AccountId accountId) throws AccountArchiveServiceException {
        try {
            ArchivedAccount account = persistenceService.getAccount(accountId);
            if (account == null) {
                throw new ArchivedAccountNotFoundException(accountId.getInternalId());
            }
            return account;
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public List<ArchivedAccount> getAccounts(List<AccountId> accountIds) throws AccountArchiveServiceException {
        try {
            return persistenceService.getAccounts(accountIds);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public void deleteAccount(AccountId accountId) throws AccountArchiveServiceException {
        try {
            ArchivedAccount account = persistenceService.getAccount(accountId);
            persistenceService.deleteAccount(accountId);
            eventSupplier.accountDeleted(account);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public void deleteAccountsByEmail(String pattern) throws AccountArchiveServiceException {
        List<ArchivedAccount> accounts = getAllAccounts();
        Pattern p = Pattern.compile(pattern);
        Matcher matcher;
        for(ArchivedAccount account : accounts){
            matcher = p.matcher(account.getEmail());
            if(matcher.matches()){
                deleteAccount(account.getId());
            }
        }
    }

    @Override
    public void updateAccount(ArchivedAccount toUpdate) throws AccountArchiveServiceException {
        try {
            ArchivedAccount beforeUpdate = persistenceService.getAccount(toUpdate.getId());
            persistenceService.saveAccount(toUpdate);
            eventSupplier.accountUpdated(beforeUpdate, toUpdate);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public ArchivedAccount createAccount(ArchivedAccount toUpdate) throws AccountArchiveServiceException {
        try {
            toUpdate.setDeletionTimestamp(System.currentTimeMillis());
            persistenceService.saveAccount(toUpdate);
            eventSupplier.accountCreated(toUpdate);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
        return getAccount(toUpdate.getId());
    }

    @Override
    public String getCustomNote(AccountId accountId) throws AccountArchiveServiceException {
        try {
            return persistenceService.getCustomNote(accountId);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public void saveCustomNote(AccountId accountId, String customNote) throws AccountArchiveServiceException {
        try {
            persistenceService.saveCustomNote(accountId, customNote);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public ArchivedAccount getAccountByName(String name) throws AccountArchiveServiceException {
        try {
            AccountId id = persistenceService.getIdByName(name);
            if (id == null) {
                throw new ArchivedAccountNotFoundException(name);
            }
            return getAccount(id);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public ArchivedAccount getAccountByEmail(String email) throws AccountArchiveServiceException {
        try {
            AccountId id = persistenceService.getIdByEmail(email);
            if (id == null) {
                throw new ArchivedAccountNotFoundException(email);
            }
            return getAccount(id);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public List<ArchivedAccount> getAllAccounts() throws AccountArchiveServiceException {
        try {
            return persistenceService.getAllAccounts();
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }

    @Override
    public List<ArchivedAccount> getAccountsByQuery(ArchivedAccountQuery query) throws AccountArchiveServiceException {
        try {
            return persistenceService.getAccountsByQuery(query);
        } catch (ArchivedAccountPersistenceServiceException e) {
            throw new AccountArchiveServiceException(e);
        }
    }
}
