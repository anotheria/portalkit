package net.anotheria.portalkit.services.subscription;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.03.16 17:26
 */
public class SubscriptionServiceException extends Exception {
	public SubscriptionServiceException(String message){
		super (message);
	}

	public SubscriptionServiceException(String message, Throwable cause){
		super (message, cause);
	}
}
