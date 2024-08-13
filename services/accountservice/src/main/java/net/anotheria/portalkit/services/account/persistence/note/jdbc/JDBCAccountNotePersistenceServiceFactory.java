package net.anotheria.portalkit.services.account.persistence.note.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.account.persistence.note.AccountNotePersistenceService;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

public class JDBCAccountNotePersistenceServiceFactory implements ServiceFactory<AccountNotePersistenceService> {

    @Override
    public AccountNotePersistenceService create() {
        return ServiceProxyUtil.createServiceProxy(AccountNotePersistenceService.class, new JDBCAccountNotePersistenceServiceImpl(), "service",
                "portal-kit-persistence", true, BasePersistenceService.class);
    }
}
