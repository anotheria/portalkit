package net.anotheria.portalkit.services.pushtoken.persistence;

import net.anotheria.portalkit.services.pushtoken.persistence.PushTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushTokenRepository extends JpaRepository<PushTokenEntity, PushTokenEntity.PushTokenEntityPK> {

    List<PushTokenEntity> findAllById_AccountId(String accountId);

    PushTokenEntity getById_Token(String token);

}
