package net.anotheria.portalkit.adminapi.api;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.adminapi.rest.account.AccountUpdateRequest;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountAdminService;
import net.anotheria.portalkit.services.account.AccountService;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsService;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.accountsettings.attribute.Attribute;
import net.anotheria.portalkit.services.accountsettings.attribute.AttributeType;
import net.anotheria.portalkit.services.authentication.AuthenticationService;
import net.anotheria.portalkit.services.common.AccountId;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminAPIImpl extends AbstractAPIImpl implements AdminAPI {

    private AccountService accountService;
    private AccountAdminService accountAdminService;
    private AuthenticationService authenticationService;
    private AccountSettingsService accountSettingsService;

    @Override
    public void init() throws APIInitException {
        super.init();

        try {
            this.accountService = MetaFactory.get(AccountService.class);
            this.accountAdminService = MetaFactory.get(AccountAdminService.class);
            this.authenticationService = MetaFactory.get(AuthenticationService.class);
            this.accountSettingsService = MetaFactory.get(AccountSettingsService.class);
        } catch (MetaFactoryException ex) {
            log.error("Cannot initialize AccountResource", ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public PageResult<Account> getAccounts(int pageNumber, int itemsOnPage, String searchTerm) throws APIException {
        PageResult<Account> result = new PageResult<>();
        try {
            List<AccountId> accountIds = new LinkedList<>(accountAdminService.getAllAccountIds());
            List<Account> accounts = accountService.getAccounts(accountIds);

            if (!StringUtils.isEmpty(searchTerm)) {
                accounts = accounts.stream()
                        .filter(e ->
                                e.getEmail().toLowerCase().contains(searchTerm.trim().toLowerCase()) ||
                                        e.getName().toLowerCase().contains(searchTerm.trim().toLowerCase())
                        )
                        .collect(Collectors.toList());
            }

            result.setTotalItems(accounts.size());
            result.setItemsOnPage(itemsOnPage);
            result.setPageNumber(pageNumber);

            if (accounts.size() >= itemsOnPage) {

                //in-memory pagination
                int fromIndex = pageNumber * itemsOnPage;
                int toIndex = fromIndex + itemsOnPage;

                if (toIndex > accounts.size()) {
                    toIndex = accounts.size();
                }

                accounts = new ArrayList<>(accounts.subList(fromIndex, toIndex));
            }

            result.setContent(accounts);
        } catch (Exception any) {
            log.error("Cannot get accounts");
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public Account getAccountById(AccountId accountId) throws APIException {
        Account result = null;
        try {
            result = accountService.getAccount(accountId);
        } catch (Exception any) {
            log.error("Cannot get account by id", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public Account updateAccount(AccountUpdateRequest updateRequest) throws APIException {
        if (updateRequest.getId() == null) {
            throw new AccountIdEmptyAdminAPIException();
        }

        Account result = null;
        try {
            result = accountService.getAccount(new AccountId(updateRequest.getId()));
            if (!StringUtils.isEmpty(updateRequest.getEmail())) {
                if (!accountService.getAccountIdByEmail(updateRequest.getEmail()).equals(result.getId())) {
                    throw new EmailExistsAdminAPIException();
                }
                result.setEmail(updateRequest.getEmail());
            }
            if (!StringUtils.isEmpty(updateRequest.getBrand())) {
                result.setBrand(updateRequest.getBrand());
            }
            if (!StringUtils.isEmpty(updateRequest.getName())) {
                result.setName(updateRequest.getName());
            }
            if (!StringUtils.isEmpty(updateRequest.getTenant())) {
                result.setTenant(updateRequest.getTenant());
            }
            if (updateRequest.getType() != null) {
                result.setType(updateRequest.getType());
            }

            accountService.updateAccount(result);
        } catch (Exception any) {
            log.error("Cannot update account", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public Account addAccountStatus(AccountId accountId, int status) throws APIException {
        Account result = null;
        try {
            result = accountService.getAccount(accountId);
            result.addStatus(status);
            result = accountService.updateAccount(result);
        } catch (Exception any) {
            log.error("Cannot add status to account", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public Account removeAccountStatus(AccountId accountId, int status) throws APIException {
        Account result = null;
        try {
            result = accountService.getAccount(accountId);
            result.removeStatus(status);
            result = accountService.updateAccount(result);
        } catch (Exception any) {
            log.error("Cannot add status to account", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public void setNewAccountPassword(AccountId accountId, String newPassword) throws APIException {
        try {
            authenticationService.setPassword(accountId, newPassword);
        } catch (Exception any) {
            log.error("Cannot update account password", any);
            throw new APIException(any.getMessage(), any);
        }
    }

    @Override
    public String getSignAsToken(AccountId accountId) throws APIException {
        String result = null;
        try {
            // TDB
        } catch (Exception any) {
            log.error("Cannot get sign as token", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public List<Dataspace> getAllDataspaces(AccountId accountId) {
        List<Dataspace> result = null;
        try {
            result = new LinkedList<>(accountSettingsService.getAllDataspaces(accountId));
        } catch (Exception any) {
            log.error("Cannot get user's dataspaces", any);
        }
        return result;
    }

    @Override
    public Dataspace addDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName, String attributeValue, AttributeType type) throws APIException {
        Dataspace result = null;
        try {
            for (Dataspace dataspace : getAllDataspaces(accountId)) {
                if (dataspace.getKey().getDataspaceId() == dataspaceId) {
                    result = dataspace;
                    break;
                }
            }

            if (result == null) {
                throw new APIException("Cannot find dataspace by id: " + dataspaceId);
            }

            Attribute attributeToAdd = Attribute.createAttribute(type, attributeName, attributeValue);
            result.addAttribute(attributeName, attributeToAdd);
            accountSettingsService.saveDataspace(result);
        } catch (Exception any) {
            log.error("Cannot add dataspace attribute", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public Dataspace removeDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName) throws APIException {
        Dataspace result = null;
        try {
            for (Dataspace dataspace : getAllDataspaces(accountId)) {
                if (dataspace.getKey().getDataspaceId() == dataspaceId) {
                    result = dataspace;
                    break;
                }
            }

            if (result == null) {
                throw new APIException("Cannot find dataspace by id: " + dataspaceId);
            }

            result.removeAttribute(attributeName);
            accountSettingsService.saveDataspace(result);
        } catch (Exception any) {
            log.error("Cannot remove dataspace attribute", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }
}
