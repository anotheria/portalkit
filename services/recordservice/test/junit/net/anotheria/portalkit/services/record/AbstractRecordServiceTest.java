package net.anotheria.portalkit.services.record;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;

import org.junit.Test;

public abstract class AbstractRecordServiceTest {
	
	private RecordService service;

	public void setService(RecordService service) {
		this.service = service;
	}
	
	@Test
	public void testSingleRecordReadWrite() throws MetaFactoryException, RecordServiceException {
		// Record rec = new Record();
		AccountId accId = AccountId.generateNew();

		// first check handling of not existing records.
		Record old = service.getRecord(accId, "age");
		assertNotNull("null is not expected, EmptyRecord expected instead", old);
		assertTrue(old.isEmpty());
		assertEquals("age", old.getRecordId());

		// now create a new record
		Record rec = new IntRecord("age", 23);
		service.setRecord(accId, rec);

		// now try to read the new record
		Record rec2 = service.getRecord(accId, "age");
		assertNotNull("null is not expected, Record expected instead", rec2);
		assertFalse("expected the element no be empty " + rec2, rec2.isEmpty());
		assertEquals("age", rec2.getRecordId());
		assertEquals("23", rec2.getValueAsString());
		assertEquals(RecordType.INT, rec2.getType());
		assertEquals(23, (((IntRecord) rec2).getInt()));

	}

	@Test
	public void testRecordSetReadWrite() throws MetaFactoryException, RecordServiceException {

		AccountId accId = AccountId.generateNew();

		List<String> ids = Arrays.asList(new String[] { "firstname", "lastname", "city" });

		RecordSet oldSet = service.getRecordSet(accId, ids);
		assertNotNull(oldSet);
		for (String id : ids) {
			assertNotNull(oldSet.get(id));
			assertTrue(oldSet.get(id).isEmpty());
		}

		RecordSet set = new RecordSet();
		set.put(new StringRecord("firstname", "Ivan"));
		set.put(new StringRecord("lastname", "Petrov"));

		service.setRecords(accId, set);
		// try single save
		service.setRecord(accId, new StringRecord("city", "Kiev"));

		RecordSet fromService = service.getRecordSet(accId, ids);
		System.out.println(fromService);
		assertNotNull(fromService);
		for (String id : ids) {
			assertNotNull(fromService.get(id));
			assertFalse("expected to have a non empty element for id " + id, fromService.get(id).isEmpty());
		}

		assertEquals("Ivan", fromService.get("firstname").getValueAsString());
		assertEquals("Petrov", fromService.get("lastname").getValueAsString());
		assertEquals("Kiev", fromService.get("city").getValueAsString());
	}

}