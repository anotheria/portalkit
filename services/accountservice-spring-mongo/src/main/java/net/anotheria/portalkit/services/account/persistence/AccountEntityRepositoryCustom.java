package net.anotheria.portalkit.services.account.persistence;

import net.anotheria.portalkit.services.account.AccountQuery;

import java.util.List;

public interface AccountEntityRepositoryCustom {
    //TODO at some point add paging.
    List<AccountEntity> search(AccountQuery query);

    long countMatching(AccountQuery query);
}
