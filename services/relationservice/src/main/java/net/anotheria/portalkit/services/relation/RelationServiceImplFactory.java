package net.anotheria.portalkit.services.relation;

import net.anotheria.anoprise.dualcrud.CrudService;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.moskito.core.dynamic.ProxyUtils;
import net.anotheria.portalkit.services.common.util.ServiceProxyUtil;

/**
 * @author asamoilich
 */
public class RelationServiceImplFactory implements ServiceFactory<RelationService> {
    @Override
    public RelationService create() {
        return ServiceProxyUtil.createServiceProxy(RelationService.class, new RelationServiceImpl(), "service", "portal-kit", true, CrudService.class);
    }
}