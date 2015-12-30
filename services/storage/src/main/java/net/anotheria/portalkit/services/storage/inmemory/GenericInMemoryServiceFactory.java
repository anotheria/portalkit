package net.anotheria.portalkit.services.storage.inmemory;

import java.io.Serializable;

import net.anotheria.anoprise.metafactory.AbstractParameterizedServiceFactory;

/**
 * {@link GenericInMemoryService} factory.<br>
 * Check parameters constants.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public final class GenericInMemoryServiceFactory<T extends Serializable> extends AbstractParameterizedServiceFactory<GenericInMemoryService<T>> {

	/**
	 * Parameter name for entity key field name.<br>
	 * Required.
	 */
	public static final String PARAMETER_ENTITY_KEY_FIELD_NAME = "entityKeyFieldName";

	@Override
	public GenericInMemoryService<T> create() {
		String entityKeyFieldName = getParameterValueAsString(PARAMETER_ENTITY_KEY_FIELD_NAME);
		return new GenericInMemoryServiceImpl<T>(entityKeyFieldName);
	}

}
