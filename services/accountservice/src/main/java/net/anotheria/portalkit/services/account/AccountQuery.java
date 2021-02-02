package net.anotheria.portalkit.services.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.anotheria.portalkit.services.common.AccountId;

/**
 * Query parameters.
 * 
 * @author Alexandr Bolbat
 */
public final class AccountQuery implements Serializable {
	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -559248313830424493L;
	/**
	 * Account identifiers.
	 */
	private final List<AccountId> ids;
	/**
	 * Account name mask.
	 */
	private final String nameMask;
	/**
	 * Account email mask.
	 */
	private final String emailMask;
	/**
	 * Account identifier mask.
	 */
	private final String idMask;
	/**
	 * Included types.
	 */
	private final List<Integer> typesIncluded;
	/**
	 * Excluded types.
	 */
	private final List<Integer> typesExcluded;
	/**
	 * Included statuses.
	 */
	private final List<Long> statusesIncluded;
	/**
	 * Excluded statuses.
	 */
	private final List<Long> statusesExcluded;
	/**
	 * Registered from.
	 */
	private final Long registeredFrom;
	/**
	 * Registered till.
	 */
	private final Long registeredTill;
	/**
	 * {@link List} of {@link String} tenants.
	 */
	private final List<String> tenants;

	private AccountQuery(Builder builder) {
		this.ids = builder.getIds();
		this.nameMask = builder.getNameMask();
		this.emailMask = builder.getEmailMask();
		this.idMask = builder.getIdMask();
		this.typesIncluded = builder.getTypesIncluded();
		this.typesExcluded = builder.getTypesExcluded();
		this.statusesIncluded = builder.getStatusesIncluded();
		this.statusesExcluded = builder.getStatusesExcluded();
		this.registeredFrom = builder.getRegisteredFrom();
		this.registeredTill = builder.getRegisteredTill();
		this.tenants = builder.getTenants();
	}

	public List<AccountId> getIds() {
		return ids;
	}

	public String getNameMask() {
		return nameMask;
	}

	public String getEmailMask() {
		return emailMask;
	}

	public String getIdMask() {
		return idMask;
	}

	public List<Integer> getTypesIncluded() {
		return typesIncluded;
	}

	public List<Integer> getTypesExcluded() {
		return typesExcluded;
	}

	public List<Long> getStatusesIncluded() {
		return statusesIncluded;
	}

	public List<Long> getStatusesExcluded() {
		return statusesExcluded;
	}

	public Long getRegisteredFrom() {
		return registeredFrom;
	}

	public Long getRegisteredTill() {
		return registeredTill;
	}

	public List<String> getTenants() {
		return tenants;
	}

	@Override
	public String toString() {
		return "AccountQuery{" +
				"ids=" + ids +
				", nameMask='" + nameMask + '\'' +
				", emailMask='" + emailMask + '\'' +
				", idMask='" + idMask + '\'' +
				", typesIncluded=" + typesIncluded +
				", typesExcluded=" + typesExcluded +
				", statusesIncluded=" + statusesIncluded +
				", statusesExcluded=" + statusesExcluded +
				", registeredFrom=" + registeredFrom +
				", registeredTill=" + registeredTill +
				", tenants=" + tenants +
				'}';
	}

	/**
	 * Builder for {@link AccountQuery}.
	 */
	public static class Builder {
		private List<AccountId> ids = new ArrayList<>();
		private String nameMask;
		private String emailMask;
		private String idMask;
		private List<Integer> typesIncluded = new ArrayList<>();
		private List<Integer> typesExcluded = new ArrayList<>();
		private List<Long> statusesIncluded = new ArrayList<>();
		private List<Long> statusesExcluded = new ArrayList<>();
		private Long registeredFrom;
		private Long registeredTill;
		private List<String> tenants = new ArrayList<>();

		/**
		 * Default constructor of builder.
		 */
		public Builder() {}

		/**
		 * Build {@link AccountQuery}.
		 *
		 * @return  constructed {@link AccountQuery}
		 */
		public AccountQuery build() {
			return new AccountQuery(this);
		}

		public Builder setIds(List<AccountId> ids) {
			this.ids = ids;
			return this;
		}

		public Builder setNameMask(String nameMask) {
			this.nameMask = nameMask;
			return this;
		}

		public Builder setEmailMask(String emailMask) {
			this.emailMask = emailMask;
			return this;
		}

		public Builder setIdMask(String idMask) {
			this.idMask = idMask;
			return this;
		}

		public Builder setTypesIncluded(List<Integer> typesIncluded) {
			this.typesIncluded = typesIncluded;
			return this;
		}

		public Builder setTypesExcluded(List<Integer> typesExcluded) {
			this.typesExcluded = typesExcluded;
			return this;
		}

		public Builder setStatusesIncluded(List<Long> statusesIncluded) {
			this.statusesIncluded = statusesIncluded;
			return this;
		}

		public Builder setStatusesExcluded(List<Long> statusesExcluded) {
			this.statusesExcluded = statusesExcluded;
			return this;
		}

		public Builder setRegisteredFrom(Long registeredFrom) {
			this.registeredFrom = registeredFrom;
			return this;
		}

		public Builder setRegisteredTill(Long registeredTill) {
			this.registeredTill = registeredTill;
			return this;
		}

		public Builder addTenants(List<String> tenants) {
			this.tenants.addAll(tenants);
			return this;
		}

		public Builder addTenant(String tenant) {
			tenants.add(tenant);
			return this;
		}

		List<AccountId> getIds() {
			return ids;
		}

		String getNameMask() {
			return nameMask;
		}

		String getEmailMask() {
			return emailMask;
		}

		String getIdMask() {
			return idMask;
		}

		List<Integer> getTypesIncluded() {
			return typesIncluded;
		}

		List<Integer> getTypesExcluded() {
			return typesExcluded;
		}

		List<Long> getStatusesIncluded() {
			return statusesIncluded;
		}

		List<Long> getStatusesExcluded() {
			return statusesExcluded;
		}

		Long getRegisteredFrom() {
			return registeredFrom;
		}

		Long getRegisteredTill() {
			return registeredTill;
		}

		List<String> getTenants() {
			return tenants;
		}
	}
}