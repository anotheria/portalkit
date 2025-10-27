package net.anotheria.portalkit.services.account;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.01.13 00:36
 */
public class AccountAlreadyExistsException extends AccountServiceException {
	public AccountAlreadyExistsException(String field, String value){
		super("Account with "+field+" = "+value+" already exists and double "+field+" values are forbidden.");
	}

	public AccountAlreadyExistsException(String field, String value, String brand) {
		super("Account with " + field + " = " + value + " already with brand:" + brand + " exists and double values are forbidden.");
	}
}
