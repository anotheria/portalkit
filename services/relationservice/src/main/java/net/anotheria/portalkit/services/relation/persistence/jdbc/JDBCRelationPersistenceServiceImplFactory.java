package net.anotheria.portalkit.services.relation.persistence.jdbc;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceService;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;
import net.anotheria.portalkit.services.relation.persistence.RelationPersistenceService;

/**
 * JDBC persistence based factory for {@link RelationPersistenceService}.
 *
 * @author asamoilich
 */
public class JDBCRelationPersistenceServiceImplFactory implements ServiceFactory<RelationPersistenceService> {
    @Override
    public RelationPersistenceService create() {
        return ServiceProxyUtil.createServiceProxy(RelationPersistenceService.class, new JDBCRelationPersistenceServiceImpl(), "service",
                "portal-kit-persistence", true, BasePersistenceService.class);
    }
}
