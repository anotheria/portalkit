package net.anotheria.portalkit.services.accountarchive.persistence.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.accountarchive.persistence.AccountArchivePersistenceService;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * @author VKoulakov
 * @since 22.04.14 11:37
 */
public class JdbcAccountArchivePersistenceServiceFactory implements ServiceFactory<AccountArchivePersistenceService> {
    @Override
    public AccountArchivePersistenceService create() {
        return ServiceProxyUtil.createServiceProxy(AccountArchivePersistenceService.class, new JdbcAccountArchivePersistenceServiceImpl(), "service",
                "portal-kit-persistence", true, BasePersistenceService.class);
    }
}
