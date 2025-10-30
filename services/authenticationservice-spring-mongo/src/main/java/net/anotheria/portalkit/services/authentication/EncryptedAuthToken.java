package net.anotheria.portalkit.services.authentication;

import java.io.Serializable;

/**
 * Encrypted {@link AuthToken} data.
 * 
 * @author lrosenberg
 * @since 29.01.13 16:40
 */
public class EncryptedAuthToken implements Serializable {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -3339612481447472867L;
	/**
	 * {@link AuthToken} data
	 */
	private AuthToken authToken;

	/**
	 * Encrypted presentation of {@link AuthToken}.
	 */
	private String encryptedVersion;

    /**
     * Default constructor.
     */
    public EncryptedAuthToken() {
    }

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
