package net.anotheria.portalkit.adminapi.api.admin;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.adminapi.api.shared.PageResult;
import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;
import net.anotheria.portalkit.adminapi.rest.account.request.AccountUpdateRequest;
import net.anotheria.portalkit.services.account.*;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsKey;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsService;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsServiceException;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.accountsettings.attribute.AttributeType;
import net.anotheria.portalkit.services.accountsettings.attribute.StringAttribute;
import net.anotheria.portalkit.services.authentication.AuthenticationService;
import net.anotheria.portalkit.services.authentication.AuthenticationServiceException;
import net.anotheria.portalkit.services.authentication.EncryptedAuthToken;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.argThat;

@RunWith(MockitoJUnitRunner.class)
public class AdminAPIImplTest {

    @Mock
    private AccountService accountService;

    @Mock
    private AccountAdminService accountAdminService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private AccountSettingsService accountSettingsService;

    @Mock
    private AdminAPIConfig config;

    @Mock
    private AdminAPI adminAPI;

    @InjectMocks
    private AdminAPIImpl testAdminImpl = (AdminAPIImpl) AdminAPIFactory.getForUnitTests();

    @Test
    public void testGetAccountStatuses() throws APIException {

        // given
        AdminAPIConfig.AccountStatusConfig[] statuses = new AdminAPIConfig.AccountStatusConfig[]{new AdminAPIConfig.AccountStatusConfig(1, "REGISTERED"), new AdminAPIConfig.AccountStatusConfig(2, "PREMIUM")};
        given(config.getStatuses()).willReturn(statuses);

        // when
        List<AdminAPIConfig.AccountStatusConfig> result = testAdminImpl.getAccountStatuses();

        // then
        assertEquals(2, result.size());
        assertArrayEquals(statuses, result.toArray());
    }

    @Test
    public void testGetAccountTypes() throws APIException {

        // given
        AdminAPIConfig.AccountTypeConfig[] types = new AdminAPIConfig.AccountTypeConfig[]{new AdminAPIConfig.AccountTypeConfig(1, "USER"), new AdminAPIConfig.AccountTypeConfig(2, "ADMIN")};
        given(config.getTypes()).willReturn(types);

        // when
        List<AdminAPIConfig.AccountTypeConfig> result = testAdminImpl.getAccountTypes();

        // then
        assertEquals(2, result.size());
        assertArrayEquals(types, result.toArray());
    }

    @Test
    public void testGetAccounts() throws AccountAdminServiceException, AccountServiceException, APIException {

        // given
        int pageNumber = 0;
        int itemsOnPage = 1;
        String term = "test";

        AccountId accountId = AccountId.generateNew();
        AccountId accountId1 = AccountId.generateNew();
        AccountId accountId2 = AccountId.generateNew();

        List<AccountId> accountIds = new LinkedList<>(Arrays.asList(accountId, accountId1, accountId2));
        given(accountAdminService.getAllAccountIds()).willReturn(accountIds);

        Account account = new Account();
        account.setId(accountId);
        account.setEmail("email");
        account.setName("name");

        Account account1 = new Account();
        account1.setId(accountId1);
        account1.setEmail("TESTEmail");
        account1.setName("name");

        Account account2 = new Account();
        account2.setId(accountId2);
        account2.setEmail("TESTEmail123");
        account2.setName("name");

        List<Account> accounts = new LinkedList<>(Arrays.asList(account, account1, account2));
        given(accountService.getAccounts(accountIds)).willReturn(accounts);

        AdminAPIConfig.AccountTypeConfig[] types = new AdminAPIConfig.AccountTypeConfig[]{new AdminAPIConfig.AccountTypeConfig(1, "USER"), new AdminAPIConfig.AccountTypeConfig(2, "ADMIN")};
        given(config.getTypes()).willReturn(types);

        AdminAPIConfig.AccountStatusConfig[] statuses = new AdminAPIConfig.AccountStatusConfig[]{new AdminAPIConfig.AccountStatusConfig(1, "REGISTERED"), new AdminAPIConfig.AccountStatusConfig(2, "PREMIUM")};
        given(config.getStatuses()).willReturn(statuses);

        // when
        PageResult<AdminAccountAO> result = testAdminImpl.getAccounts(pageNumber, itemsOnPage, term);

        // then
        assertEquals(2, result.getTotalItems());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testGetAccountsEmptyList() throws AccountAdminServiceException, AccountServiceException, APIException {

        // given
        int pageNumber = 1;
        int itemsOnPage = 10;
        String term = "test";

        AccountId accountId = AccountId.generateNew();
        AccountId accountId1 = AccountId.generateNew();
        AccountId accountId2 = AccountId.generateNew();

        List<AccountId> accountIds = new LinkedList<>(Arrays.asList(accountId, accountId1, accountId2));
        given(accountAdminService.getAllAccountIds()).willReturn(accountIds);

        Account account = new Account();
        account.setId(accountId);
        account.setEmail("email");
        account.setName("name");

        Account account1 = new Account();
        account1.setId(accountId1);
        account1.setEmail("TESTEmail");
        account1.setName("name");

        Account account2 = new Account();
        account2.setId(accountId2);
        account2.setEmail("TESTEmail123");
        account2.setName("name");

        List<Account> accounts = new LinkedList<>(Arrays.asList(account, account1, account2));
        given(accountService.getAccounts(accountIds)).willReturn(accounts);

        // when
        PageResult<AdminAccountAO> result = testAdminImpl.getAccounts(pageNumber, itemsOnPage, term);

        // then
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalItems());
    }

    @Test
    public void testGetAccountsToIndexGreaterThenTotalItems() throws AccountAdminServiceException, AccountServiceException, APIException {

        // given
        int pageNumber = 1;
        int itemsOnPage = 3;
        String term = "test";

        AccountId accountId = AccountId.generateNew();
        AccountId accountId1 = AccountId.generateNew();
        AccountId accountId2 = AccountId.generateNew();
        AccountId accountId3 = AccountId.generateNew();
        AccountId accountId4 = AccountId.generateNew();

        List<AccountId> accountIds = new LinkedList<>(Arrays.asList(accountId, accountId1, accountId2, accountId3, accountId4));
        given(accountAdminService.getAllAccountIds()).willReturn(accountIds);

        Account account = new Account();
        account.setId(accountId);
        account.setEmail("email");
        account.setName("name");

        Account account1 = new Account();
        account1.setId(accountId1);
        account1.setEmail("TESTEmail");
        account1.setName("name");

        Account account2 = new Account();
        account2.setId(accountId2);
        account2.setEmail("TESTEmail123");
        account2.setName("name");

        Account account3 = new Account();
        account3.setId(accountId3);
        account3.setEmail("TESTEmail123");
        account3.setName("name");

        Account account4 = new Account();
        account4.setId(accountId3);
        account4.setEmail("TESTEmail123");
        account4.setName("name");

        List<Account> accounts = new LinkedList<>(Arrays.asList(account, account1, account2, account3, account4));
        given(accountService.getAccounts(accountIds)).willReturn(accounts);

        AdminAPIConfig.AccountTypeConfig[] types = new AdminAPIConfig.AccountTypeConfig[]{new AdminAPIConfig.AccountTypeConfig(1, "USER"), new AdminAPIConfig.AccountTypeConfig(2, "ADMIN")};
        given(config.getTypes()).willReturn(types);

        AdminAPIConfig.AccountStatusConfig[] statuses = new AdminAPIConfig.AccountStatusConfig[]{new AdminAPIConfig.AccountStatusConfig(1, "REGISTERED"), new AdminAPIConfig.AccountStatusConfig(2, "PREMIUM")};
        given(config.getStatuses()).willReturn(statuses);

        // when
        PageResult<AdminAccountAO> result = testAdminImpl.getAccounts(pageNumber, itemsOnPage, term);

        // then
        assertEquals(4, result.getTotalItems());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testGetAccountById() throws AccountServiceException, APIException {

        // given
        AccountId accountId = AccountId.generateNew();
        Account account = new Account();
        account.setId(accountId);

        given(accountService.getAccount(accountId)).willReturn(account);

        AdminAPIConfig.AccountTypeConfig[] types = new AdminAPIConfig.AccountTypeConfig[]{new AdminAPIConfig.AccountTypeConfig(1, "USER"), new AdminAPIConfig.AccountTypeConfig(2, "ADMIN")};
        given(config.getTypes()).willReturn(types);

        AdminAPIConfig.AccountStatusConfig[] statuses = new AdminAPIConfig.AccountStatusConfig[]{new AdminAPIConfig.AccountStatusConfig(1, "REGISTERED"), new AdminAPIConfig.AccountStatusConfig(2, "PREMIUM")};
        given(config.getStatuses()).willReturn(statuses);

        // when
        AdminAccountAO result = testAdminImpl.getAccountById(accountId);

        // then
        assertEquals(accountId, result.getAccountId());
    }

    @Test
    public void testUpdateAccountEmailOnly() throws APIException, AccountServiceException {

        // given
        AccountId accountId = AccountId.generateNew();
        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setId(accountId.getInternalId());
        updateRequest.setEmail("email-to-update");

        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setName("name");
        existingAccount.setEmail("old-email");

        given(accountService.getAccount(accountId)).willReturn(existingAccount);
        given(accountService.getAccountIdByEmail(updateRequest.getEmail())).willThrow(new AccountNotFoundException("Not found"));
        given(config.getStatuses()).willReturn(new AdminAPIConfig.AccountStatusConfig[]{});
        given(config.getTypes()).willReturn(new AdminAPIConfig.AccountTypeConfig[]{});

        // when
        testAdminImpl.updateAccount(updateRequest);

        // then
        then(accountService).should().updateAccount(argThat(e -> e.getEmail().equals(updateRequest.getEmail()) && e.getId().equals(accountId) && e.getName().equals(existingAccount.getName())));
    }

    @Test
    public void testUpdateAccountEmailExists() throws APIException, AccountServiceException {

        // given
        AccountId accountId = AccountId.generateNew();
        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setId(accountId.getInternalId());
        updateRequest.setEmail("email-to-update");

        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setName("name");
        existingAccount.setEmail("old-email");

        given(accountService.getAccount(accountId)).willReturn(existingAccount);
        given(accountService.getAccountIdByEmail(updateRequest.getEmail())).willReturn(AccountId.generateNew());

        try {

            // when
            testAdminImpl.updateAccount(updateRequest);

            // then
            fail("exception expected");
        } catch (APIException ignored) {
        }
    }

    @Test
    public void testUpdateAccountAll() throws APIException, AccountServiceException {

        // given
        AccountId accountId = AccountId.generateNew();
        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setId(accountId.getInternalId());
        updateRequest.setEmail("email-to-update");
        updateRequest.setName("name-to-update");
        updateRequest.setTenant("tenant-to-update");
        updateRequest.setType("USER");
        updateRequest.setBrand("brand-to-update");

        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setName("name");
        existingAccount.setEmail("old-email");
        existingAccount.setType(1);
        existingAccount.setTenant("old-tenant");
        existingAccount.setBrand("old-brand");

        given(accountService.getAccount(accountId)).willReturn(existingAccount);
        given(accountService.getAccountIdByEmail(updateRequest.getEmail())).willThrow(new AccountNotFoundException(""));

        given(config.getStatuses()).willReturn(new AdminAPIConfig.AccountStatusConfig[]{});
        given(config.getTypes()).willReturn(new AdminAPIConfig.AccountTypeConfig[]{});

        // when
        testAdminImpl.updateAccount(updateRequest);

        // then
        then(accountService).should().updateAccount(argThat(
                e -> e.getId().equals(accountId) && e.getName().equals(updateRequest.getName())
                        && e.getEmail().equals(updateRequest.getEmail()) && e.getBrand().equals(updateRequest.getBrand())
                        && e.getTenant().equals(updateRequest.getTenant())
        ));
    }

    @Test
    public void testUpdateAccountAccountIdEmpty() {

        // given
        AccountUpdateRequest updateRequest = new AccountUpdateRequest();

        try {

            // when
            testAdminImpl.updateAccount(updateRequest);

            // then
            fail("exception expected");
        } catch (APIException ignored) {
        }
    }

    @Test
    public void testAddAccountStatus() throws APIException, AccountServiceException {

        // given
        AccountId accountId = AccountId.generateNew();
        int status = 8;

        Account account = new Account();
        account.setId(accountId);

        given(accountService.getAccount(accountId)).willReturn(account);
        given(accountService.updateAccount(argThat(e -> e.getId().equals(accountId)))).willReturn(account);

        given(config.getStatuses()).willReturn(new AdminAPIConfig.AccountStatusConfig[]{});
        given(config.getTypes()).willReturn(new AdminAPIConfig.AccountTypeConfig[]{});

        // when
        testAdminImpl.addAccountStatus(accountId, status);

        // then
        then(accountService).should().updateAccount(argThat(e -> e.getId().equals(accountId) && e.getStatus() == status));
    }

    @Test
    public void testRemoveAccountStatus() throws AccountServiceException, APIException {

        // given
        AccountId accountId = AccountId.generateNew();
        int status = 4;

        Account account = new Account();
        account.setId(accountId);
        account.setStatus(4);

        given(accountService.getAccount(accountId)).willReturn(account);
        given(accountService.updateAccount(argThat(e -> e.getId().equals(accountId)))).willReturn(account);

        given(config.getStatuses()).willReturn(new AdminAPIConfig.AccountStatusConfig[]{});
        given(config.getTypes()).willReturn(new AdminAPIConfig.AccountTypeConfig[]{});

        // when
        testAdminImpl.removeAccountStatus(accountId, status);

        // then
        then(accountService).should().updateAccount(argThat(e -> e.getId().equals(accountId) && e.getStatus() == 0));
    }

    @Test
    public void testSetNewAccountPassword() throws APIException, AuthenticationServiceException {

        // given
        AccountId accountId = AccountId.generateNew();
        String newPassword = "new-password";

        // when
        testAdminImpl.setNewAccountPassword(accountId, newPassword);

        // then
        then(authenticationService).should().setPassword(accountId, newPassword);
    }

    @Test
    public void testGetSignAsToken() throws AuthenticationServiceException, APIException {

        // given
        AccountId accountId = AccountId.generateNew();

        AdminAPIConfig.AuthTokenConfig[] tokens = new AdminAPIConfig.AuthTokenConfig[]{new AdminAPIConfig.AuthTokenConfig(0, "AUTH_TOKEN", false), new AdminAPIConfig.AuthTokenConfig(1, "DIRECT_LOGIN", true)};
        given(config.getTokens()).willReturn(tokens);

        String token = "token";
        EncryptedAuthToken encryptedAuthToken = new EncryptedAuthToken();
        encryptedAuthToken.setEncryptedVersion(token);
        given(authenticationService.generateEncryptedToken(eq(accountId), argThat(e -> e.getAccountId().equals(accountId) && e.getType() == 1 && e.isMultiUse()))).willReturn(encryptedAuthToken);

        // when
        String result = testAdminImpl.getSignAsToken(accountId);

        // then
        assertEquals(token, result);
    }

    @Test
    public void testGetAllDataspaces() throws AccountSettingsServiceException {

        // given
        AccountId accountId = AccountId.generateNew();

        List<Dataspace> dataspaces = new LinkedList<>();

        Dataspace dataspace = new Dataspace();
        dataspace.setKey(new AccountSettingsKey(accountId, 1));
        dataspaces.add(dataspace);

        Dataspace dataspace1 = new Dataspace();
        dataspace1.setKey(new AccountSettingsKey(accountId, 2));
        dataspaces.add(dataspace1);

        given(accountSettingsService.getAllDataspaces(accountId)).willReturn(dataspaces);

        // when
        List<DataspaceAO> result = testAdminImpl.getAllDataspaces(accountId);

        // then
        assertEquals(dataspace.getKey().getDataspaceId(), result.get(0).getType());
        assertEquals(new AccountId(dataspace.getKey().getAccountId()), result.get(0).getAccountId());
        assertEquals(dataspace1.getKey().getDataspaceId(), result.get(1).getType());
        assertEquals(new AccountId(dataspace1.getKey().getAccountId()), result.get(1).getAccountId());
    }

    @Test
    public void testAddDataspaceAttribute() throws AccountSettingsServiceException, APIException {

        // given
        AccountId accountId = AccountId.generateNew();
        int dataspaceId = 1;
        String attributeName = "attrName";
        String attributeValue = "attrValue";
        AttributeType type = AttributeType.STRING;

        List<Dataspace> dataspaces = new LinkedList<>();
        Dataspace dataspace = new Dataspace();
        dataspace.setKey(new AccountSettingsKey(accountId, dataspaceId));
        dataspaces.add(dataspace);

        given(accountSettingsService.getAllDataspaces(accountId)).willReturn(dataspaces);

        // when
        DataspaceAO result = testAdminImpl.saveDataspaceAttribute(accountId, dataspaceId, attributeName, attributeValue, type);

        // then
        then(accountSettingsService).should().saveDataspace(argThat(e -> e.getKey().getAccountId().equals(accountId.getInternalId()) && e.getKey().getDataspaceId() == dataspaceId));
        assertEquals(attributeName, result.getAttributes().get(0).getName());
        assertEquals(attributeValue, result.getAttributes().get(0).getValueAsString());
        assertEquals(type, result.getAttributes().get(0).getType());
    }

    @Test
    public void testAddDataspaceAttributeNoDataspace() throws AccountSettingsServiceException, APIException {

        // given
        AccountId accountId = AccountId.generateNew();
        int dataspaceId = 1;
        String attributeName = "attrName";
        String attributeValue = "attrValue";
        AttributeType type = AttributeType.STRING;

        given(accountSettingsService.getAllDataspaces(accountId)).willReturn(Collections.emptyList());

        try {

            // when
            testAdminImpl.saveDataspaceAttribute(accountId, dataspaceId, attributeName, attributeValue, type);

            // then
            fail("exception expected");
        } catch (APIException ignored) {

        }
    }

    @Test
    public void testRemoveDataspaceAttribute() throws AccountSettingsServiceException, APIException {

        // given
        AccountId accountId = AccountId.generateNew();
        int dataspaceId = 1;
        String attributeName = "attrName";

        List<Dataspace> dataspaces = new LinkedList<>();
        Dataspace dataspace = new Dataspace();
        dataspace.setKey(new AccountSettingsKey(accountId, dataspaceId));
        dataspace.addAttribute(attributeName, new StringAttribute(attributeName, "someAttr"));
        dataspaces.add(dataspace);

        given(accountSettingsService.getAllDataspaces(accountId)).willReturn(dataspaces);

        // when
        DataspaceAO result = testAdminImpl.removeDataspaceAttribute(accountId, dataspaceId, attributeName);

        // then
        then(accountSettingsService).should().saveDataspace(argThat(e -> e.getKey().getDataspaceId() == dataspaceId && e.getKey().getAccountId().equals(accountId.getInternalId())));
        assertTrue(result.getAttributes().isEmpty());
    }

    @Test
    public void testRemoveDataspaceAttributeNoDataspace() throws AccountSettingsServiceException, APIException {

        // given
        AccountId accountId = AccountId.generateNew();
        int dataspaceId = 1;
        String attributeName = "attrName";

        given(accountSettingsService.getAllDataspaces(accountId)).willReturn(Collections.emptyList());

        try {

            // when
            testAdminImpl.removeDataspaceAttribute(accountId, dataspaceId, attributeName);

            // then
            fail("exception expected");
        } catch (APIException ignored) {
        }
    }
}
