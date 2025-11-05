package net.anotheria.portalkit.services.authentication.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * {@link MongoRepository} for {@link PasswordEntity}.
 *
 * @author ykalapusha
 * @since 27.10.2025
 */
public interface PasswordEntityRepository extends MongoRepository<PasswordEntity, String> {
    /**
     * Find all ids.
     *
     * @return list of ids in persistence
     */
    @Query(value ="{}", fields = "{ '_id' : 1 }")
    List<String> findAllIds();
}
