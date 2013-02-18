package net.anotheria.portalkit.services.account;

import java.io.Serializable;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Representation of a user account.
 *
 * @author lrosenberg
 * @since 11.12.12 11:08
 */
public class Account implements Serializable {
	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -2199455445180759484L;
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
	 * This field is used to set different statuses of the account. Feel free to use whatever you like, but
	 * we would advice to use this field as bitmap and combine multiple statuses.
	 */
	private long status;

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

	/**
	 * Creates a new account from account pattern. Copies all fields (except id) from the pattern account.
	 * Use to properly create new account objects with preset attributes.
	 * @param pattern
	 * @return
	 */
	public static final Account newAccountFromPattern(Account pattern){
		Account ret = new Account(AccountId.generateNew());
		ret.copyFrom(pattern);
		return ret;
	}

	private void copyFrom(Account anotherAccount){
		name = anotherAccount.name;
		email = anotherAccount.email;
		type = anotherAccount.type;
		status = anotherAccount.status;
	}

	@Override public String toString(){
		return getId()+" "+getName()+" "+getType()+" "+getStatus();
	}

	@Override public boolean equals(Object o){
		return o instanceof Account && ((Account)o).getId().equals(getId());
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + type;
		result = 31 * result + (int) (status ^ (status >>> 32));
		result = 31 * result + (int) (registrationTimestamp ^ (registrationTimestamp >>> 32));
		return result;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	/**
	 * Adds aditional status (sets bit) to the internal status bitmap.
	 * @param aStatus
	 */
	public void addStatus(long aStatus){
		status |= aStatus;
	}

	/**
	 * Returns true if the submitted status bit is set.
	 * @param aStatus
	 * @return
	 */
	public boolean hasStatus(long aStatus){
		return (status & aStatus) == aStatus;
	}	

}
