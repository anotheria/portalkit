package net.anotheria.portalkit.services.record.persistence.jdbc;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.persistence.JDBCPickerConflictResolver;
import net.anotheria.portalkit.services.record.AbstractRecordServiceTest;
import net.anotheria.portalkit.services.record.RecordService;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Before;

public class JDBCRecordServiceImplTest extends AbstractRecordServiceTest {

	@Before
	public void reset() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new JDBCPickerConflictResolver());
		try {
			setService(MetaFactory.get(RecordService.class));
		} catch (MetaFactoryException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
