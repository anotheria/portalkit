package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.authentication.encryptors.BlowfishPasswordEncryptionAlgorithm;
import org.configureme.annotations.ConfigureMe;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 23:17
 */
@ConfigureMe (allfields = true, name="pk-auth")
public class AuthenticationServiceConfig {
	private String passwordKey = "PORTALKITFOREVER";
	private String passwordAlgorithm = BlowfishPasswordEncryptionAlgorithm.class.getName();
	/**
	 * Minimum age of the stored last used timestamp of a token before it is written again. A long lived token
	 * which is used by an integration is authenticated with on every single api call, and writing the timestamp
	 * each time would put a database write on the hot path of every request. With this interval a busy token
	 * costs one write per interval instead, at the price of the last used information being that coarse.
	 */
	private int lastUsedUpdateIntervalInHours = 24;

	public String getPasswordKey() {
		return passwordKey;
	}

	public void setPasswordKey(String passwordKey) {
		this.passwordKey = passwordKey;
	}

	public String getPasswordAlgorithm() {
		return passwordAlgorithm;
	}

	public void setPasswordAlgorithm(String passwordAlgorithm) {
		this.passwordAlgorithm = passwordAlgorithm;
	}

	public int getLastUsedUpdateIntervalInHours() {
		return lastUsedUpdateIntervalInHours;
	}

	public void setLastUsedUpdateIntervalInHours(int lastUsedUpdateIntervalInHours) {
		this.lastUsedUpdateIntervalInHours = lastUsedUpdateIntervalInHours;
	}
}
