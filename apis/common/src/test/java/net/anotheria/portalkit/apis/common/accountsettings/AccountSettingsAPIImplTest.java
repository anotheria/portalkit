package net.anotheria.portalkit.apis.common.accountsettings;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsService;
import net.anotheria.portalkit.services.accountsettings.AccountSettingsServiceException;
import net.anotheria.portalkit.services.accountsettings.Dataspace;
import net.anotheria.portalkit.services.accountsettings.DataspaceType;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountSettingsAPIImplTest {

    @Mock
    AccountSettingsService accountSettingsService;

    @InjectMocks
    AccountSettingsAPIImpl api = new AccountSettingsAPIImpl();

    private static final AccountId ACCOUNT_ID = AccountId.generateNew();

    private static final int DATA_SPACE_ID_LIMIT = 5;

    @Test(expected = APIException.class)
    public void deleteDataSpacesExceptionTest() throws APIException, AccountSettingsServiceException {
        // Given
        LinkedList<Dataspace> dsList = new LinkedList<>();
        dsList.add(new Dataspace(ACCOUNT_ID, new DataspaceType() {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "";
            }
        }));
        when(accountSettingsService.getAllDataspaces(ACCOUNT_ID)).thenReturn(dsList);
        when(accountSettingsService.deleteDataspace(eq(ACCOUNT_ID), eq(Integer.valueOf(1)))).thenThrow(AccountSettingsServiceException.class);

        // When
        api.deleteDataspaces(ACCOUNT_ID);
    }

    @Test
    public void deleteDataSpacesTest() throws APIException, AccountSettingsServiceException {

        ArrayList<Dataspace> dummyDs = new ArrayList<>();
        for (int i=0; i<DATA_SPACE_ID_LIMIT; i++) {
            final int id = i;
            dummyDs.add(new Dataspace(ACCOUNT_ID, new DataspaceType() {
                @Override
                public int getId() {
                    return id;
                }

                @Override
                public String getName() {
                    return "";
                }
            }));
        }
        // When
        when(accountSettingsService.getAllDataspaces(ACCOUNT_ID)).thenReturn(dummyDs);


        api.deleteDataspaces(ACCOUNT_ID);

        // Then
        for (int i=0; i<DATA_SPACE_ID_LIMIT; i++) {
            final int id = i;
            verify(accountSettingsService).deleteDataspace(eq(ACCOUNT_ID), eq(id));
        }

    }

}
