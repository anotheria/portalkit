package net.anotheria.portalkit.services.record;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * {@link RecordService} factory for main implementation.
 * 
 * @author dagafonov
 * 
 */
public class RecordServiceFactory implements ServiceFactory<RecordService> {

	@Override
	public RecordService create() {
		return new RecordServiceImpl();
	}

}
