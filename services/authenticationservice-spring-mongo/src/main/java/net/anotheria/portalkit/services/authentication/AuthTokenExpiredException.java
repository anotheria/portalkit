package net.anotheria.portalkit.services.authentication;

/**
 * Token is expired exception.
 *
 * @author lrosenberg
 * @since 30.01.13 14:31
 */
public class AuthTokenExpiredException extends AuthenticationServiceException{
    /**
     * Default constructor.
     */
	public AuthTokenExpiredException(){
		super("AuthToken expired");
	}
}
