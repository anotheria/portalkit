package net.anotheria.portalkit.services.authentication;

/**
 * Some preselected auth token types. Feel free to create your own. The idea of this class is to have some kind of convinience between multiple portalkit projects.
 *
 * @author lrosenberg
 * @since 11.12.12 16:02
 */
public class AuthTokenTypes {
	/**
	 * Login credentials stored in browser.
	 */
	public static final int BROWSER_LOGIN = 1;
	/**
	 * Login for mail.
	 */
	public static final int MAIL_LOGIN = 2;
	/**
	 * Application login for a 3rd party application.
	 */
	public static final int APPLICATION_LOGIN = 3;
}
