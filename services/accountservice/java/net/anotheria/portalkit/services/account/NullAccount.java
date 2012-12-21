package net.anotheria.portalkit.services.account;

/**
 * This class represents a non existing account.
 *
 * @author lrosenberg
 * @since 14.12.12 09:10
 */
public class NullAccount extends Account{
	static final NullAccount INSTANCE = new NullAccount();

	private NullAccount(){}

	@Override public boolean equals(Object o){
		return o == this;
	}

	@Override public int hashCode(){
		return 42;
	}

}
