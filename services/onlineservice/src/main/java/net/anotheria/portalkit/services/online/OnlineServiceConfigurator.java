package net.anotheria.portalkit.services.online;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.online.persistence.ActivityPersistenceService;
import net.anotheria.portalkit.services.online.persistence.storagebased.SBActivityPersistenceConstants;
import net.anotheria.portalkit.services.online.persistence.storagebased.SBActivityPersistenceServiceFactory;
import net.anotheria.portalkit.services.online.persistence.storagebased.SBActivityPersistenceServiceImpl;
import net.anotheria.portalkit.services.storage.StorageService;
import net.anotheria.portalkit.services.storage.StorageServiceFactory;
import net.anotheria.portalkit.services.storage.mongo.GenericMongoServiceFactory;
import net.anotheria.portalkit.services.storage.type.StorageType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Configurator that allows distributeme to initialize the service.
 *
 * @author Vlad Lukjanenko
 */
public class OnlineServiceConfigurator {

	public static void configure(){

		Map<String, Serializable> factoryParameters = new HashMap<>();
		factoryParameters.put(StorageServiceFactory.PARAMETER_STORAGE_TYPE, StorageType.NO_SQL_MONGO_GENERIC);
		factoryParameters.put(GenericMongoServiceFactory.PARAMETER_ENTITY_CLASS, SBActivityPersistenceServiceImpl.AccountActivityVO.class);
		factoryParameters.put(GenericMongoServiceFactory.PARAMETER_CONFIGURATION, "pk-storage-mongo-onlineservice-activities");
		MetaFactory.addParameterizedFactoryClass(StorageService.class, SBActivityPersistenceConstants.ACTIVITY_PERSISTENCE_GENERIC_STORAGE_NAME,
				StorageServiceFactory.class, factoryParameters);
		MetaFactory.addAlias(OnlineService.class, Extension.LOCAL);
		MetaFactory.addFactoryClass(OnlineService.class, Extension.LOCAL, OnlineServiceFactory.class);
		MetaFactory.addAlias(ActivityPersistenceService.class, Extension.LOCAL);
		MetaFactory.addFactoryClass(ActivityPersistenceService.class, Extension.LOCAL, SBActivityPersistenceServiceFactory.class);
	}
}
