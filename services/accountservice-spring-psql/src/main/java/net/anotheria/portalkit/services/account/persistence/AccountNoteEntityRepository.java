package net.anotheria.portalkit.services.account.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountNoteEntityRepository extends JpaRepository<AccountNoteEntity, Long> {
    /**
     * Returns all notes for an account.
     * @param accountId internal account id.
     * @return list of notes.
     */
    List<AccountNoteEntity> findAllByAccountId(String accountId);
}
