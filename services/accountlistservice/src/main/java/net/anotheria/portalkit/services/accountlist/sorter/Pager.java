package net.anotheria.portalkit.services.accountlist.sorter;

import java.io.Serializable;

/**
 * Contains information about segment of result.
 * 
 * @author dagafonov
 */
public class Pager implements Serializable {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -5204562448124309091L;

	/**
	 * Page number.
	 */
	private int pageNumber;

	/**
	 * Elements per page.
	 */
	private int elementsPerPage;

	/**
	 * Default constructor.
	 * 
	 * @param pageNumber
	 * @param elementsPerPage
	 */
	public Pager(int pageNumber, int elementsPerPage) {
		setPageNumber(pageNumber);
		setElementsPerPage(elementsPerPage);
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getElementsPerPage() {
		return elementsPerPage;
	}

	public void setElementsPerPage(int elementsPerPage) {
		this.elementsPerPage = elementsPerPage;
	}

}