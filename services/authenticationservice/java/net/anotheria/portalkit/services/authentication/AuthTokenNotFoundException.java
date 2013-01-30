package net.anotheria.portalkit.services.authentication;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 30.01.13 10:06
 */
public class AuthTokenNotFoundException extends AuthenticationServiceException{
	public AuthTokenNotFoundException(){
		super("AuthToken not found");
	}
}
