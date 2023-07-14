package net.anotheria.portalkit.adminapi.api;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.adminapi.config.AdminAPIConfig;
import net.anotheria.portalkit.services.account.*;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsService;
import net.anotheria.portalkit.services.authentication.AuthenticationService;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

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
    private AdminAPIImpl testAdminImpl = new AdminAPIImpl();

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

}
