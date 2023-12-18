package net.anotheria.portalkit.services.storage.mongo;

import com.mongodb.*;
import com.mongodb.util.JSON;
import net.anotheria.moskito.aop.annotation.DontMonitor;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.storage.exception.EntityAlreadyExistStorageException;
import net.anotheria.portalkit.services.storage.exception.EntityNotFoundStorageException;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.exception.StorageRuntimeException;
import net.anotheria.portalkit.services.storage.mongo.index.Index;
import net.anotheria.portalkit.services.storage.mongo.index.IndexField;
import net.anotheria.portalkit.services.storage.mongo.util.MongoConstants;
import net.anotheria.portalkit.services.storage.mongo.util.MongoUtil;
import net.anotheria.portalkit.services.storage.query.LimitQuery;
import net.anotheria.portalkit.services.storage.query.OffsetQuery;
import net.anotheria.portalkit.services.storage.query.Query;
import net.anotheria.portalkit.services.storage.query.common.QueryUtils;
import net.anotheria.portalkit.services.storage.util.EntityUtils;
import net.anotheria.util.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link GenericMongoService} implementation.
 *
 * @author Alexandr Bolbat
 *
 * @param <T>
 */
@Monitor (subsystem = "portalkit")
public class GenericMongoServiceImpl<T extends Serializable> extends AbstractMongoService implements GenericMongoService<T> {

	/**
	 * {@link Logger} instance.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(GenericMongoServiceImpl.class);

	/**
	 * {@link GenericMongoServiceConfig} instance.
	 */
	private final GenericMongoServiceConfig configuration;

	/**
	 * Entity class.
	 */
	private final Class<T> entityClass;

	/**
	 * Default constructor.
	 *
	 * @param aEntityClass
	 *            entity class
	 */
	public GenericMongoServiceImpl(final Class<T> aEntityClass) {
		this(aEntityClass, null, null, null, null);
	}

	/**
	 * Public constructor.
	 *
	 * @param aEntityClass
	 *            entity class
	 * @param conf
	 *            service configuration name, can be <code>null</code>
	 * @param serviceConf
	 *            abstract service configuration name, can be <code>null</code>
	 * @param clientConf
	 *            mongo client configuration name, can be <code>null</code>
	 * @param env
	 *            configuration environment, can be <code>null</code>
	 */
	public GenericMongoServiceImpl(final Class<T> aEntityClass, final String conf, final String serviceConf, final String clientConf, final String env) {
		super(serviceConf, clientConf, env);

		if (aEntityClass == null)
			throw new IllegalArgumentException("aEntityClass argument is null.");

		this.entityClass = aEntityClass;
		this.configuration = GenericMongoServiceConfig.getInstance(conf, env);

		// initialization done there (not in super constructor)
		// because initialization use getDBName() method (custom for each implementation)
		// current implementation takes database name from configuration
		// and it should be initialized before general initialization, but after calling super constructor
		initialize();

		// indexes initialization
		initializeIndexes();

		// validating configured entity key field
		final String entityKeyFieldName = configuration.getEntityKeyFieldName();
		if (entityKeyFieldName == null || entityKeyFieldName.trim().isEmpty())
			throw new StorageRuntimeException("Wrong key field[" + entityKeyFieldName + "] configured.");

		if (!EntityUtils.isFieldExist(entityClass, entityKeyFieldName))
			throw new StorageRuntimeException("Wrong key field[" + entityKeyFieldName + "] configured.");
	}

	/**
	 * Indexes initialization.
	 */
	private void initializeIndexes() {
		if (!configuration.isInitializeIndexes())
			return;

		final List<Index> indexes = configuration.getIndexes();
		if (indexes == null || indexes.isEmpty()) {
			LOGGER.warn("Indexes configuration is empty. Skipping.");
			return;
		}

		for (final Index index : indexes) {
			if (index == null) {
				LOGGER.warn("Index[" + null + "] configuration is wrong. Skipping.");
				continue;
			}
			if (index.getFields() == null || index.getFields().isEmpty()) {
				LOGGER.warn("Index[" + index + "] configuration is wrong. No configured fields. Skipping.");
				continue;
			}

			final BasicDBObject fields = new BasicDBObject();
			for (final IndexField field : index.getFields()) {
				if (field == null) {
					LOGGER.warn("Index[" + index + "] field[" + null + "] configuration is wrong. Skipping.");
					continue;
				}
				if (StringUtils.isEmpty(field.getName())) {
					LOGGER.warn("Index[" + index + "] field[" + field + "] configuration is wrong. Empty field name. Skipping.");
					continue;
				}

				final String name = field.getName();
				final int order = field.getOrder();
				final boolean hashed = field.isHashed();
				fields.put(name, hashed ? IndexField.MONGO_INDEX_FIELD_PROPERTY_HASHED : order);
			}
			final BasicDBObject options = new BasicDBObject();
			if (!StringUtils.isEmpty(index.getName())) // index name
				options.put(Index.MONGO_INDEX_PROPERTY_NAME, index.getName());

			options.put(Index.MONGO_INDEX_PROPERTY_UNIQUE, index.isUnique()); // unique constraint
			options.put(Index.MONGO_INDEX_PROPERTY_DROPDUPS, index.isDropDups()); // drop duplicates on creation, should be used very carefully
			options.put(Index.MONGO_INDEX_PROPERTY_SPARSE, index.isSparse());
			options.put(Index.MONGO_INDEX_PROPERTY_BACKGROUND, index.isBackground());

			getCollection().createIndex(fields, options);
		}
	}

	@Override
	@DontMonitor
	protected String getDBName() {
		return configuration.getDatabaseName();
	}

	@DontMonitor
	private DBCollection getCollection() {
		return getMongoClient().getDB(getDBName()).getCollection(configuration.getCollectionName());
	}

	@Override
	public T read(final String uid) throws StorageException {
		if (uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("uid argument is empty.");

		DBObject obj;
		try {
			obj = getCollection().findOne(MongoUtil.queryGetEntity(uid));
		} catch (final MongoException e) {
			throw new StorageException("Can't read entity[" + uid + "].", e);
		}

		if (obj == null)
			throw new EntityNotFoundStorageException(uid);

		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
			return objectMapper.readValue(obj.toString(), entityClass);
		} catch (final JsonParseException e) {
			throw new StorageException("Can't parse entity[" + obj + "].", e);
		} catch (final JsonMappingException e) {
			throw new StorageException("Can't map entity[" + obj + "].", e);
		} catch (final IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public T save(final T toSave) throws StorageException {
		if (toSave == null)
			throw new IllegalArgumentException("toSave argument is null.");

		final String uid = EntityUtils.getFieldValue(toSave, configuration.getEntityKeyFieldName());
		try {
			final DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toSave)));
			if (!MongoConstants.FIELD_ID_NAME.equals(configuration.getEntityKeyFieldName()))
				entity.put(MongoConstants.FIELD_ID_NAME, uid);
			getCollection().save(entity);
		} catch (final JsonGenerationException e) {
			throw new StorageException("Can't generate entity[" + toSave + "].", e);
		} catch (final JsonMappingException e) {
			throw new StorageException("Can't map entity[" + toSave + "].", e);
		} catch (final IOException e) {
			throw new StorageException(e);
		} catch (final MongoException e) {
			throw new StorageException("Can't save entity[" + toSave + "].", e);
		}

		return read(uid);
	}

	@Override
	public T create(final T toCreate) throws StorageException {
		if (toCreate == null)
			throw new IllegalArgumentException("toCreate argument is null.");

		final String uid = EntityUtils.getFieldValue(toCreate, configuration.getEntityKeyFieldName());
		try {
			final T entity = read(uid);
			if (entity != null)
				throw new EntityAlreadyExistStorageException(uid);
		} catch (final EntityNotFoundStorageException e) {
			if (LOGGER.isTraceEnabled())
				LOGGER.trace("create(" + toCreate + ") expected exception. Message[" + e.getMessage() + "].");
		}

		try {
			final DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toCreate)));
			if (!MongoConstants.FIELD_ID_NAME.equals(configuration.getEntityKeyFieldName()))
				entity.put(MongoConstants.FIELD_ID_NAME, uid);
			getCollection().insert(entity);
		} catch (final JsonGenerationException e) {
			throw new StorageException("Can't generate entity[" + toCreate + "].", e);
		} catch (final JsonMappingException e) {
			throw new StorageException("Can't map entity[" + toCreate + "].", e);
		} catch (final IOException e) {
			throw new StorageException(e);
		} catch (final MongoException e) {
			throw new StorageException("Can't create entity[" + toCreate + "].", e);
		}

		return read(uid);
	}

	@Override
	public T update(final T toUpdate) throws StorageException {
		if (toUpdate == null)
			throw new IllegalArgumentException("toUpdate argument is null.");

		// checking is entity exist
		final String uid = EntityUtils.getFieldValue(toUpdate, configuration.getEntityKeyFieldName());
		read(uid);

		// performing entity update
		try {
			final DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toUpdate)));
			if (!MongoConstants.FIELD_ID_NAME.equals(configuration.getEntityKeyFieldName()))
				entity.put(MongoConstants.FIELD_ID_NAME, uid);
			getCollection().update(MongoUtil.queryGetEntity(uid), entity);
		} catch (final JsonGenerationException e) {
			throw new StorageException("Can't generate entity[" + toUpdate + "].", e);
		} catch (final JsonMappingException e) {
			throw new StorageException("Can't map entity[" + toUpdate + "].", e);
		} catch (final IOException e) {
			throw new StorageException(e);
		} catch (final MongoException e) {
			throw new StorageException("Can't create entity[" + toUpdate + "].", e);
		}

		// reading updated entity
		return read(EntityUtils.getFieldValue(toUpdate, configuration.getEntityKeyFieldName()));
	}

	@Override
	public T delete(final String uid) throws StorageException {
		final T result = read(uid);
		try {
			getCollection().remove(MongoUtil.queryGetEntity(uid));
		} catch (final MongoException e) {
			throw new StorageException("Can't delete entity[" + uid + "].", e);
		}
		return result;
	}

	@Override
	public List<T> read(final List<String> uidList) throws StorageException {
		final List<T> result = new ArrayList<T>();
		if (uidList == null || uidList.isEmpty())
			return result;

		for (final String uid : uidList)
			try {
				result.add(read(uid));
			} catch (final EntityNotFoundStorageException e) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("read(" + uid + ") skipped[" + e.getMessage() + "].");
			} catch (final StorageException e) {
				LOGGER.warn("read(" + uid + ") fail. Skipping.", e);
			}

		return result;
	}

	@Override
	public List<T> save(final List<T> toSaveList) throws StorageException {
		final List<T> result = new ArrayList<T>();
		if (toSaveList == null || toSaveList.isEmpty())
			return result;

		for (final T entity : toSaveList)
			try {
				result.add(save(entity));
			} catch (final StorageException e) {
				LOGGER.warn("save(" + entity + ") fail. Skipping.", e);
			}

		return result;
	}

	@Override
	public List<T> create(final List<T> toCreateList) throws StorageException {
		final List<T> result = new ArrayList<T>();
		if (toCreateList == null || toCreateList.isEmpty())
			return result;

		for (final T entity : toCreateList)
			try {
				result.add(create(entity));
			} catch (final EntityAlreadyExistStorageException e) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("create(" + entity + ") skipped[" + e.getMessage() + "].");
			} catch (final StorageException e) {
				LOGGER.warn("create(" + entity + ") fail. Skipping.", e);
			}

		return result;
	}

	@Override
	public List<T> update(final List<T> toUpdateList) throws StorageException {
		final List<T> result = new ArrayList<T>();
		if (toUpdateList == null || toUpdateList.isEmpty())
			return result;

		for (final T entity : toUpdateList)
			try {
				result.add(update(entity));
			} catch (final EntityNotFoundStorageException e) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("update(" + entity + ") skipped[" + e.getMessage() + "].");
			} catch (final StorageException e) {
				LOGGER.warn("update(" + entity + ") fail. Skipping.", e);
			}

		return result;
	}

	@Override
	public List<T> delete(final List<String> uidList) throws StorageException {
		final List<T> result = new ArrayList<T>();
		if (uidList == null || uidList.isEmpty())
			return result;

		for (final String uid : uidList)
			try {
				result.add(delete(uid));
			} catch (final EntityNotFoundStorageException e) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("delete(" + uid + ") skipped[" + e.getMessage() + "].");
			} catch (final StorageException e) {
				LOGGER.warn("delete(" + uid + ") fail. Skipping.", e);
			}

		return result;
	}

	@Override
	public List<T> findAll() throws StorageException {
		final List<T> result = new ArrayList<T>();

		DBCursor rawResult = null;
		try {
			rawResult = getCollection().find();

			final ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

			while (rawResult.hasNext()) {
				final DBObject obj = rawResult.next();
				try {
					result.add(objectMapper.readValue(obj.toString(), entityClass));
				} catch (final JsonParseException e) {
					throw new StorageException("Can't parse entity[" + obj + "].", e);
				} catch (final JsonMappingException e) {
					throw new StorageException("Can't map entity[" + obj + "].", e);
				} catch (final IOException e) {
					throw new StorageException(e);
				}
			}

			return result;
		} catch (final MongoException e) {
			throw new StorageException("Can't exequte query: find all entities.", e);
		} finally {
			if (rawResult != null)
				rawResult.close();
		}
	}

	@Override
	public long countAll() throws StorageException {
		try {
			return getCollection().count();
		} catch (final MongoException e) {
			throw new StorageException("Can't execute query: count all entities.", e);
			}
	}

	@Override
	public List<T> find(final Query query) throws StorageException {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		final List<T> result = new ArrayList<T>();

		final BasicDBObject mongoQuery = MongoQueryMapper.map(query);
		if (mongoQuery == null)
			return result;

		DBCursor rawResult = null;
		try {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("find(" + query + ") executing with mongo query[" + mongoQuery + "].");

			rawResult = getCollection().find(mongoQuery);

			final BasicDBObject sorting = MongoQueryMapper.getSorting(query);
			if (sorting != null)
				rawResult.sort(sorting);

			final OffsetQuery offset = QueryUtils.getOffset(query);
			if (offset != null)
				rawResult.skip(offset.getQueryValue().getValue());

			final LimitQuery limit = QueryUtils.getLimit(query);
			if (limit != null)
				rawResult.limit(limit.getQueryValue().getValue());

			// processing results
			final ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

			while (rawResult.hasNext()) {
				final DBObject obj = rawResult.next();
				try {
					result.add(objectMapper.readValue(obj.toString(), entityClass));
				} catch (final JsonParseException e) {
					throw new StorageException("Can't parse entity[" + obj + "].", e);
				} catch (final JsonMappingException e) {
					throw new StorageException("Can't map entity[" + obj + "].", e);
				} catch (final IOException e) {
					throw new StorageException(e);
				}
			}

			return result;
		} catch (final MongoException e) {
			throw new StorageException("Can't exequte query[" + query + "].", e);
		} finally {
			if (rawResult != null)
				rawResult.close();
		}
	}

	@Override
	public void delete(Query query) throws StorageException {

		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		final BasicDBObject mongoQuery = MongoQueryMapper.map(query);

		if (mongoQuery == null)
			return;

		DBCursor rawResult = null;

		try {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("find(" + query + ") executing with mongo query[" + mongoQuery + "].");

			rawResult = getCollection().find(mongoQuery);

			while (rawResult.hasNext()) {
				final DBObject obj = rawResult.next();

				delete((String) obj.get(MongoConstants.FIELD_ID_NAME));
			}
		} catch (final MongoException e) {
			throw new StorageException("Can't exequte query[" + query + "].", e);
		} finally {
			if (rawResult != null)
				rawResult.close();
		}
	}
}
