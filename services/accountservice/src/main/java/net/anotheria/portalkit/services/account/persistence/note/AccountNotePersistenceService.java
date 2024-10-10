package net.anotheria.portalkit.services.account.persistence.note;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.account.AccountNote;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

public interface AccountNotePersistenceService extends Service {
    void saveAccountNote(AccountNote accountNote) throws AccountNotePersistenceServiceException;

    List<AccountNote> getNotesByAccountId(AccountId accountId) throws AccountNotePersistenceServiceException;

    AccountNote getAccountNoteById(long id) throws AccountNotePersistenceServiceException;
    AccountNote updateAccountNote(AccountNote accountNote) throws AccountNotePersistenceServiceException;
    void deleteAccountNote(long id) throws AccountNotePersistenceServiceException;
}
