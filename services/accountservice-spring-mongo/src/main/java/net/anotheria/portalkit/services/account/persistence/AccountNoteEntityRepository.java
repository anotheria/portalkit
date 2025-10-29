package net.anotheria.portalkit.services.account.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountNoteEntityRepository extends MongoRepository<AccountNoteEntity, String> {
    /**
     * Returns all notes for an account.
     * @param accountId
     * @return
     */
    List<AccountNoteEntity> findAllByAccountId(String accountId);
}
