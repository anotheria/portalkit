package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * This class represents a non existing account.
 *
 * @author lrosenberg
 * @since 14.12.12 09:10
 */
public final class NullAccount extends Account{
	/**
	 * The one and only instance.
	 */
	static final NullAccount INSTANCE = new NullAccount();

	/**
	 * Not instantiateable.
	 */
	private NullAccount(){}

	@Override public boolean equals(Object o){
		return o == this;
	}

	@Override public int hashCode(){
		return 42;
	}

	@Override public String toString(){
		return "NULL ACCOUNT";
	}

	@Override
	public AccountId getId() {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public void setId(AccountId id) {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public String getEmail() {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public void setEmail(String email) {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public int getType() {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public void setType(int type) {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public long getRegistrationTimestamp() {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}

	@Override
	public void setRegistrationTimestamp(long registrationTimestamp) {
		throw new UnsupportedOperationException("This is a null account, no setter/getter allowed.");
	}
}
