package net.anotheria.portalkit.services.accountsettings;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.accountsettings.attribute.BooleanAttribute;
import net.anotheria.portalkit.services.accountsettings.attribute.IntAttribute;
import net.anotheria.portalkit.services.accountsettings.attribute.LongAttribute;
import net.anotheria.portalkit.services.accountsettings.attribute.StringAttribute;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceService;
import net.anotheria.portalkit.services.accountsettings.persistence.AccountSettingsPersistenceServiceFactory;
import net.anotheria.portalkit.services.common.AccountId;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author dagafonov
 * 
 */
@Ignore
public class AccountSettingsImplTest {

	/**
	 * 
	 */
	private AccountSettingsService service;

	/**
	 * 
	 */
	public AccountSettingsImplTest() {

		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test"));
		System.setProperty("JUNITTEST", "true");

		MetaFactory.reset();

		MetaFactory.addFactoryClass(AccountSettingsPersistenceService.class, Extension.PERSISTENCE, AccountSettingsPersistenceServiceFactory.class);
		MetaFactory.addAlias(AccountSettingsPersistenceService.class, Extension.PERSISTENCE);

		service = new AccountSettingsServiceImpl();
	}

	@Test
	public void testSave() throws AccountSettingsServiceException {

		AccountId accId = AccountId.generateNew();
		int dataspaceId = 1;

		Dataspace ds = new Dataspace(accId.getInternalId(), dataspaceId);
		ds.addAttribute("firstName", new StringAttribute("fn", "Dmytro"));
		ds.addAttribute("lastName", new StringAttribute("ln", "Agafonov"));
		ds.addAttribute("age", new IntAttribute("age", 23));
		ds.addAttribute("timestamp", new LongAttribute("time", System.currentTimeMillis()));
		ds.addAttribute("isMaried", new BooleanAttribute("asdf", Boolean.FALSE));

		service.saveDataspace(ds);

		ds = service.getDataspace(accId, dataspaceId);
	}

}
