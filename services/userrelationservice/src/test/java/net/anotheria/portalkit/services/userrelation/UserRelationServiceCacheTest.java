package net.anotheria.portalkit.services.userrelation;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.userrelation.exception.UserRelationServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.*;

/**
 * @author bvanchuhov
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRelationServiceCacheTest {

    private static final AccountId ACCOUNT_A = new AccountId("A");
    private static final AccountId ACCOUNT_B = new AccountId("B");
    private static final String RELATION_NAME = "T0";
    private static final String CACHE_KEY = "A|B|T0";

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private Cache<String, Boolean> isRelatedCacheMock;

    @InjectMocks
    private UserRelationServiceImpl relationService;

    @Test
    public void isRelated_emptyCache() throws UserRelationServiceException {
        UserRelationId userRelationId = new UserRelationId(ACCOUNT_A, ACCOUNT_B, "T0");
        when(entityManagerMock.find(UserRelationEntity.class, userRelationId)).thenReturn(new UserRelationEntity());

        relationService.isRelated(ACCOUNT_A, ACCOUNT_B, RELATION_NAME);

        verify(isRelatedCacheMock).get(CACHE_KEY);
        verify(isRelatedCacheMock).put(CACHE_KEY, true);

        verify(entityManagerMock).find(UserRelationEntity.class, userRelationId);
    }

    @Test
    public void isRelated_filledCache() throws UserRelationServiceException {
        when(isRelatedCacheMock.get(CACHE_KEY)).thenReturn(true);

        relationService.isRelated(ACCOUNT_A, ACCOUNT_B, RELATION_NAME);

        verify(isRelatedCacheMock).get(CACHE_KEY);
        verifyNoMoreInteractions(isRelatedCacheMock);

        verifyZeroInteractions(entityManagerMock);
    }
}
