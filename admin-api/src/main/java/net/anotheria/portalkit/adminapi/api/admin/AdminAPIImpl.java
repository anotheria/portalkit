package net.anotheria.portalkit.adminapi.api.admin;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.adminapi.api.admin.dataspace.DataspaceAO;
import net.anotheria.portalkit.adminapi.api.admin.dataspace.DataspaceAttributeAO;
import net.anotheria.portalkit.adminapi.api.admin.dataspace.DataspaceExistsAPIException;
import net.anotheria.portalkit.adminapi.api.admin.dataspace.DataspaceTypeInternal;
import net.anotheria.portalkit.adminapi.api.shared.PageResult;
import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountUpdateRequest;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountsGetRequest;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountAdminService;
import net.anotheria.portalkit.services.account.AccountNotFoundException;
import net.anotheria.portalkit.services.account.AccountService;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsKey;
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

public class AdminAPIImpl extends AbstractAPIImpl implements AdminAPI {

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
                if (services != null) {
                    if (services.contains(AuthenticationService.class.getName().replace(".", "_"))) {
                        tmpAuthenticationService = MetaFactory.get(AuthenticationService.class);
                    } else {
                        tmpAuthenticationService = MetaFactory.get(SecretKeyAuthenticationService.class);
                    }

                    this.authenticationService = tmpAuthenticationService;
                } else {
                    throw new RuntimeException("Cannot get DiMe Registry");
                }
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
    public PageResult<AdminAccountAO> getAccounts(AccountsGetRequest request) throws APIException {
        PageResult<AdminAccountAO> result = new PageResult<>();
        try {
            List<AccountId> accountIds = new LinkedList<>(accountAdminService.getAllAccountIds());
            List<Account> accounts = accountService.getAccounts(accountIds);

            if (!StringUtils.isEmpty(request.getSearchTerm())) {
                accounts = accounts.stream()
                        .filter(e ->
                                e.getEmail().toLowerCase().contains(request.getSearchTerm().trim().toLowerCase()) ||
                                        e.getName().toLowerCase().contains(request.getSearchTerm().trim().toLowerCase()) ||
                                        e.getId().getInternalId().toLowerCase().contains(request.getSearchTerm().toLowerCase())
                        )
                        .collect(Collectors.toList());
            }

            // filtering by registration date range, included and excluded statuses
            accounts = accounts.stream().filter(e -> {
                for (String includedStatus : request.getIncludedStatuses()) {
                    AdminAPIConfig.AccountStatusConfig status = config.getStatus(includedStatus);
                    if (status != null) {
                        if (!e.hasStatus(status.getValue())) {
                            return false;
                        }
                    }
                }

                for (String excludedStatus : request.getExcludedStatuses()) {
                    AdminAPIConfig.AccountStatusConfig status = config.getStatus(excludedStatus);
                    if (status != null) {
                        if (e.hasStatus(status.getValue())) {
                            return false;
                        }
                    }
                }

                if (request.getRegistrationRange() != null) {
                    AccountsGetRequest.AccountRegistrationDateRange range = request.getRegistrationRange();
                    return e.getRegistrationTimestamp() >= range.getFrom() && e.getRegistrationTimestamp() <= range.getTo();
                }

                return true;
            }).collect(Collectors.toList());

            result.setTotalItems(accounts.size());
            result.setItemsOnPage(request.getItemsOnPage());
            result.setPageNumber(request.getPageIndex());

            // sorting by field
            switch (request.getSort().getField()) {
                case NAME:
                    switch (request.getSort().getDirection()) {
                        case ASC:
                            accounts = accounts.stream().sorted(Comparator.comparing(Account::getName)).collect(Collectors.toList());
                            break;
                        case DESC:
                            accounts = accounts.stream().sorted(Comparator.comparing(Account::getName).reversed()).collect(Collectors.toList());
                            break;
                    }
                    break;
                case EMAIL:
                    switch (request.getSort().getDirection()) {
                        case ASC:
                            accounts = accounts.stream().sorted(Comparator.comparing(Account::getEmail)).collect(Collectors.toList());
                            break;
                        case DESC:
                            accounts = accounts.stream().sorted(Comparator.comparing(Account::getEmail).reversed()).collect(Collectors.toList());
                            break;
                    }
                    break;
                case STATUS:
                    switch (request.getSort().getDirection()) {
                        case ASC:
                            accounts = accounts.stream().sorted(Comparator.comparing(Account::getStatus)).collect(Collectors.toList());
                            break;
                        case DESC:
                            accounts = accounts.stream().sorted(Comparator.comparing(Account::getStatus).reversed()).collect(Collectors.toList());
                            break;
                    }
                    break;
                case REGISTRATION_DATE:
                    switch (request.getSort().getDirection()) {
                        case ASC:
                            accounts = accounts.stream().sorted(Comparator.comparing(Account::getRegistrationTimestamp)).collect(Collectors.toList());
                            break;
                        case DESC:
                            accounts = accounts.stream().sorted(Comparator.comparing(Account::getRegistrationTimestamp).reversed()).collect(Collectors.toList());
                            break;
                    }
                    break;
            }

            int maxPage = accounts.size() / request.getItemsOnPage();

            if (request.getPageIndex() > maxPage) {
                accounts = Collections.emptyList();
                result.setTotalItems(0);
            } else if (accounts.size() >= request.getPageIndex()) {

                //in-memory pagination
                int fromIndex = request.getPageIndex() * request.getItemsOnPage();
                int toIndex = fromIndex + request.getItemsOnPage();

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
                boolean accountWithEmailExists = false;
                try {
                    AccountId accountId = accountService.getAccountIdByEmail(updateRequest.getEmail());
                    accountWithEmailExists = !accountId.equals(result.getId());
                } catch (AccountNotFoundException ignored) {
                }

                if (accountWithEmailExists) {
                    throw new EmailExistsAdminAPIException("Cannot update email. Another account with this email already exists");
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
                AdminAPIConfig.AccountTypeConfig accountType = config.getType(updateRequest.getType());
                if (accountType != null) {
                    result.setType(accountType.getValue());
                }
            }

            if (!updateRequest.getStatuses().isEmpty()) {
                result.setStatus(0);
                for (String status : updateRequest.getStatuses()) {
                    AdminAPIConfig.AccountStatusConfig accountStatus = config.getStatus(status);
                    if (accountStatus != null) {
                        result.addStatus(accountStatus.getValue());
                    }
                }
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
    public List<AdminAPIConfig.DataspaceConfig> getDataspaces() throws APIException {
        List<AdminAPIConfig.DataspaceConfig> result = null;
        try {
            result = new LinkedList<>(Arrays.asList(config.getDataspaces()));
        } catch (Exception any) {
            throw new APIException("Cannot get dataspaces config", any);
        }
        return result;
    }

    @Override
    public List<DataspaceAO> getAllDataspaces(AccountId accountId) {
        List<DataspaceAO> result = null;
        try {
            result = map(accountSettingsService.getAllDataspaces(accountId));
        } catch (Exception any) {
            log.error("Cannot get user's dataspaces", any);
        }
        return result;
    }

    @Override
    public DataspaceAO createDataspace(AccountId accountId, int dataspaceId, List<DataspaceAttributeAO> attributes) throws APIException {
        try {
            DataspaceAO result = null;
            for (Dataspace dataspace : accountSettingsService.getAllDataspaces(accountId)) {
                if (dataspace.getKey().getDataspaceId() == dataspaceId) {
                    throw new DataspaceExistsAPIException("Dataspace already exists");
                }
            }
            Dataspace toSave = new Dataspace();
            toSave.setKey(new AccountSettingsKey(accountId, dataspaceId));
            for (DataspaceAttributeAO attributeToMap : attributes) {
                Attribute attribute = Attribute.createAttribute(attributeToMap.getType(), attributeToMap.getAttributeName(), attributeToMap.getAttributeValue());
                toSave.addAttribute(attributeToMap.getAttributeName(), attribute);
            }
            accountSettingsService.saveDataspace(toSave);
            result = map(toSave);
            return result;
        } catch (DataspaceExistsAPIException ex) {
            throw new DataspaceExistsAPIException(ex.getMessage(), ex);
        } catch (Exception any) {
            log.error("Cannot create dataspace", any);
            throw new APIException("Cannot create dataspace", any);
        }
    }

    @Override
    public DataspaceAO saveDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName, String attributeValue, AttributeType type) throws APIException {
        DataspaceAO result = null;
        try {
            Dataspace toEdit = null;
            for (Dataspace dataspace : accountSettingsService.getAllDataspaces(accountId)) {
                if (dataspace.getKey().getDataspaceId() == dataspaceId) {
                    toEdit = dataspace;
                    break;
                }
            }

            if (toEdit == null) {
                throw new APIException("Cannot find dataspace by id: " + dataspaceId);
            }

            Attribute attributeToAdd = Attribute.createAttribute(type, attributeName, attributeValue);
            toEdit.addAttribute(attributeName, attributeToAdd);
            accountSettingsService.saveDataspace(toEdit);

            result = map(toEdit);
        } catch (Exception any) {
            log.error("Cannot add dataspace attribute", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public DataspaceAO removeDataspaceAttribute(AccountId accountId, int dataspaceId, String attributeName) throws APIException {
        DataspaceAO result = null;
        try {
            Dataspace toEdit = null;
            for (Dataspace dataspace : accountSettingsService.getAllDataspaces(accountId)) {
                if (dataspace.getKey().getDataspaceId() == dataspaceId) {
                    toEdit = dataspace;
                    break;
                }
            }

            if (toEdit == null) {
                throw new APIException("Cannot find dataspace by id: " + dataspaceId);
            }

            toEdit.removeAttribute(attributeName);
            accountSettingsService.saveDataspace(toEdit);

            result = map(toEdit);
        } catch (Exception any) {
            log.error("Cannot remove dataspace attribute", any);
            throw new APIException(any.getMessage(), any);
        }
        return result;
    }

    @Override
    public void deleteDataspace(AccountId accountId, int dataspaceId) throws APIException {
        try {
            accountSettingsService.deleteDataspace(accountId, dataspaceId);
        } catch (Exception any) {
            log.error("Cannot delete dataspace", any);
            throw new APIException(any.getMessage(), any);
        }
    }

    private AdminAccountAO map(Account toMap) {
        AdminAccountAO result = new AdminAccountAO();
        result.setAccountId(toMap.getId());
        result.setEmail(toMap.getEmail());
        result.setName(toMap.getName());
        result.setTenant(toMap.getTenant());
        result.setRandomUID(toMap.getRandomUID());
        result.setRegistrationTimestamp(toMap.getRegistrationTimestamp());
        result.setNumericType(toMap.getType());
        result.setStatus(toMap.getStatus());

        String type = null;
        List<String> statuses = new LinkedList<>();
        if (config != null) {
            for (AdminAPIConfig.AccountTypeConfig accountType : config.getTypes()) {
                if (toMap.getType() == accountType.getValue()) {
                    type = accountType.getName();
                    break;
                }
            }

            for (AdminAPIConfig.AccountStatusConfig status : config.getStatuses()) {
                if (toMap.hasStatus(status.getValue())) {
                    statuses.add(status.getName());
                }
            }
        }

        result.setType(type);
        result.setStatuses(statuses);

        return result;
    }

    private List<DataspaceAO> map(Collection<Dataspace> toMap) {
        List<DataspaceAO> result = new LinkedList<>();
        for (Dataspace dataspace : toMap) {
            result.add(map(dataspace));
        }
        return result;
    }

    private DataspaceAO map(Dataspace toMap) {
        DataspaceAO result = new DataspaceAO();
        AdminAPIConfig.DataspaceConfig dataspaceConfig = config.getDataspace(toMap.getKey().getDataspaceId());

        result.setAccountId(new AccountId(toMap.getKey().getAccountId()));
        result.setType(toMap.getKey().getDataspaceId());
        if (dataspaceConfig != null) {
            result.setName(dataspaceConfig.getName());
        }

        List<Attribute> attributes = new LinkedList<>();
        for (Map.Entry<String, Attribute> entry : toMap.getAttributes().entrySet()) {
            attributes.add(entry.getValue());
        }
        result.setAttributes(attributes);
        return result;
    }
}
