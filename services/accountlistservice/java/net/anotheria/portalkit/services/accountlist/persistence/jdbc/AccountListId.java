package net.anotheria.portalkit.services.accountlist.persistence.jdbc;

import net.anotheria.anoprise.inmemorymirror.Mirrorable;

public class AccountListId implements Mirrorable<String>{
	
	private Long id;
	private String listName;

	public AccountListId(Long id, String listName) {
		this.id = id;
		this.listName = listName;
	}
	
	public AccountListId(String listName) {
		this.listName = listName;
	}

	@Override
	public String getKey() {
		return id.toString();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getListName() {
		return listName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountListId other = (AccountListId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
