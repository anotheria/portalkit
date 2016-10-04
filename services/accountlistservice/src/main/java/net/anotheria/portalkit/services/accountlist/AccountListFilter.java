package net.anotheria.portalkit.services.accountlist;

import java.io.Serializable;

import net.anotheria.portalkit.services.accountlist.sorter.AccountListFieldComparators;
import net.anotheria.portalkit.services.accountlist.sorter.Pager;
import net.anotheria.portalkit.services.accountlist.sorter.SortingDirection;

/**
 * 
 * @author dagafonov
 * 
 */
public class AccountListFilter implements Serializable {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 3170245878233025405L;

	/**
	 * 
	 */
	private SortingDirection orderBy;

	/**
	 * 
	 */
	private Pager pager;

	/**
	 * 
	 */
	private AccountListFieldComparators sortBy;

	/**
	 * Default constructor.
	 */
	public AccountListFilter() {
		setOrderBy(SortingDirection.ASC);
		setPager(new Pager(1, Integer.MAX_VALUE));
		setSortBy(AccountListFieldComparators.CREATION_TIMESTAMP);
	}

	/**
	 * Constructor with all parameters.
	 * 
	 * @param sortBy	{@link AccountListFieldComparators}
	 * @param orderBy	{@link SortingDirection}
	 * @param pager		{@link Pager}
	 */
	public AccountListFilter(AccountListFieldComparators sortBy, SortingDirection orderBy, Pager pager) {
		setOrderBy(orderBy);
		setPager(pager);
		setSortBy(sortBy);
	}

	public SortingDirection getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(SortingDirection orderBy) {
		this.orderBy = orderBy;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public AccountListFieldComparators getSortBy() {
		return sortBy;
	}

	public void setSortBy(AccountListFieldComparators sortBy) {
		this.sortBy = sortBy;
	}

}
