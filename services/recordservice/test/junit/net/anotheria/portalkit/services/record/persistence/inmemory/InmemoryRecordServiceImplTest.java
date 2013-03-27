package net.anotheria.portalkit.services.record.persistence.inmemory;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import net.anotheria.portalkit.services.record.AbstractRecordServiceTest;
import net.anotheria.portalkit.services.record.RecordService;
import net.anotheria.portalkit.services.record.RecordServiceFactory;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;

/**
 * InMemory implementation of {@link AbstractRecordServiceTest}.
 * @author dagafonov
 *
 */
public class InmemoryRecordServiceImplTest extends AbstractRecordServiceTest {

	@Before
	@After
	public void reset() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());
		MetaFactory.addFactoryClass(RecordService.class, Extension.LOCAL, RecordServiceFactory.class);
		MetaFactory.addAlias(RecordService.class, Extension.LOCAL);
		try {
			setService(MetaFactory.get(RecordService.class));
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}
	}

}
