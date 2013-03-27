package net.anotheria.portalkit.services.approval;

/**
 * Describes statuses of tickets.
 * 
 * @author dagafonov
 * 
 */
public enum TicketStatus {

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
	 * Searches {@link TicketStatus} by name.
	 * 
	 * @param s
	 * @return {@link TicketStatus}
	 */
	public static TicketStatus find(String s) {
		for (TicketStatus ts : values()) {
			if (ts.name().equalsIgnoreCase(s)) {
				return ts;
			}
		}
		return NONE;
	}

}
