package net.anotheria.portalkit.services.account.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountAuditEntityRepository extends JpaRepository<AccountAuditEntity, Long> {
    List<AccountAuditEntity> findAllByAccountId(String accountId);
}
