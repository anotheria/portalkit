package net.anotheria.portalkit.services.account.persistence.audit.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.account.persistence.audit.AccountAuditPersistenceService;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * {@link AccountAuditPersistenceService} factory implementation JDBC implementation.
 *
 * @author ykalapusha
 */
public class JDBCAccountAuditPersistenceServiceFactory implements ServiceFactory<AccountAuditPersistenceService> {

    @Override
    public AccountAuditPersistenceService create() {
        return ServiceProxyUtil.createServiceProxy(AccountAuditPersistenceService.class, new JDBCAccountAuditPersistenceServiceImpl(), "service",
                "portal-kit-persistence", true, BasePersistenceService.class);
    }
}
