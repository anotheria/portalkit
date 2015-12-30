package net.anotheria.portalkit.services.profileservice;

import com.mongodb.*;
import com.mongodb.util.JSON;
import net.anotheria.portalkit.services.profileservice.index.Index;
import net.anotheria.portalkit.services.profileservice.index.IndexField;
import net.anotheria.util.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author asamoilich.
 */
public class ProfileServiceImpl<T extends Profile> implements ProfileService<T> {

    /**
     * {@link Logger} instance.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private static final String MONGO_ID = "_id";

    private final ProfileServiceConfig config;
    private MongoClient mongoClient;

    /**
     * Entity class.
     */
    private final Class<T> entity;


    /**
     * Public constructor.
     *
     * @param entity      entity class
     * @param conf        service configuration name, can be <code>null</code>
     * @param env         configuration environment, can be <code>null</code>
     */
    public ProfileServiceImpl(final Class<T> entity, final String conf, final String env) {
        if (entity == null)
            throw new IllegalArgumentException("entity argument is null.");

        this.entity = entity;
        this.config = ProfileServiceConfig.getInstance(conf, env);
        List<ServerAddress> addresses = ProfileServiceUtil.getAddresses(config);
        mongoClient = new MongoClient(addresses);
        initializeIndexes();
    }

    /**
     * Indexes initialization.
     */
    private void initializeIndexes() {
        if (!config.isInitializeIndexes())
            return;

        final List<Index> indexes = config.getIndexes();
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

            getCollection().ensureIndex(fields, options);
        }
    }


    @Override
    public T read(String uid) throws ProfileServiceException {
        if (uid == null || uid.trim().isEmpty())
            throw new IllegalArgumentException("uid argument is empty.");

        DBObject obj;
        try {
            obj = getCollection().findOne(queryGetEntity(uid));
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't read profile[" + uid + "].", e);
        }

        if (obj == null)
            throw new ProfileNotFoundException(uid);

        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            return objectMapper.readValue(obj.toString(), entity);
        } catch (final JsonParseException e) {
            throw new ProfileServiceException("Can't parse profile[" + obj + "].", e);
        } catch (final JsonMappingException e) {
            throw new ProfileServiceException("Can't map profile[" + obj + "].", e);
        } catch (final IOException e) {
            throw new ProfileServiceException(e);
        }
    }

    @Override
    public T save(T toSave) throws ProfileServiceException {
        if (toSave == null)
            throw new IllegalArgumentException("toSave argument is null.");
        try {
            final DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toSave)));
            getCollection().save(entity);
        } catch (final JsonGenerationException e) {
            throw new ProfileServiceException("Can't generate profile[" + toSave + "].", e);
        } catch (final JsonMappingException e) {
            throw new ProfileServiceException("Can't map profile[" + toSave + "].", e);
        } catch (final IOException e) {
            throw new ProfileServiceException(e);
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't save profile[" + toSave + "].", e);
        }
        return read(toSave.get_id());
    }

    @Override
    public T create(T toCreate) throws ProfileServiceException {
        if (toCreate == null)
            throw new IllegalArgumentException("toCreate argument is null.");

        final String uid = toCreate.get_id();
        try {
            final T entity = read(uid);
            if (entity != null)
                throw new ProfileAlreadyExistException(uid);
        } catch (final ProfileNotFoundException e) {
            if (LOGGER.isTraceEnabled())
                LOGGER.trace("create(" + toCreate + ") expected exception. Message[" + e.getMessage() + "].");
        }

        try {
            final DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toCreate)));
            getCollection().insert(entity);
        } catch (final JsonGenerationException e) {
            throw new ProfileServiceException("Can't generate profile[" + toCreate + "].", e);
        } catch (final JsonMappingException e) {
            throw new ProfileServiceException("Can't map profile[" + toCreate + "].", e);
        } catch (final IOException e) {
            throw new ProfileServiceException(e);
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't create profile[" + toCreate + "].", e);
        }

        return read(uid);
    }

    @Override
    public T update(T toUpdate) throws ProfileServiceException {
        if (toUpdate == null)
            throw new IllegalArgumentException("toUpdate argument is null.");

        // checking is entity exist
        final String uid = toUpdate.get_id();
        read(uid);

        // performing entity update
        try {
            final DBObject entity = DBObject.class.cast(JSON.parse(new ObjectMapper().writeValueAsString(toUpdate)));
            getCollection().update(queryGetEntity(uid), entity);
        } catch (final JsonGenerationException e) {
            throw new ProfileServiceException("Can't generate profile[" + toUpdate + "].", e);
        } catch (final JsonMappingException e) {
            throw new ProfileServiceException("Can't map profile[" + toUpdate + "].", e);
        } catch (final IOException e) {
            throw new ProfileServiceException(e);
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't create profile[" + toUpdate + "].", e);
        }

        // reading updated entity
        return read(uid);
    }

    @Override
    public T delete(String uid) throws ProfileServiceException {
        final T result = read(uid);
        try {
            getCollection().remove(queryGetEntity(uid));
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't delete profile[" + uid + "].", e);
        }
        return result;
    }


    @Override
    public List<T> findAll() throws ProfileServiceException {
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
                    result.add(objectMapper.readValue(obj.toString(), entity));
                } catch (final JsonParseException e) {
                    throw new ProfileServiceException("Can't parse profile[" + obj + "].", e);
                } catch (final JsonMappingException e) {
                    throw new ProfileServiceException("Can't map profile[" + obj + "].", e);
                } catch (final IOException e) {
                    throw new ProfileServiceException(e);
                }
            }

            return result;
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't execute query: find all entities.", e);
        } finally {
            if (rawResult != null)
                rawResult.close();
        }
    }

    @Override
    public List<T> find(final BasicDBObject mongoQuery) throws ProfileServiceException {
        if (mongoQuery == null)
            throw new IllegalArgumentException("query argument in null.");

        final List<T> result = new ArrayList<T>();
        DBCursor rawResult = null;
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("find(" + mongoQuery + ") executing with mongo query[" + mongoQuery + "].");

            rawResult = getCollection().find(mongoQuery);

            // processing results
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

            while (rawResult.hasNext()) {
                final DBObject obj = rawResult.next();
                try {
                    result.add(objectMapper.readValue(obj.toString(), entity));
                } catch (final JsonParseException e) {
                    throw new ProfileServiceException("Can't parse profile[" + obj + "].", e);
                } catch (final JsonMappingException e) {
                    throw new ProfileServiceException("Can't map profile[" + obj + "].", e);
                } catch (final IOException e) {
                    throw new ProfileServiceException(e);
                }
            }

            return result;
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't execute profile[" + mongoQuery + "].", e);
        } finally {
            if (rawResult != null)
                rawResult.close();
        }
    }

    private String getDBName() {
        return config.getDatabaseName();
    }

    private DBCollection getCollection() {
        return mongoClient.getDB(getDBName()).getCollection(config.getCollectionName());
    }

    private static DBObject queryGetEntity(final String uid) {
        return new BasicDBObject(MONGO_ID, uid);
    }
}

