package net.anotheria.portalkit.services.profileservice;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import net.anotheria.portalkit.services.profileservice.index.Index;
import net.anotheria.portalkit.services.profileservice.index.IndexField;
import net.anotheria.util.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
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

    private static final String FIELD_ID_NAME = "_id";

    private final ProfileServiceConfig config;
    private final MongoClient mongoClient;

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
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(addresses))
                .build();
        mongoClient = MongoClients.create(settings);
        initializeIndexes();
    }

    /**
     * Indexes initialization.
     */
    private void initializeIndexes() {
        if (!config.isInitializeIndexes())
            return;

        final List<Index> indexes = config.getIndexes();
        if (indexes.isEmpty()) {
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

            Document keys = new Document();
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
                keys.put(name, hashed ? IndexField.MONGO_INDEX_FIELD_PROPERTY_HASHED : order);
            }

            IndexOptions options = new IndexOptions()
                    .name(index.getName())
                    .unique(index.isUnique())
                    .sparse(index.isSparse())
                    .background(index.isBackground());

            getCollection().createIndex(keys, options);
        }
    }


    @Override
    public T read(String uid) throws ProfileServiceException {
        if (uid == null || uid.trim().isEmpty())
            throw new IllegalArgumentException("uid argument is empty.");

        Document obj;

        try {
            obj = getCollection().find(Filters.eq(FIELD_ID_NAME, uid)).first();
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't read profile[" + uid + "].", e);
        }

        if (obj == null)
            throw new ProfileNotFoundException(uid);

        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            return objectMapper.readValue(obj.toJson(), entity);
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
            Document entity = Document.parse(new ObjectMapper().writeValueAsString(toSave));
            try {
                String uid = toSave.get_id();
                read(uid);
                getCollection().replaceOne(Filters.eq(FIELD_ID_NAME, uid), entity);
            } catch (ProfileNotFoundException e) {
                getCollection().insertOne(entity);
            }
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
            Document entity = Document.parse(new ObjectMapper().writeValueAsString(toCreate));
            getCollection().insertOne(entity);
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
            Document entity = Document.parse(new ObjectMapper().writeValueAsString(toUpdate));
            Bson filter = Filters.eq(FIELD_ID_NAME, uid);
            getCollection().replaceOne(filter, entity);
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
            getCollection().deleteOne(Filters.eq(FIELD_ID_NAME, uid));
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't delete profile[" + uid + "].", e);
        }
        return result;
    }


    @Override
    public List<T> findAll() throws ProfileServiceException {
        final List<T> result = new ArrayList<T>();

        MongoCursor<Document> cursor = null;
        try {
            FindIterable<Document> rawResult = getCollection().find();
            cursor = rawResult.iterator();

            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

            while (cursor.hasNext()) {
                final Document doc = cursor.next();
                try {
                    result.add(objectMapper.readValue(doc.toJson(), entity));
                } catch (final JsonParseException e) {
                    throw new ProfileServiceException("Can't parse profile[" + doc + "].", e);
                } catch (final JsonMappingException e) {
                    throw new ProfileServiceException("Can't map profile[" + doc + "].", e);
                } catch (final IOException e) {
                    throw new ProfileServiceException(e);
                }
            }

            return result;
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't execute query: find all entities.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public List<T> find(final BasicDBObject mongoQuery) throws ProfileServiceException {
        if (mongoQuery == null)
            throw new IllegalArgumentException("query argument in null.");

        final List<T> result = new ArrayList<T>();
        MongoCursor<Document> cursor = null;
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("find(" + mongoQuery + ") executing with mongo query[" + mongoQuery + "].");

            FindIterable<Document> rawResult = getCollection().find(mongoQuery);

            // processing results
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

            cursor = rawResult.iterator();
            while (cursor.hasNext()) {
                final Document doc = cursor.next();
                try {
                    result.add(objectMapper.readValue(doc.toJson(), entity));
                } catch (final JsonParseException e) {
                    throw new ProfileServiceException("Can't parse profile[" + doc + "].", e);
                } catch (final JsonMappingException e) {
                    throw new ProfileServiceException("Can't map profile[" + doc + "].", e);
                } catch (final IOException e) {
                    throw new ProfileServiceException(e);
                }
            }

            return result;
        } catch (final MongoException e) {
            throw new ProfileServiceException("Can't execute profile[" + mongoQuery + "].", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    private String getDBName() {
        return config.getDatabaseName();
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(getDBName()).getCollection(config.getCollectionName());
    }

}

