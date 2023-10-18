package net.anotheria.portalkit.services.personaldata;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.personaldata.storage.MongoConnector;
import net.anotheria.util.crypt.CryptTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * {@link dev.morphia.Datastore} instance.
     * */
    private Datastore datastore;

    /**
     * {@link PersonalDataServiceConfig} instance.
     * */
    private PersonalDataServiceConfig config;


    /**
     * Default constructor.
     * */
    public PersonalDataServiceImpl() {
        datastore = MongoConnector.getDatabase();
        config = PersonalDataServiceConfig.getInstance();
    }


    @Override
    public PersonalData get(AccountId accountId) throws PersonalDataServiceException {

        PersonalData personalData = datastore.find(PersonalData.class)
                .filter(Filters.eq("_id", accountId.getInternalId()))
                .first();

        if (personalData == null) {
            return null;
        }

        return decryptPersonalData(personalData);
    }

    @Override
    public void save(PersonalData personalData) throws PersonalDataServiceException {

        if (personalData == null) {
            return;
        }

        personalData.set_id(personalData.getAccountId().getInternalId());
        datastore.save(encryptPersonalData(personalData));
    }

    @Override
    public void delete(AccountId accountId) throws PersonalDataServiceException {

        PersonalData personalData = datastore.find(PersonalData.class)
                .filter(Filters.eq("_id", accountId.getInternalId()))
                .first();

        if (personalData == null) {
            return;
        }

        datastore.delete(personalData);
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
}
