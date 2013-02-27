package net.anotheria.portalkit.services.record;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordServiceTest{
	@Test public void testSingleRecordReadWrite() throws MetaFactoryException, RecordServiceException{
		//Record rec = new Record();
		RecordService service = MetaFactory.get(RecordService.class);
		AccountId accId = AccountId.generateNew();

		//first check handling of not existing records.
		Record old = service.getRecord(accId, "profile", "age");
		assertNotNull("null is not expected, EmptyRecord expected instead", old);
		assertTrue(old.isEmpty());
		assertEquals("age", old.getRecordId());

		//now create a new record


		//now try to read the new record



	}
}