package net.anotheria.portalkit.services.approval.inmemory;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.approval.AbstractApprovalServiceImplTest;
import net.anotheria.portalkit.services.approval.ApprovalService;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Before;

/**
 * 
 * @author dagafonov
 *
 */
public class InMemoryApprovalServiceImplTest extends AbstractApprovalServiceImplTest {

	@Before
	public void reset() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new InMemoryPickerConflictResolver());
		try {
			setService(MetaFactory.get(ApprovalService.class));
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}
	}

}
