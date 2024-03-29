package net.anotheria.portalkit.adminapi.api.admin;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.adminapi.api.admin.dataspace.DataspaceAO;
import net.anotheria.portalkit.adminapi.api.admin.dataspace.DataspaceAttributeAO;
import net.anotheria.portalkit.adminapi.api.shared.PageResult;
import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountUpdateRequest;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountsGetRequest;
import net.anotheria.portalkit.services.accountsettings.attribute.AttributeType;
import net.anotheria.portalkit.services.common.AccountId;

import java.util.List;

/**
 * API that provides basic admin functionality.
 */
public interface AdminAPI extends API {

    /**
     * Returns all account statuses
     *
     * @return list of {@link net.anotheria.portalkit.adminapi.config.AdminAPIConfig.AccountStatusConfig}
     * @throws APIException in case of error
     */
    List<AdminAPIConfig.AccountStatusConfig> getAccountStatuses() throws APIException;

    /**
     * Returns all account types
     *
     * @return list of {@link net.anotheria.portalkit.adminapi.config.AdminAPIConfig.AccountTypeConfig}
     * @throws APIException in case of error
     */
    List<AdminAPIConfig.AccountTypeConfig> getAccountTypes() throws APIException;

    /**
     * Returns paginated list of created in system accounts.
     *
     * @param pageNumber  number of page
     * @param itemsOnPage amount of items on page
     * @param searchTerm  some term like email, name to filter accounts
     * @return list of paginated {@link AdminAccountAO}
     * @throws APIException in case of error
     */
    PageResult<AdminAccountAO> getAccounts(int pageNumber, int itemsOnPage, String searchTerm) throws APIException;

    /**
     * Returns paginated list of created in system accounts.
     *
     * @param request criteria request
     * @return list of paginated {@link AdminAccountAO}
     * @throws APIException in case of error
     */
    PageResult<AdminAccountAO> getAccounts(AccountsGetRequest request) throws APIException;

    /**
     * Returns account by accountId
     *
     * @param accountId accountId
     * @return {@link AdminAccountAO}
     * @throws APIException in case of error
     */
    AdminAccountAO getAccountById(AccountId accountId) throws APIException;

    /**
     * Updates account information. Only email, name, brand, type or tenant can be updated.
     *
     * @param updateRequest {@link AccountUpdateRequest}
     * @return {@link AdminAccountAO}
     * @throws APIException in case of error
     */
    AdminAccountAO updateAccount(AccountUpdateRequest updateRequest) throws APIException;

    /**
     * Adds a status to provided account
     *
     * @param accountId accountId
     * @param status    status to add
     * @return {@link AdminAccountAO}
     * @throws APIException in case of error
     */
    AdminAccountAO addAccountStatus(AccountId accountId, int status) throws APIException;

    /**
     * Removes a status from provided account
     *
     * @param accountId accountId
     * @param status    status to remove
     * @return {@link AdminAccountAO}
     * @throws APIException in case of error
     */
    AdminAccountAO removeAccountStatus(AccountId accountId, int status) throws APIException;

    /**
     * Sets a new password for provided account
     *
     * @param accountId   accountId
     * @param newPassword new password to set
     * @throws APIException in case of error
     */
    void setNewAccountPassword(AccountId accountId, String newPassword) throws APIException;

    /**
     * Creates a token that can be used to log in as provided user.
     *
     * @param accountId account whose token will be created to perform log in
     * @return authToken
     * @throws APIException in case of error
     */
    String getSignAsToken(AccountId accountId) throws APIException;

    List<AdminAPIConfig.DataspaceConfig> getDataspaces() throws APIException;

    /**
     * Returns all account's dataspaces
     *
     * @param accountId dataspaces owner
     * @return {@link DataspaceAO}
     */
    List<DataspaceAO> getAllDataspaces(AccountId accountId);

    /**
     * Creates dataspace with for account with specific id and attributes.
     *
     * @param accountId   owner of dataspace
     * @param dataspaceId id of dataspace
     * @param attributes  list of attributes
     * @return {@link DataspaceAO}
     */
    DataspaceAO createDataspace(AccountId accountId, int dataspaceId, List<DataspaceAttributeAO> attributes) throws APIException;

    /**
     * Saves an attribute to existing dataspace.
     * If there is no dataspace with provided id, exception will be thrown.
     *
     * @param accountId      dataspace owner
     * @param dataspaceId    id of dataspace
     * @param attributeName  name of attribute
     * @param attributeValue value of attribute
     * @param type           type of attribute
     * @return {@link DataspaceAO}
     * @throws APIException in case of error
     */
    DataspaceAO saveDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName, String attributeValue, AttributeType type) throws APIException;

    /**
     * Removes an attribute from existing dataspace.
     * If there is no dataspace with provided id, exception will be thrown.
     *
     * @param accountId     dataspace owner
     * @param dataspaceId   id of dataspace
     * @param attributeName name of attribute
     * @return {@link DataspaceAO}
     * @throws APIException in case of error
     */
    DataspaceAO removeDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName) throws APIException;

    /**
     * Removes dataspace completely by account and dataspace id.
     * @param accountId dataspace owner
     * @param dataspaceId dataspace type (id)
     */
    void deleteDataspace(AccountId accountId, int dataspaceId) throws APIException;

}
