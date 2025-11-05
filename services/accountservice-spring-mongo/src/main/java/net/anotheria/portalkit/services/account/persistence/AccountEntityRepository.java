package net.anotheria.portalkit.services.account.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountEntityRepository extends MongoRepository<AccountEntity, String> , AccountEntityRepositoryCustom{
    @Query(value = "{ 'name' : ?0 }", fields = "{ '_id' : 1 }")
    Optional<IDOnly> findIdByName(String name);

    @Query(value = "{ 'name' : ?0, 'brand': ?1 }", fields = "{ '_id' : 1 }")
    Optional<IDOnly> findIdByNameAndBrand(String name, String brand);

    @Query(value = "{ 'email' : ?0 }", fields = "{ '_id' : 1 }")
    Optional<IDOnly> findIdByEmail(String email);

    @Query(value = "{ 'email' : ?0, 'brand': ?1 }", fields = "{ '_id' : 1 }")
    Optional<IDOnly> findIdByEmailAndBrand(String email, String brand);

    @Query(value = "{ 'brand' : ?0 }", fields = "{ '_id' : 1 }")
    List<String> findAllIdsByBrand(String brand);

    @Query(value ="{}", fields = "{ '_id' : 1 }")
    List<String> findAllIds();

    @Query(value = "{ 'type' : ?0 }", fields = "{ '_id' : 1 }")
    List<String> findAllIdsByType(int type);

}
