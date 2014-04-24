package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * @author VKoulakov
 * @since 22.04.14 17:28
 */
public class ArchivedAccountNotFoundException extends AccountArchiveServiceException {
    private static final long serialVersionUID = -3402544016147201908L;

    public ArchivedAccountNotFoundException(AccountId accountId){
        super("Archived account having id=" + accountId + " was not found");
    }

    public ArchivedAccountNotFoundException(String accountName){
        super("Archived account with name=" + accountName + " was not found");
    }
}
