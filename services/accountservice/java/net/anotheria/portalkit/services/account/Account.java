package net.anotheria.portalkit.services.account;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Representation of a user account.
 *
 * @author lrosenberg
 * @since 11.12.12 11:08
 */
public class Account {
	/**
	 * The account id.
	 */
	private AccountId id;
	/**
	 * Name that is used by the user. Can be email.
	 */
	private String name;

	/**
	 * Email of the user.
	 */
	private String email;

	/**
	 * Type of the user.
	 */
	private int type;

	/**
	 * The creation timestamp for this account.
	 */
	private long registrationTimestamp;

	public Account(){
		registrationTimestamp = System.currentTimeMillis();
	}

	public Account(AccountId anId){
		this();
		id = anId;
	}

	/**
	 * Creates a new account as copy of this account.
	 * @param acc
	 */
	public Account(Account acc){
		this();
		copyFrom(acc);
	}

	public AccountId getId() {
		return id;
	}

	public void setId(AccountId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getRegistrationTimestamp() {
		return registrationTimestamp;
	}

	public void setRegistrationTimestamp(long registrationTimestamp) {
		this.registrationTimestamp = registrationTimestamp;
	}

	public static final Account newAccountFromPattern(Account pattern){
		Account ret = new Account(AccountId.generateNew());
		ret.copyFrom(pattern);
		return ret;
	}

	private void copyFrom(Account anotherAccount){
		name = anotherAccount.name;
		email = anotherAccount.email;
		type = anotherAccount.type;
	}

	@Override public String toString(){
		return getId()+" "+getName()+" "+getType();
	}

	@Override public boolean equals(Object o){
		return o instanceof Account && ((Account)o).getId().equals(getId());
	}

}
