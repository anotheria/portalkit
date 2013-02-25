package net.anotheria.portalkit.services.approval;

/**
 * Describes statuses of tickets.
 * 
 * @author dagafonov
 * 
 */
public enum TicketStatus {

	NONE, APPROVED, DISAPPROVED, IN_APPROVAL;

	public static TicketStatus find(String s) {
		for (TicketStatus ts : values()) {
			if (ts.name().equalsIgnoreCase(s)) {
				return ts;
			}
		}
		return NONE;
	}

}
