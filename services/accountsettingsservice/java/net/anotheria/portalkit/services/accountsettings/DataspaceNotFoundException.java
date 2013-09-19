package net.anotheria.portalkit.services.accountsettings;

/**
 * Thrown if dataspace doesn't exists.
 *
 * @author lrosenberg
 * @since 19.09.13 23:08
 */
public class DataspaceNotFoundException extends AccountSettingsServiceException {
	public DataspaceNotFoundException(String name){
		super("Dataspace "+name+" not found.");
	}
}
