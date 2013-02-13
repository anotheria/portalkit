package net.anotheria.portalkit.services.storage.mongo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.anotheria.portalkit.services.storage.exception.EntityAlreadyExistStorageException;
import net.anotheria.portalkit.services.storage.exception.EntityNotFoundStorageException;
import net.anotheria.portalkit.services.storage.exception.StorageException;
import net.anotheria.portalkit.services.storage.exception.StorageRuntimeException;
import net.anotheria.portalkit.services.storage.mongo.util.MongoConstants;
import net.anotheria.portalkit.services.storage.mongo.util.MongoUtil;
import net.anotheria.portalkit.services.storage.query.LimitQuery;
import net.anotheria.portalkit.services.storage.query.OffsetQuery;
import net.anotheria.portalkit.services.storage.query.Query;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 * {@link GenericMongoService} implementation.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
// TODO Right logging should be added later
public class GenericMongoServiceImpl<T extends Serializable> extends AbstractMongoService implements GenericMongoService<T> {

	/**
	 * {@link Logger} instance.
	 */
	protected static final Logger LOGGER = Logger.getLogger(GenericMongoServiceImpl.class);

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

		// validating configured entity key field
		String entityKeyFieldName = configuration.getEntityKeyFieldName();
		if (entityKeyFieldName == null || entityKeyFieldName.trim().isEmpty())
			throw new StorageRuntimeException("Wrong key field[" + entityKeyFieldName + "] configured.");

		try {
			entityClass.getDeclaredField(entityKeyFieldName);
		} catch (SecurityException e) {
			throw new StorageRuntimeException("Wrong key field[" + entityKeyFieldName + "] configured.");
		} catch (NoSuchFieldException e) {
			throw new StorageRuntimeException("Wrong key field[" + entityKeyFieldName + "] configured.");
		}
	}

	@Override
	protected String getDBName() {
		return configuration.getDatabaseName();
	}

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
		} catch (MongoException e) {
			throw new StorageException("Can't read entity[" + uid + "].", e);
		}

		if (obj == null)
			throw new EntityNotFoundStorageException(uid);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
			return objectMapper.readValue(obj.toString(), entityClass);
		} catch (JsonParseException e) {
			throw new StorageException("Can't parse entity[" + obj + "].", e);
		} catch (JsonMappingException e) {
			throw new StorageException("Can't map entity[" + obj + "].", e);
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public T save(final T toSave) throws StorageException {
		if (toSave == null)
			throw new IllegalArgumentException("toSave argument is null.");

		String uid = MongoUtil.getEntityUID(toSave, configuration);
		try {
			DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toSave)));
			if (!MongoConstants.FIELD_ID_NAME.equals(configuration.getEntityKeyFieldName()))
				entity.put(MongoConstants.FIELD_ID_NAME, uid);
			getCollection().save(entity);
		} catch (JsonGenerationException e) {
			throw new StorageException("Can't generate entity[" + toSave + "].", e);
		} catch (JsonMappingException e) {
			throw new StorageException("Can't map entity[" + toSave + "].", e);
		} catch (IOException e) {
			throw new StorageException(e);
		} catch (MongoException e) {
			throw new StorageException("Can't save entity[" + toSave + "].", e);
		}

		return read(uid);
	}

	@Override
	public T create(final T toCreate) throws StorageException {
		if (toCreate == null)
			throw new IllegalArgumentException("toCreate argument is null.");

		String uid = MongoUtil.getEntityUID(toCreate, configuration);
		try {
			T entity = read(uid);
			if (entity != null)
				throw new EntityAlreadyExistStorageException(uid);
		} catch (EntityNotFoundStorageException e) {
			// ignored
		}

		try {
			DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toCreate)));
			if (!MongoConstants.FIELD_ID_NAME.equals(configuration.getEntityKeyFieldName()))
				entity.put(MongoConstants.FIELD_ID_NAME, uid);
			getCollection().insert(entity);
		} catch (JsonGenerationException e) {
			throw new StorageException("Can't generate entity[" + toCreate + "].", e);
		} catch (JsonMappingException e) {
			throw new StorageException("Can't map entity[" + toCreate + "].", e);
		} catch (IOException e) {
			throw new StorageException(e);
		} catch (MongoException e) {
			throw new StorageException("Can't create entity[" + toCreate + "].", e);
		}

		return read(uid);
	}

	@Override
	public T update(final T toUpdate) throws StorageException {
		if (toUpdate == null)
			throw new IllegalArgumentException("toUpdate argument is null.");

		// checking is entity exist
		String uid = MongoUtil.getEntityUID(toUpdate, configuration);
		read(uid);

		// performing entity update
		try {
			DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toUpdate)));
			if (!MongoConstants.FIELD_ID_NAME.equals(configuration.getEntityKeyFieldName()))
				entity.put(MongoConstants.FIELD_ID_NAME, uid);
			getCollection().update(MongoUtil.queryGetEntity(uid), entity);
		} catch (JsonGenerationException e) {
			throw new StorageException("Can't generate entity[" + toUpdate + "].", e);
		} catch (JsonMappingException e) {
			throw new StorageException("Can't map entity[" + toUpdate + "].", e);
		} catch (IOException e) {
			throw new StorageException(e);
		} catch (MongoException e) {
			throw new StorageException("Can't create entity[" + toUpdate + "].", e);
		}

		// reading updated entity
		return read(MongoUtil.getEntityUID(toUpdate, configuration));
	}

	@Override
	public T delete(final String uid) throws StorageException {
		T result = read(uid);
		try {
			getCollection().remove(MongoUtil.queryGetEntity(uid));
		} catch (MongoException e) {
			throw new StorageException("Can't delete entity[" + uid + "].", e);
		}
		return result;
	}

	@Override
	public List<T> read(final List<String> uidList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (uidList == null || uidList.isEmpty())
			return result;

		for (String uid : uidList)
			try {
				result.add(read(uid));
			} catch (StorageException e) {
				// ignoring entity
			}

		return result;
	}

	@Override
	public List<T> save(final List<T> toSaveList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (toSaveList == null || toSaveList.isEmpty())
			return result;

		for (T entity : toSaveList)
			try {
				result.add(save(entity));
			} catch (StorageException e) {
				// ignoring entity
			}

		return result;
	}

	@Override
	public List<T> create(final List<T> toCreateList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (toCreateList == null || toCreateList.isEmpty())
			return result;

		for (T entity : toCreateList)
			try {
				result.add(create(entity));
			} catch (StorageException e) {
				// ignoring entity
			}

		return result;
	}

	@Override
	public List<T> update(final List<T> toUpdateList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (toUpdateList == null || toUpdateList.isEmpty())
			return result;

		for (T entity : toUpdateList)
			try {
				result.add(update(entity));
			} catch (StorageException e) {
				// ignoring entity
			}

		return result;
	}

	@Override
	public List<T> delete(final List<String> uidList) throws StorageException {
		List<T> result = new ArrayList<T>();
		if (uidList == null || uidList.isEmpty())
			return result;

		for (String uid : uidList)
			try {
				result.add(delete(uid));
			} catch (StorageException e) {
				// ignoring entity
			}

		return result;
	}

	@Override
	public List<T> findAll() throws StorageException {
		List<T> result = new ArrayList<T>();

		DBCursor rawResult = null;
		try {
			rawResult = getCollection().find();

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

			while (rawResult.hasNext()) {
				DBObject obj = rawResult.next();
				try {
					result.add(objectMapper.readValue(obj.toString(), entityClass));
				} catch (JsonParseException e) {
					throw new StorageException("Can't parse entity[" + obj + "].", e);
				} catch (JsonMappingException e) {
					throw new StorageException("Can't map entity[" + obj + "].", e);
				} catch (IOException e) {
					throw new StorageException(e);
				}
			}

			return result;
		} catch (MongoException e) {
			throw new StorageException("Can't exequte query: find all entities.", e);
		} finally {
			if (rawResult != null)
				rawResult.close();
		}
	}

	@Override
	public List<T> find(final Query query) throws StorageException {
		if (query == null)
			throw new IllegalArgumentException("query argument in null.");

		List<T> result = new ArrayList<T>();

		BasicDBObject mongoQuery = MongoQueryMapper.map(query);
		if (mongoQuery == null)
			return result;

		DBCursor rawResult = null;
		try {
			rawResult = getCollection().find(mongoQuery);

			BasicDBObject sorting = MongoQueryMapper.getSorting(query);
			if (sorting != null)
				rawResult.sort(sorting);

			OffsetQuery offset = MongoQueryMapper.getOffset(query);
			if (offset != null)
				rawResult.skip(offset.getQueryValue().getValue());

			LimitQuery limit = MongoQueryMapper.getLimit(query);
			if (limit != null)
				rawResult.limit(limit.getQueryValue().getValue());

			// processing results
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

			while (rawResult.hasNext()) {
				DBObject obj = rawResult.next();
				try {
					result.add(objectMapper.readValue(obj.toString(), entityClass));
				} catch (JsonParseException e) {
					throw new StorageException("Can't parse entity[" + obj + "].", e);
				} catch (JsonMappingException e) {
					throw new StorageException("Can't map entity[" + obj + "].", e);
				} catch (IOException e) {
					throw new StorageException(e);
				}
			}

			return result;
		} catch (MongoException e) {
			throw new StorageException("Can't exequte query[" + query + "].", e);
		} finally {
			if (rawResult != null)
				rawResult.close();
		}
	}

}