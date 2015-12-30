package net.anotheria.portalkit.services.authentication;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 30.01.13 14:31
 */
public class AuthTokenExpiredException extends AuthenticationServiceException{
	public AuthTokenExpiredException(){
		super("AuthToken expired");
	}
}
