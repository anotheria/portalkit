package net.anotheria.portalkit.services.authentication;

import java.io.Serializable;

/**
 * TODO comment this class
 * 
 * @author lrosenberg
 * @since 29.01.13 16:40
 */
public class EncryptedAuthToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3339612481447472867L;

	/**
	 * 
	 */
	private AuthToken authToken;

	/**
	 * 
	 */
	private String encryptedVersion;

	public AuthToken getAuthToken() {
		return authToken;
	}

	public void setAuthToken(AuthToken authToken) {
		this.authToken = authToken;
	}

	public String getEncryptedVersion() {
		return encryptedVersion;
	}

	public void setEncryptedVersion(String encryptedVersion) {
		this.encryptedVersion = encryptedVersion;
	}

	@Override
	public String toString() {
		return "AT: " + authToken + " = " + encryptedVersion;
	}
}
