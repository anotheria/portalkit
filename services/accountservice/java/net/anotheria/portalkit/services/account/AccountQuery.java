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
public class AccountQuery implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -559248313830424493L;

	/**
	 * Account identifiers.
	 */
	private final List<AccountId> ids = new ArrayList<AccountId>();

	/**
	 * Account name mask.
	 */
	private String nameMask;

	/**
	 * Account email mask.
	 */
	private String emailMask;

	/**
	 * Account identifier mask.
	 */
	private String idMask;

	/**
	 * Included types.
	 */
	private final List<Integer> typesIncluded = new ArrayList<Integer>();

	/**
	 * Excluded types.
	 */
	private final List<Integer> typesExcluded = new ArrayList<Integer>();

	/**
	 * Included statuses.
	 */
	private final List<Long> statusesIncluded = new ArrayList<Long>();

	/**
	 * Excluded statuses.
	 */
	private final List<Long> statusesExcluded = new ArrayList<Long>();

	/**
	 * Registered from.
	 */
	private Long registeredFrom;

	/**
	 * Registered till.
	 */
	private Long registeredTill;

	public List<AccountId> getIds() {
		return ids;
	}

	public String getNameMask() {
		return nameMask;
	}

	public void setNameMask(final String aNameMask) {
		this.nameMask = aNameMask;
	}

	public String getEmailMask() {
		return emailMask;
	}

	public void setEmailMask(final String aEmailMask) {
		this.emailMask = aEmailMask;
	}

	public String getIdMask() {
		return idMask;
	}

	public void setIdMask(final String aIdMask) {
		this.idMask = aIdMask;
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

	public void setRegisteredFrom(final Long aRegisteredFrom) {
		this.registeredFrom = aRegisteredFrom;
	}

	public Long getRegisteredTill() {
		return registeredTill;
	}

	public void setRegisteredTill(final Long aRegisteredTill) {
		this.registeredTill = aRegisteredTill;
	}

}
