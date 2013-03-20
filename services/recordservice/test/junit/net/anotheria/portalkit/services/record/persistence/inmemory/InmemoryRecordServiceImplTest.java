package net.anotheria.portalkit.services.record.persistence.inmemory;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import net.anotheria.portalkit.services.record.AbstractRecordServiceTest;
import net.anotheria.portalkit.services.record.RecordService;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class InmemoryRecordServiceImplTest extends AbstractRecordServiceTest {

	@Before
	@After
	public void reset() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());
		try {
			setService(MetaFactory.get(RecordService.class));
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}
	}

}
