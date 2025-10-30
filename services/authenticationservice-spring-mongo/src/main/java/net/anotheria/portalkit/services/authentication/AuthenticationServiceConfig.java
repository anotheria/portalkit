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
}
