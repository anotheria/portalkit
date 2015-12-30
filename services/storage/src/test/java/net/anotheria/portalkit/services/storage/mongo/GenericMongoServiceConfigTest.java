package net.anotheria.portalkit.services.storage.mongo;

import java.util.List;

import net.anotheria.portalkit.services.storage.mongo.index.Index;
import net.anotheria.portalkit.services.storage.mongo.index.IndexField;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link GenericMongoServiceConfig} test.
 * 
 * @author Alexandr Bolbat
 */
public class GenericMongoServiceConfigTest {

	/**
	 * Test general configuration functionality.
	 */
	@Test
	public void testConfiguration() {
		GenericMongoServiceConfig configuration = GenericMongoServiceConfig.getInstance();

		// general validation
		Assert.assertNotNull(configuration);
		Assert.assertSame(configuration, GenericMongoServiceConfig.getInstance());

		// basic properties
		Assert.assertEquals("databaseName should be equal", "test_database", configuration.getDatabaseName());
		Assert.assertEquals("collectionName should be equal", "test_collection", configuration.getCollectionName());
		Assert.assertEquals("entityKeyFieldName should be equal", "id", configuration.getEntityKeyFieldName());

		// indexes part
		Assert.assertEquals("initializeIndexes should be true", true, configuration.isInitializeIndexes());
		List<Index> indexes = configuration.getIndexes();
		Assert.assertNotNull(indexes);
		Assert.assertEquals("two indexes should be configured", 2, indexes.size());

		// first index
		Index index = indexes.get(0);
		Assert.assertNotNull(index);
		Assert.assertEquals("index name should be equal", "testIndex", index.getName());
		Assert.assertEquals("index unique parameter should be equal", false, index.isUnique());
		Assert.assertEquals("index dropDups parameter should be equal", false, index.isDropDups());
		Assert.assertEquals("index sparse parameter should be equal", false, index.isSparse());
		Assert.assertEquals("index background parameter should be equal", true, index.isBackground());
		List<IndexField> indexFields = index.getFields();
		Assert.assertNotNull(indexFields);
		Assert.assertEquals("one field should be configured", 1, indexFields.size());
		IndexField indexField = indexFields.get(0);
		Assert.assertNotNull(indexField);
		Assert.assertEquals("field name should be equal", "firstName", indexField.getName());
		Assert.assertEquals("field order should be equal", 1, indexField.getOrder());
		Assert.assertEquals("field hashed parameter should be equal", true, indexField.isHashed());

		// second index
		index = indexes.get(1);
		Assert.assertNotNull(index);
		Assert.assertEquals("index name should be equal", "anotherIndex", index.getName());
		Assert.assertEquals("index unique parameter should be equal", true, index.isUnique());
		Assert.assertEquals("index dropDups parameter should be equal", true, index.isDropDups());
		Assert.assertEquals("index sparse parameter should be equal", true, index.isSparse());
		Assert.assertEquals("index background parameter should be equal", true, index.isBackground());
		indexFields = index.getFields();
		Assert.assertNotNull(indexFields);
		Assert.assertEquals("one field should be configured", 2, indexFields.size());
		indexField = indexFields.get(0); // first field
		Assert.assertNotNull(indexField);
		Assert.assertEquals("field name should be equal", "age", indexField.getName());
		Assert.assertEquals("field order should be equal", 1, indexField.getOrder());
		Assert.assertEquals("field hashed parameter should be equal", false, indexField.isHashed());
		indexField = indexFields.get(1); // second field
		Assert.assertNotNull(indexField);
		Assert.assertEquals("field name should be equal", "anotherField", indexField.getName());
		Assert.assertEquals("field order should be equal", -1, indexField.getOrder());
		Assert.assertEquals("field hashed parameter should be equal", false, indexField.isHashed());
	}

	/**
	 * Test default configuration.
	 */
	@Test
	public void testDefaultConfiguration() {
		GenericMongoServiceConfig configuration = GenericMongoServiceConfig.getInstance("not-exist-configuration", null);

		// general validation
		Assert.assertNotNull(configuration);
		Assert.assertNotSame(configuration, GenericMongoServiceConfig.getInstance());

		// basic properties
		Assert.assertEquals("databaseName should be equal", "", configuration.getDatabaseName());
		Assert.assertEquals("collectionName should be equal", "", configuration.getCollectionName());
		Assert.assertEquals("entityKeyFieldName should be equal", "", configuration.getEntityKeyFieldName());

		// indexes part
		Assert.assertEquals("initializeIndexes should be false", false, configuration.isInitializeIndexes());
		List<Index> indexes = configuration.getIndexes();
		Assert.assertNotNull(indexes);
		Assert.assertEquals("zero indexes should be configured", 0, indexes.size());
	}

}
