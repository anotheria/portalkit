package net.anotheria.portalkit.services.approval;

/**
 * Describes type of tickets.
 * 
 * @author Vlad Lukjanenko
 * 
 */
public enum TicketType {

	/**
	 * NONE status.
	 */
	NONE,
	/**
	 * Status is approved.
	 */
	APPROVED,
	/**
	 * Status is disapproved.
	 */
	DISAPPROVED,
	/**
	 * Status is in approval process.
	 */
	IN_APPROVAL;

	/**
	 * Searches {@link TicketType} by name.
	 * 
	 * @param s	ticket type name.
	 * @return {@link TicketType}
	 */
	public static TicketType find(String s) {
		for (TicketType ts : values()) {
			if (ts.name().equalsIgnoreCase(s)) {
				return ts;
			}
		}
		return NONE;
	}

}
