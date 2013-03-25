package net.anotheria.portalkit.services.foreignid;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link ForeignIdService} factory for main implementation.
 * 
 * @author dagafonov
 * 
 */
public class ForeignIdServiceFactory implements ServiceFactory<ForeignIdService> {
	@Override
	public ForeignIdService create() {
		return new ForeignIdServiceImpl();
	}
}
