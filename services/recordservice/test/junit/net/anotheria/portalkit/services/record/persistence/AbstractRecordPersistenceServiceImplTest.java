package net.anotheria.portalkit.services.record.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.InMemoryPickerConflictResolver;
import net.anotheria.portalkit.services.record.IntRecord;
import net.anotheria.portalkit.services.record.Record;
import net.anotheria.portalkit.services.record.RecordCollection;
import net.anotheria.portalkit.services.record.RecordType;
import net.anotheria.portalkit.services.record.StringRecord;
import net.anotheria.portalkit.services.record.persistence.RecordPersistenceService;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author dagafonov
 * 
 */
public abstract class AbstractRecordPersistenceServiceImplTest {

	private RecordPersistenceService persistence;

	private static final String ACCOUNT_COLLECTION = "accountcoll";
	
	public void setPersistence(RecordPersistenceService persistence) {
		this.persistence = persistence;
	}
	
	@Test
	public void testGetEmptyCollection() throws Exception {

		AccountId accId = AccountId.generateNew();

		RecordCollection collection = persistence.getCollection(accId.getInternalId(), ACCOUNT_COLLECTION);
		System.out.println("collection=" + collection);
		assertNotNull(collection);
		assertNotNull(collection.getRecord("age"));

		Record record = persistence.getSingleRecord(accId.getInternalId(), ACCOUNT_COLLECTION, "age");
		System.out.println("record=" + record);
		assertNotNull(record);

	}

	@Test
	public void testUpdateCollection() throws Exception {
		AccountId accId = AccountId.generateNew();

		RecordCollection coll = new RecordCollection(ACCOUNT_COLLECTION);
		coll.setRecord(new IntRecord("age", 34));
		coll.setRecord(new StringRecord("firstname", "Vasyl"));
		coll.setRecord(new StringRecord("lastname", "Pupkin"));
		coll.setRecord(new StringRecord("city", "Melitopol"));

		persistence.updateCollection(accId.getInternalId(), ACCOUNT_COLLECTION, coll);

		coll = persistence.getCollection(accId.getInternalId(), ACCOUNT_COLLECTION);
		assertEquals(ACCOUNT_COLLECTION, coll.getCollectionId());
		assertEquals(4, coll.getRecordSet().getRecords().size());
		{
			Record r = coll.getRecord("age");
			assertNotNull(r);
			assertEquals(RecordType.INT, r.getType());
			assertEquals("34", r.getValueAsString());
		}
		{
			Record r = coll.getRecord("firstname");
			assertNotNull(r);
			assertEquals(RecordType.STRING, r.getType());
			assertEquals("Vasyl", r.getValueAsString());
		}
		{
			Record r = coll.getRecord("lastname");
			assertNotNull(r);
			assertEquals(RecordType.STRING, r.getType());
			assertEquals("Pupkin", r.getValueAsString());
		}
		{
			Record r = coll.getRecord("city");
			assertNotNull(r);
			assertEquals(RecordType.STRING, r.getType());
			assertEquals("Melitopol", r.getValueAsString());
		}
	}
	
	@Test
	public void testUpdateSingleRecord() throws Exception {
		AccountId accId = AccountId.generateNew();
		Record city = new StringRecord("city", "Moscow");
		
		persistence.updateSingleRecord(accId.getInternalId(), ACCOUNT_COLLECTION, city);
		
		RecordCollection rc = persistence.getCollection(accId.getInternalId(), ACCOUNT_COLLECTION);
		assertEquals(1, rc.getRecordSet().getRecords().size());
		Record r = rc.getRecord("city");
		assertNotNull(r);
		assertTrue(!r.isEmpty());
		assertEquals("Moscow", r.getValueAsString());
		
	}
	
	@Test
	public void testUpdateSingleRecord2() throws Exception {
		AccountId accId = AccountId.generateNew();
		Record city = new StringRecord("city2", "Kiev");
		
		persistence.updateSingleRecord(accId.getInternalId(), ACCOUNT_COLLECTION, city);
		
		Record r = persistence.getSingleRecord(accId.getInternalId(), ACCOUNT_COLLECTION, "city2");
		assertNotNull(r);
		assertTrue(!r.isEmpty());
		assertEquals("Kiev", r.getValueAsString());
		
	}
	
	@Test
	public void testGetSingleRecord() throws Exception {
		AccountId accId = AccountId.generateNew();
		
		Record r = persistence.getSingleRecord(accId.getInternalId(), ACCOUNT_COLLECTION, "city");
		assertNotNull(r);
		assertTrue(r.isEmpty());
		
		Record city = new StringRecord("city", "New York");
		persistence.updateSingleRecord(accId.getInternalId(), ACCOUNT_COLLECTION, city);
		
		r = persistence.getSingleRecord(accId.getInternalId(), ACCOUNT_COLLECTION, "city");
		
		assertNotNull(r);
		assertTrue(!r.isEmpty());
		assertEquals("New York", r.getValueAsString());
	}

}
