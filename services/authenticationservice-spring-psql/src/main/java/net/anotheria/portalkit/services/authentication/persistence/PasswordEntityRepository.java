package net.anotheria.portalkit.services.authentication.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for {@link PasswordEntity}.
 */
public interface PasswordEntityRepository extends JpaRepository<PasswordEntity, String> {
    /**
     * Find all account ids.
     *
     * @return list of ids in persistence
     */
    @Query("select p.accountId from PasswordEntity p")
    List<String> findAllIds();
}
