package net.anotheria.portalkit.services.account;

/**
 * 
 * @author dagafonov
 *
 * @param <T>
 */
public interface AccountType<T extends Enum<T>> {
	
	/**
	 * 
	 * @return int
	 */
	int getId();
	
	/**
	 * 
	 * @return String
	 */
	String getName();
	
	/**
	 * 
	 * @param type
	 * @return {@link T}
	 */
	T find(int type);

}
