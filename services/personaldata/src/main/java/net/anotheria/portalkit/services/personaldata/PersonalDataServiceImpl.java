package net.anotheria.portalkit.services.personaldata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.morphia.Datastore;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.personaldata.storage.MongoConnector;
import net.anotheria.util.crypt.CryptTool;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vlad Lukjanenko
 */
public class PersonalDataServiceImpl implements PersonalDataService {

    /**
     * {@link Logger} instance.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(PersonalDataServiceImpl.class);

    /**
     * {@link Datastore} instance.
     */
    private Datastore datastore;

    /**
     * {@link PersonalDataServiceConfig} instance.
     */
    private PersonalDataServiceConfig config;


    /**
     * Default constructor.
     */
    public PersonalDataServiceImpl() {
        datastore = MongoConnector.getDatabase();
        config = PersonalDataServiceConfig.getInstance();
    }


    @Override
    public PersonalData get(AccountId accountId) throws PersonalDataServiceException {
        Document personalData = getCollection().find(Filters.eq("_id", accountId.getInternalId())).first();
        if (personalData == null) {
            return null;
        }
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        try {
            return decryptPersonalData(objectMapper.readValue(personalData.toJson(), PersonalData.class));
        } catch (IOException e) {
            throw new PersonalDataServiceException(e.getMessage());
        }
    }

    @Override
    public void save(PersonalData personalData) throws PersonalDataServiceException {

        if (personalData == null) {
            return;
        }
        personalData.set_id(personalData.getAccountId().getInternalId());
        PersonalData encryptData = encryptPersonalData(personalData);

        try {
            Document entity = Document.parse(new ObjectMapper().writeValueAsString(encryptData));
            PersonalData oldData = get(personalData.getAccountId());
            if (oldData == null) {
                getCollection().insertOne(entity);
            }
            getCollection().replaceOne(Filters.eq("_id", personalData.getAccountId().getInternalId()), entity);
        } catch (Exception e) {
            throw new PersonalDataServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(AccountId accountId) throws PersonalDataServiceException {
        PersonalData personalData = get(accountId);
        if (personalData != null) {
            getCollection().deleteOne(Filters.eq("id", new ObjectId(accountId.getInternalId())));
        }
    }


    private PersonalData decryptPersonalData(PersonalData toDecrypt) {

        Map<String, String> decrypted = new HashMap<>();
        CryptTool cryptTool = new CryptTool(config.getApplicationSecret() + toDecrypt.getAccountId().getInternalId());

        for (Map.Entry<String, String> entry : toDecrypt.getPersonalData().entrySet()) {
            decrypted.put(entry.getKey(), cryptTool.decryptFromHexTrim(entry.getValue()));
        }

        toDecrypt.setPersonalData(decrypted);

        return toDecrypt;
    }

    private PersonalData encryptPersonalData(PersonalData toEncrypt) {

        Map<String, String> encrypted = new HashMap<>();
        CryptTool cryptTool = new CryptTool(config.getApplicationSecret() + toEncrypt.getAccountId().getInternalId());

        for (Map.Entry<String, String> entry : toEncrypt.getPersonalData().entrySet()) {
            encrypted.put(entry.getKey(), cryptTool.encryptToHex(entry.getValue()));
        }

        toEncrypt.setPersonalData(encrypted);

        return toEncrypt;
    }

    private MongoCollection<Document> getCollection() {
        return datastore.getDatabase().getCollection(PersonalDataServiceConfig.getInstance().getCollectionName());
    }
}
