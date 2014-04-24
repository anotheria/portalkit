package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.TimeUnit;

/**
 * @author VKoulakov
 * @since 24.04.14 18:25
 */
public class AccountArchiveUtils {
    private static final int DAYS_AGO = 7;

    public static ArchivedAccount createAccountTemplate() {
        ArchivedAccount account = new ArchivedAccount(AccountId.generateNew());
        account.setName("test");
        account.setEmail("test@example.com");
        account.setType(1);
        account.setRegistrationTimestamp(System.currentTimeMillis());
        account.addStatus(1);
        account.addStatus(16);
        account.addStatus(32);
        long now = System.currentTimeMillis();
        account.setDeletionTimestamp(now - TimeUnit.DAY.getMillis(DAYS_AGO));
        account.setDeletionNote("test deletion");
        return account;
    }

}
