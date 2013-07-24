package net.anotheria.portalkit.services.accountsettings;

import java.io.Serializable;

/**
 * DataspaceType interface used in {@link AccountSettingsService}.
 * 
 * @author dagafonov
 * 
 */
public interface DataspaceType extends Serializable {

	/**
	 * Get dataspace type id.
	 * 
	 * @return int
	 */
	int getId();

	/**
	 * Get dataspace type name.
	 * 
	 * @return String
	 */
	String getName();

}
