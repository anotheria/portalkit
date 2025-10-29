package net.anotheria.portalkit.apis.common.accountsettings;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.moskito.aop.annotation.Accumulate;
import net.anotheria.moskito.aop.annotation.Accumulates;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.apis.common.BasePortalKitAPIImpl;
import net.anotheria.portalkit.services.accountsettings.*;
import net.anotheria.portalkit.services.accountsettings.attribute.Attribute;
import net.anotheria.portalkit.services.accountsettings.attribute.BooleanAttribute;
import net.anotheria.portalkit.services.accountsettings.attribute.LongAttribute;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

import java.util.Collection;
import java.util.Optional;

@Monitor(category = "api", subsystem = "portalkit")
@Accumulates({
        @Accumulate(valueName = "Avg", intervalName = "5m"),
        @Accumulate(valueName = "Avg", intervalName = "1h"),
        @Accumulate(valueName = "Req", intervalName = "5m"),
        @Accumulate(valueName = "Req", intervalName = "1h"),
        @Accumulate(valueName = "Err", intervalName = "5m"),
        @Accumulate(valueName = "Err", intervalName = "1h"),
        @Accumulate(valueName = "Time", intervalName = "5m"),
        @Accumulate(valueName = "Time", intervalName = "1h")
})
public class AccountSettingsAPIImpl extends BasePortalKitAPIImpl implements AccountSettingsAPI{
    /**
     * {@link AccountSettingsService} instance.
     */
    private AccountSettingsService accountSettingsService;

    private IdBasedLockManager<AccountId> lockManager = new SafeIdBasedLockManager<AccountId>();

    @Override
    public void init() throws APIInitException {
        super.init();

        try {
            accountSettingsService = MetaFactory.get(AccountSettingsService.class);
        } catch (MetaFactoryException e) {
            throw new APIInitException("Can't instantiate " + AccountSettingsService.class.getName(), e);
        }

    }


    @Override
    public Dataspace getDataspace(AccountId accountId, DataspaceType dataspaceType) throws APIException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            try {
                return accountSettingsService.getDataspace(accountId, dataspaceType);
            } catch (DataspaceNotFoundException e) {
                Dataspace newDS = new Dataspace(accountId, dataspaceType);
                return newDS;
            } catch (AccountSettingsServiceException e) {
                throw new APIException("Can't retrieve dataspace from service (" + accountId + ", " + dataspaceType + ")", e);
            }
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void saveDataspace(Dataspace dataspace) throws APIException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(new AccountId(dataspace.getKey().getAccountId()));
        lock.lock();
        try {

            AccountSettingsKey key = dataspace.getKey();
            try {
                accountSettingsService.saveDataspace(dataspace);
            } catch (AccountSettingsServiceException e) {
                throw new APIException("Can't save dataspace due to error in service (" + dataspace + ")", e);
            }
        }finally {
            lock.unlock();
        }

    }

    @Override
    public void deleteDataspaces(AccountId accountId) throws APIException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();



        try {
            Collection<Dataspace> dataspaces = accountSettingsService.getAllDataspaces(accountId);
            for (Dataspace dataspace : dataspaces) {
                try {
                    accountSettingsService.deleteDataspace(accountId, dataspace.getKey().getDataspaceId());
                } catch (AccountSettingsServiceException e) {
                    throw new APIException("Error occurred while removing dataspace" + dataspace.getKey(), e);
                }
            }
        }catch(AccountSettingsServiceException e){
            throw new APIException("Can't retrieve dataspaces for user: " + accountId, e);
        }finally {
            lock.unlock();
        }
    }


    @Override
    public void setAttribute(AccountId accountId, DataspaceType dataspaceType, Attribute... attribute) throws APIException {
        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try{
            Dataspace ds = getDataspace(accountId, dataspaceType);
            for (Attribute attr : attribute) {
                ds.addAttribute(attr.getName(), attr);
            }
            saveDataspace(ds);
        }finally {
            lock.unlock();
        }
    }

    public Optional<Attribute> getAttribute(AccountId accountId, DataspaceType dataspaceType, String attributeName) throws APIException{
        IdBasedLock<AccountId> lock = lockManager.obtainLock(accountId);
        lock.lock();
        try {
            Dataspace ds = getDataspace(accountId, dataspaceType);
            Attribute attribute = ds.getAttribute(attributeName);
            if (attribute != null) {
                return java.util.Optional.of(attribute);
            } else {
                return java.util.Optional.empty();
            }
        }finally {
            lock.unlock();
        }
    }

    protected String getStringAttributeOrNull(AccountId accountId, DataspaceType dataspaceType, String attributeName) throws APIException {
        Optional<String> attr = getStringAttribute(accountId, dataspaceType, attributeName);
        return attr.isEmpty() ? null : attr.get();
    }

    protected Boolean getBooleanAttributeOrNull(AccountId accountId, DataspaceType dataspaceType, String attributeName) throws APIException {
        Optional<Boolean> attr = getBooleanAttribute(accountId, dataspaceType, attributeName);
        return attr.isEmpty() ? null : attr.get();
    }

    protected Long getLongAttributeOrNull(AccountId accountId, DataspaceType dataspaceType, String attributeName) throws APIException {
        Optional<Long> attr = getLongAttribute(accountId, dataspaceType, attributeName);
        return attr.isEmpty() ? null : attr.get();
    }

    protected Optional<String> getStringAttribute(AccountId accountId, DataspaceType dataspaceType, String attributeName) throws APIException {
        Optional<Attribute> attr = getAttribute(accountId, dataspaceType, attributeName);
        return attr.isEmpty() ? Optional.empty() : Optional.of(attr.get().getValueAsString());
    }

    protected Optional<Long> getLongAttribute(AccountId accountId, DataspaceType dataspaceType, String attributeName) throws APIException {
        Optional<Attribute> attr = getAttribute(accountId, dataspaceType, attributeName);
        return attr.isEmpty() ? Optional.empty() : Optional.of(((LongAttribute) attr.get()).getValue());
    }

    protected Optional<Boolean> getBooleanAttribute(AccountId accountId, DataspaceType dataspaceType, String attributeName) throws APIException {
        Optional<Attribute> attr = getAttribute(accountId, dataspaceType, attributeName);
        return attr.isEmpty() ? Optional.empty() : Optional.of(((BooleanAttribute) attr.get()).getValue());
    }

}
