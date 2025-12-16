import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import net.anotheria.portalkit.services.foreignid.ForeignIdServiceImpl;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdEntity;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdEntityId;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForeignIdServiceTest {

    private ForeignIdEntityRepository repository;
    private ForeignIdServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(ForeignIdEntityRepository.class);
        service = new ForeignIdServiceImpl(repository);
    }

    @Test
    void testGetEntityCount() {
        when(repository.count()).thenReturn(5L);
        assertEquals(5, service.getEntityCount("any"));
    }

    @Test
    void testAddForeignId() throws Exception {
        AccountId accId = new AccountId("acc1");
        String foreignId = "fid";
        int sourceId = 1;
        ForeignIdEntityId id = new ForeignIdEntityId(foreignId, sourceId);

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.addForeignId(accId, foreignId, sourceId);

        ArgumentCaptor<ForeignIdEntity> captor = ArgumentCaptor.forClass(ForeignIdEntity.class);
        verify(repository).save(captor.capture());
        assertEquals("acc1", captor.getValue().getAccountId());
    }

    @Test
    void testRemoveForeignId() throws Exception {
        AccountId accId = new AccountId("acc1");
        String foreignId = "fid";
        int sourceId = 1;
        ForeignIdEntityId id = new ForeignIdEntityId(foreignId, sourceId);

        service.removeForeignId(accId, foreignId, sourceId);
        verify(repository).deleteById(id);
    }

    @Test
    void testGetAccountIdByForeignId() throws Exception {
        String foreignId = "fid";
        int sourceId = 1;
        ForeignIdEntityId id = new ForeignIdEntityId(foreignId, sourceId);
        ForeignIdEntity entity = new ForeignIdEntity();
        entity.setId(id);
        entity.setAccountId("acc1");

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        AccountId result = service.getAccountIdByForeignId(foreignId, sourceId);
        assertNotNull(result);
        assertEquals("acc1", result.getInternalId());
    }

    @Test
    void testGetForeignIds() throws Exception {
        AccountId accId = new AccountId("acc1");
        ForeignIdEntity entity = new ForeignIdEntity();
        entity.setId(new ForeignIdEntityId("fid", 1));
        entity.setAccountId("acc1");

        when(repository.findByAccountId("acc1")).thenReturn(List.of(entity));

        List<ForeignId> result = service.getForeignIds(accId);
        assertEquals(1, result.size());
        assertEquals("fid", result.get(0).getId());
    }

    @Test
    void testDeleteUserData() {
        AccountId accId = new AccountId("acc1");
        service.deleteUserData(accId);
        verify(repository).removeByAccountId("acc1");
    }
}