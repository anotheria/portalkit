package net.anotheria.portalkit.services.match;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.match.exception.MatchServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author bvanchuhov
 */
@RunWith(MockitoJUnitRunner.class)
public class MatchServiceCacheTest {

    private static final AccountId ACCOUNT_A = new AccountId("A");
    private static final AccountId ACCOUNT_B = new AccountId("B");
    private static final AccountId ACCOUNT_C = new AccountId("C");

    private static final Match MATCH_A_B_0 = new Match(ACCOUNT_A, ACCOUNT_B, 0);
    private static final Match MATCH_A_C_0 = new Match(ACCOUNT_A, ACCOUNT_C, 0);
    private static final Match MATCH_A_C_1 = new Match(ACCOUNT_A, ACCOUNT_C, 1);

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private Cache<String, Boolean> isMatchedCacheMock;

    @Mock (name = "ownersCache")
    private Cache<AccountId, List<Match>> ownersCacheMock;

    @Mock (name = "targetsCache")
    private Cache<AccountId, List<Match>> targetsCacheMock;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    public void testIsMatched_emptyCache() throws MatchServiceException {
        MatchId matchId = new MatchId(ACCOUNT_A, ACCOUNT_B, 0);
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

        verifyNoInteractions(entityManagerMock);
    }

    @Test
    public void getMatches_emptyCache () throws MatchServiceException {

        MatchEntity entity = new MatchEntity();
        entity.setOwnerId(ACCOUNT_A.getInternalId());
        entity.setTargetId(ACCOUNT_B.getInternalId());
        entity.setType(0);

        TypedQuery queryMock = mock(TypedQuery.class);
        when(queryMock.setParameter("ownerId", ACCOUNT_A.getInternalId())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Arrays.asList(entity));

        when(entityManagerMock.createNamedQuery(MatchEntity.JPQL_GET_BY_OWNER, MatchEntity.class)).thenReturn(queryMock);

        List<Match> actual = matchService.getMatches(ACCOUNT_A);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(MATCH_A_B_0.getOwner(), actual.get(0).getOwner());
        assertEquals(MATCH_A_B_0.getTarget(), actual.get(0).getTarget());
        assertEquals(MATCH_A_B_0.getType(), actual.get(0).getType());

        verify(ownersCacheMock, times(1)).get(ACCOUNT_A);
        verify(ownersCacheMock, times(1)).put(eq(ACCOUNT_A), anyList());
    }



    @Test
    public void getMatches_filledCache() throws MatchServiceException {

        when(ownersCacheMock.get(ACCOUNT_A)).thenReturn(Arrays.asList(MATCH_A_B_0, MATCH_A_C_0, MATCH_A_C_1));

        List<Match> actual = matchService.getMatches(ACCOUNT_A);

        assertNotNull(actual);
        assertArrayEquals(new Object[]{MATCH_A_B_0, MATCH_A_C_0, MATCH_A_C_1}, actual.toArray());

        verify(ownersCacheMock, times(1)).get(ACCOUNT_A);
        verifyNoInteractions(entityManagerMock);
    }

    @Test
    public void getTargetMatches_emptyCache () throws MatchServiceException {

        MatchEntity entity = new MatchEntity();
        entity.setOwnerId(ACCOUNT_A.getInternalId());
        entity.setTargetId(ACCOUNT_B.getInternalId());
        entity.setType(0);

        TypedQuery queryMock = mock(TypedQuery.class);
        when(queryMock.setParameter("targetId", ACCOUNT_B.getInternalId())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Arrays.asList(entity));

        when(entityManagerMock.createNamedQuery(MatchEntity.JPQL_GET_BY_TARGET, MatchEntity.class)).thenReturn(queryMock);

        List<Match> actual = matchService.getTargetMatches(ACCOUNT_B);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(MATCH_A_B_0.getOwner(), actual.get(0).getOwner());
        assertEquals(MATCH_A_B_0.getTarget(), actual.get(0).getTarget());
        assertEquals(MATCH_A_B_0.getType(), actual.get(0).getType());

        verify(targetsCacheMock, times(1)).get(ACCOUNT_B);
        verify(targetsCacheMock, times(1)).put(eq(ACCOUNT_B), anyList());
    }

    @Test
    public void getTargetMatches_filledCache() throws MatchServiceException {

        when(targetsCacheMock.get(ACCOUNT_C)).thenReturn(Arrays.asList(MATCH_A_C_0, MATCH_A_C_1));

        List<Match> actual = matchService.getTargetMatches(ACCOUNT_C);

        assertNotNull(actual);
        assertArrayEquals(new Object[]{MATCH_A_C_0, MATCH_A_C_1}, actual.toArray());

        verify(targetsCacheMock, times(1)).get(ACCOUNT_C);
        verifyNoInteractions(entityManagerMock);
    }
}
