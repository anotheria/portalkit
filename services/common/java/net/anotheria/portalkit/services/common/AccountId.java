package net.anotheria.portalkit.services.common;

import net.anotheria.portalkit.services.common.id.IdGenerator;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.10.12 21:54
 */
public class AccountId {
	private String internalId;

	public AccountId(String anInternalId){
		if (anInternalId==null || anInternalId.length()==0)
			throw new IllegalArgumentException("Empty or null id is not allowed, "+anInternalId);
		internalId = anInternalId;
	}

	@Override public boolean equals(Object o){
		return o instanceof AccountId &&
				internalId.equals(((AccountId)o).internalId);
	}

	@Override public int hashCode(){
		return internalId.hashCode();
	}

	public static final AccountId generateNew(){
		return new AccountId(IdGenerator.generateUniqueRandomId());
	}

	@Override public String toString(){
		return internalId;
	}

}
