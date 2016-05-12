package net.anotheria.portalkit.services.match;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.portalkit.services.common.AccountId;
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
public class MatchServiceCacheTest {

    private static final AccountId ACCOUNT_A = new AccountId("A");
    private static final AccountId ACCOUNT_B = new AccountId("B");

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private Cache<String, Boolean> isMatchedCacheMock;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    public void testIsMatched_emptyCache() throws MatchServiceException {
        MatchId matchId = new MatchId(ACCOUNT_A.getInternalId(), ACCOUNT_B.getInternalId());
        when(entityManagerMock.find(MatchEntity.class, matchId)).thenReturn(new MatchEntity());

        matchService.isMatched(ACCOUNT_A, ACCOUNT_B, 0);

        String cacheKey = "A|B|0";
        verify(isMatchedCacheMock).get(cacheKey);
        verify(isMatchedCacheMock).put(cacheKey, true);

        verify(entityManagerMock).find(MatchEntity.class, matchId);
    }

    @Test
    public void testIsMatched_filledCache() throws MatchServiceException {
        String cacheKey = "A|B|0";
        when(isMatchedCacheMock.get(cacheKey)).thenReturn(true);

        matchService.isMatched(ACCOUNT_A, ACCOUNT_B, 0);

        verify(isMatchedCacheMock).get(cacheKey);
        verifyNoMoreInteractions(isMatchedCacheMock);

        verifyZeroInteractions(entityManagerMock);
    }
}
