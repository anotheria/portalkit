package net.anotheria.portalkit.adminapi.api;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.adminapi.resources.account.AccountUpdateRequest;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.common.AccountId;

public interface AdminAPI extends API {

    PageResult<Account> getAccounts(int pageNumber, int itemsOnPage) throws APIException;

    Account getAccountById(AccountId accountId) throws APIException;

    Account updateAccount(AccountUpdateRequest updateRequest) throws APIException;

}
