package net.anotheria.portalkit.adminapi.api;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountUpdateRequest;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.accountsettings.attribute.AttributeType;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

public interface AdminAPI extends API {

    PageResult<Account> getAccounts(int pageNumber, int itemsOnPage, String searchTerm) throws APIException;

    Account getAccountById(AccountId accountId) throws APIException;

    Account updateAccount(AccountUpdateRequest updateRequest) throws APIException;

    Account addAccountStatus(AccountId accountId, int status) throws APIException;

    Account removeAccountStatus(AccountId accountId, int status) throws APIException;

    void setNewAccountPassword(AccountId accountId, String newPassword) throws APIException;

    String getSignAsToken(AccountId accountId) throws APIException;

    List<Dataspace> getAllDataspaces(AccountId accountId);

    Dataspace addDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName, String attributeValue, AttributeType type) throws APIException;

    Dataspace removeDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName) throws APIException;

}
