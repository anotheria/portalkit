package net.anotheria.portalkit.services.record.persistence.inmemory;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import net.anotheria.portalkit.services.record.RecordService;
import net.anotheria.portalkit.services.record.RecordServiceFactory;
import net.anotheria.portalkit.services.record.persistence.AbstractRecordPersistenceServiceImplTest;
import net.anotheria.portalkit.services.record.persistence.RecordPersistenceService;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Before;

/**
 * 
 * @author dagafonov
 * 
 */
public class InmemoryRecordPersistenceServiceImplTest extends AbstractRecordPersistenceServiceImplTest {

	@Before
	public void reset() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());
		MetaFactory.addFactoryClass(RecordService.class, Extension.LOCAL, RecordServiceFactory.class);
		MetaFactory.addAlias(RecordService.class, Extension.LOCAL);
		try {
			setPersistence(MetaFactory.get(RecordPersistenceService.class));
		} catch (MetaFactoryException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
