package net.anotheria.portalkit.adminapi.api.admin;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.adminapi.api.shared.PageResult;
import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountUpdateRequest;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.accountsettings.attribute.AttributeType;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

public interface AdminAPI extends API {

    List<AdminAPIConfig.AccountStatusConfig> getAccountStatuses() throws APIException;

    List<AdminAPIConfig.AccountTypeConfig> getAccountTypes() throws APIException;

    PageResult<AdminAccountAO> getAccounts(int pageNumber, int itemsOnPage, String searchTerm) throws APIException;

    AdminAccountAO getAccountById(AccountId accountId) throws APIException;

    AdminAccountAO updateAccount(AccountUpdateRequest updateRequest) throws APIException;

    AdminAccountAO addAccountStatus(AccountId accountId, int status) throws APIException;

    AdminAccountAO removeAccountStatus(AccountId accountId, int status) throws APIException;

    void setNewAccountPassword(AccountId accountId, String newPassword) throws APIException;

    String getSignAsToken(AccountId accountId) throws APIException;

    List<Dataspace> getAllDataspaces(AccountId accountId);

    Dataspace addDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName, String attributeValue, AttributeType type) throws APIException;

    Dataspace removeDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName) throws APIException;

}
