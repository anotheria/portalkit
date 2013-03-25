package net.anotheria.portalkit.services.approval.jdbc;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.approval.AbstractApprovalServiceImplTest;
import net.anotheria.portalkit.services.approval.ApprovalService;
import net.anotheria.portalkit.services.approval.ApprovalServiceFactory;
import net.anotheria.portalkit.services.common.persistence.JDBCPickerConflictResolver;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Before;

/**
 * 
 * @author dagafonov
 * 
 */
public class JDBCApprovalServiceImplTest extends AbstractApprovalServiceImplTest {

	@Before
	public void before() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "h2"));
		MetaFactory.reset();
		MetaFactory.addOnTheFlyConflictResolver(new JDBCPickerConflictResolver());
		
		MetaFactory.addFactoryClass(ApprovalService.class, Extension.LOCAL, ApprovalServiceFactory.class);
		MetaFactory.addAlias(ApprovalService.class, Extension.LOCAL);
		
		try {
			setService(MetaFactory.get(ApprovalService.class));
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}
	}

}
