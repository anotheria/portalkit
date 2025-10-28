package net.anotheria.portalkit.services.account.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountAuditEntityRepository extends MongoRepository<AccountAuditEntity, String> {
    List<AccountAuditEntity> findAllByAccountId(String accountId);

}
