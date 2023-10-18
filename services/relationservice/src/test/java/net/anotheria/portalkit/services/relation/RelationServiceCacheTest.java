package net.anotheria.portalkit.services.relation;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.relation.exception.RelationNotFoundException;
import net.anotheria.portalkit.services.relation.exception.RelationServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import static net.anotheria.portalkit.services.relation.RelationServiceImpl.NULL_RELATION;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * @author bvanchuhov
 */
@RunWith(MockitoJUnitRunner.class)
public class RelationServiceCacheTest {

    private static final AccountId OWNER = new AccountId("A");
    private static final AccountId PARTNER = new AccountId("B");
    private static final String RELATION_NAME = "T0";
    private static final String CACHE_KEY = "A|B|T0";

    private static final Relation CACHED_RELATION = new Relation(OWNER, PARTNER, RELATION_NAME);

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private Cache<String, Relation> isRelatedCacheMock;

    @InjectMocks
    private RelationServiceImpl relationService;

    @Test
    public void testIsRelated_emptyCache() throws RelationServiceException {
        RelationId relationId = new RelationId(OWNER, PARTNER, RELATION_NAME);
        when(entityManagerMock.find(RelationEntity.class, relationId)).thenReturn(new RelationEntity(OWNER, PARTNER, RELATION_NAME));

        relationService.isRelated(OWNER, PARTNER, RELATION_NAME);

        verify(isRelatedCacheMock).get(CACHE_KEY);
        verify(isRelatedCacheMock).put(CACHE_KEY, new Relation(OWNER, PARTNER, RELATION_NAME));

        verify(entityManagerMock).find(RelationEntity.class, relationId);
    }

    @Test
    public void testIsRelated_filledCache() throws RelationServiceException {
        when(isRelatedCacheMock.get(CACHE_KEY)).thenReturn(CACHED_RELATION);

        relationService.isRelated(OWNER, PARTNER, RELATION_NAME);

        verify(isRelatedCacheMock).get(CACHE_KEY);
        verifyNoMoreInteractions(isRelatedCacheMock);

        verifyNoInteractions(entityManagerMock);
    }

    @Test
    public void testGetRelation_emptyCache_relationExists() throws RelationServiceException {
        RelationId relationId = new RelationId(OWNER, PARTNER, RELATION_NAME);
        when(entityManagerMock.find(RelationEntity.class, relationId)).thenReturn(new RelationEntity(OWNER, PARTNER, RELATION_NAME));

        relationService.getRelation(OWNER, PARTNER, RELATION_NAME);

        verify(isRelatedCacheMock).get(CACHE_KEY);
        verify(isRelatedCacheMock).put(CACHE_KEY, new Relation(OWNER, PARTNER, RELATION_NAME));

        verify(entityManagerMock).find(RelationEntity.class, relationId);
    }

    @Test
    public void testGetRelation_emptyCache_relationNotFound() throws RelationServiceException {
        RelationId relationId = new RelationId(OWNER, PARTNER, RELATION_NAME);
        when(entityManagerMock.find(RelationEntity.class, relationId)).thenReturn(null);

        try {
            relationService.getRelation(OWNER, PARTNER, RELATION_NAME);
            fail();
        } catch (RelationNotFoundException e) {
        }

        verify(isRelatedCacheMock).get(CACHE_KEY);
        verify(isRelatedCacheMock).put(CACHE_KEY, NULL_RELATION);

        verify(entityManagerMock).find(RelationEntity.class, relationId);
    }

    @Test
    public void testGetRelation_filledCache() throws RelationServiceException {
        when(isRelatedCacheMock.get(CACHE_KEY)).thenReturn(CACHED_RELATION);

        Relation relation = relationService.getRelation(OWNER, PARTNER, RELATION_NAME);

        assertThat(relation, is(CACHED_RELATION));

        verify(isRelatedCacheMock).get(CACHE_KEY);
        verifyNoMoreInteractions(isRelatedCacheMock);

        verifyNoInteractions(entityManagerMock);
    }

    @Test
    public void testGetRelation_nullCache() throws RelationServiceException {
        when(isRelatedCacheMock.get(CACHE_KEY)).thenReturn(NULL_RELATION);

        try {
            relationService.getRelation(OWNER, PARTNER, RELATION_NAME);
            fail();
        } catch (RelationNotFoundException e) {
        }

        verify(isRelatedCacheMock).get(CACHE_KEY);
        verifyNoMoreInteractions(isRelatedCacheMock);

        verifyNoInteractions(entityManagerMock);
    }
}
