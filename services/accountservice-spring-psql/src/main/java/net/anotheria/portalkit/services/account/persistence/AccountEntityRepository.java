package net.anotheria.portalkit.services.account.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link AccountEntity}. Id-lookup methods return the {@link IDOnly}
 * projection to mirror the mongo variant's contract used by {@code AccountServiceImpl}.
 */
public interface AccountEntityRepository extends JpaRepository<AccountEntity, String>, AccountEntityRepositoryCustom {

    @Query("select a.id as id from AccountEntity a where a.name = :name")
    Optional<IDOnly> findIdByName(@Param("name") String name);

    @Query("select a.id as id from AccountEntity a where a.name = :name and a.brand = :brand")
    Optional<IDOnly> findIdByNameAndBrand(@Param("name") String name, @Param("brand") String brand);

    @Query("select a.id as id from AccountEntity a where a.email = :email")
    Optional<IDOnly> findIdByEmail(@Param("email") String email);

    @Query("select a.id as id from AccountEntity a where a.email = :email and a.brand = :brand")
    Optional<IDOnly> findIdByEmailAndBrand(@Param("email") String email, @Param("brand") String brand);

    @Query("select a.id as id from AccountEntity a where a.brand = :brand")
    List<IDOnly> findAllIdsByBrand(@Param("brand") String brand);

    @Query("select a.id as id from AccountEntity a")
    List<IDOnly> findAllIds();

    @Query("select a.id as id from AccountEntity a where a.type = :type")
    List<IDOnly> findAllIdsByType(@Param("type") int type);
}
