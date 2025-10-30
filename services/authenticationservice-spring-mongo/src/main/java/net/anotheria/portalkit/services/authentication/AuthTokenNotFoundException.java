package net.anotheria.portalkit.services.authentication;

/**
 * Token not found exception.
 *
 * @author lrosenberg
 * @since 30.01.13 10:06
 */
public class AuthTokenNotFoundException extends AuthenticationServiceException{
    /**
     * Default constructor
     */
    public AuthTokenNotFoundException(){
		super("AuthToken not found");
	}
}
