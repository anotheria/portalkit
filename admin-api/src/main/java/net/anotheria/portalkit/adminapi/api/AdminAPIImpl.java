package net.anotheria.portalkit.adminapi.api;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountUpdateRequest;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountAdminService;
import net.anotheria.portalkit.services.account.AccountService;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsService;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.accountsettings.attribute.Attribute;
import net.anotheria.portalkit.services.accountsettings.attribute.AttributeType;
import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.authentication.AuthenticationService;
import net.anotheria.portalkit.services.authentication.AuthenticationServiceException;
import net.anotheria.portalkit.services.authentication.SecretKeyAuthenticationService;
import net.anotheria.portalkit.services.common.AccountId;
import org.apache.commons.lang3.StringUtils;
import org.distributeme.core.RegistryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class AdminAPIImpl implements AdminAPI {

    private static final Logger log = LoggerFactory.getLogger(AdminAPIImpl.class);

    private AccountService accountService;
    private AccountAdminService accountAdminService;
    private AuthenticationService authenticationService;
    private AccountSettingsService accountSettingsService;

    private AdminAPIConfig config;

    protected AdminAPIImpl(boolean unitTest) {
        if (!unitTest) {
            this.config = AdminAPIConfig.getInstance();

            try {
                AuthenticationService tmpAuthenticationService;

                this.accountService = MetaFactory.get(AccountService.class);
                this.accountAdminService = MetaFactory.get(AccountAdminService.class);
                this.accountSettingsService = MetaFactory.get(AccountSettingsService.class);
                this.authenticationService = MetaFactory.get(AuthenticationService.class);

                String services = RegistryUtil.getXMLServiceList();
                if (services.contains(AuthenticationService.class.getName().replace(".", "_"))) {
                    tmpAuthenticationService = MetaFactory.get(AuthenticationService.class);
                } else {
                    tmpAuthenticationService = MetaFactory.get(SecretKeyAuthenticationService.class);
                }

                this.authenticationService = tmpAuthenticationService;
            } catch (MetaFactoryException ex) {
                log.error("Cannot initialize AccountResource", ex);
            }
        }
    }

    @Override
    public List<AdminAPIConfig.AccountStatusConfig> getAccountStatuses() throws APIException {
        List<AdminAPIConfig.AccountStatusConfig> result = null;
        try {
            result = new LinkedList<>(Arrays.asList(config.getStatuses()));
        } catch (Exception any) {
            log.error("Cannot get account statuses", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public List<AdminAPIConfig.AccountTypeConfig> getAccountTypes() throws APIException {
        List<AdminAPIConfig.AccountTypeConfig> result = null;
        try {
            result = new LinkedList<>(Arrays.asList(config.getTypes()));
        } catch (Exception any) {
            log.error("Cannot get account types", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public PageResult<AdminAccountAO> getAccounts(int pageNumber, int itemsOnPage, String searchTerm) throws APIException {
        PageResult<AdminAccountAO> result = new PageResult<>();
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
            int maxPage = accounts.size() / itemsOnPage;

            if (pageNumber > maxPage) {
                accounts = Collections.emptyList();
                result.setTotalItems(0);
            } else if (accounts.size() >= itemsOnPage) {

                //in-memory pagination
                int fromIndex = pageNumber * itemsOnPage;
                int toIndex = fromIndex + itemsOnPage;

                if (toIndex > accounts.size()) {
                    toIndex = accounts.size();
                }

                accounts = new ArrayList<>(accounts.subList(fromIndex, toIndex));
            }

            result.setContent(accounts.stream().map(this::map).collect(Collectors.toList()));
        } catch (Exception any) {
            log.error("Cannot get accounts");
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public AdminAccountAO getAccountById(AccountId accountId) throws APIException {
        Account result = null;
        try {
            result = accountService.getAccount(accountId);
        } catch (Exception any) {
            log.error("Cannot get account by id", any);
            throw new APIException(any.getMessage(), any);
        }
        return map(result);
    }

    @Override
    public AdminAccountAO updateAccount(AccountUpdateRequest updateRequest) throws APIException {
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
        return map(result);
    }

    @Override
    public AdminAccountAO addAccountStatus(AccountId accountId, int status) throws APIException {
        Account result = null;
        try {
            result = accountService.getAccount(accountId);
            result.addStatus(status);
            result = accountService.updateAccount(result);
        } catch (Exception any) {
            log.error("Cannot add status to account", any);
            throw new APIException(any.getMessage(), any);
        }
        return map(result);
    }

    @Override
    public AdminAccountAO removeAccountStatus(AccountId accountId, int status) throws APIException {
        Account result = null;
        try {
            result = accountService.getAccount(accountId);
            result.removeStatus(status);
            result = accountService.updateAccount(result);
        } catch (Exception any) {
            log.error("Cannot add status to account", any);
            throw new APIException(any.getMessage(), any);
        }
        return map(result);
    }

    @Override
    public void setNewAccountPassword(AccountId accountId, String newPassword) throws APIException {
        try {
            authenticationService.setPassword(accountId, newPassword);
        } catch (AuthenticationServiceException ex) {
            log.error("Cannot update account password", ex);
            throw new APIException(ex.getMessage(), ex);
        }
    }

    @Override
    public String getSignAsToken(AccountId accountId) throws APIException {
        String result = null;
        Integer tokenValue = null;
        try {
            for (AdminAPIConfig.AuthTokenConfig token : config.getTokens()) {
                if (token.isSignAs()) {
                    tokenValue = token.getValue();
                    break;
                }
            }

            if (tokenValue != null) {
                AuthToken token = new AuthToken();
                token.setAccountId(accountId);
                token.setExclusiveInType(false);
                token.setMultiUse(true);
                token.setExpiryTimestamp(System.currentTimeMillis() + net.anotheria.util.TimeUnit.DAY.getMillis(1));
                token.setType(tokenValue);

                result = authenticationService.generateEncryptedToken(accountId, token).getEncryptedVersion();
            } else {
                log.error("Cannot find a signAs token");
                throw new APIException("Cannot find signAs token");
            }
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

    private AdminAccountAO map(Account toMap) {
        AdminAccountAO result = new AdminAccountAO();
        result.setAccountId(toMap.getId());
        result.setEmail(toMap.getEmail());
        result.setName(toMap.getName());
        result.setTenant(toMap.getTenant());
        result.setRandomUID(toMap.getRandomUID());
        result.setRegistrationTimestamp(toMap.getRegistrationTimestamp());

        String type = null;
        for (AdminAPIConfig.AccountTypeConfig accountType : config.getTypes()) {
            if (toMap.getType() == accountType.getValue()) {
                type = accountType.getName();
                break;
            }
        }
        result.setType(type);

        List<String> statuses = new LinkedList<>();
        for (AdminAPIConfig.AccountStatusConfig status : config.getStatuses()) {
            if (toMap.hasStatus(status.getValue())) {
                statuses.add(status.getName());
            }
        }
        result.setStatuses(statuses);
        return result;
    }
}
