package net.anotheria.portalkit.services.approval;

import java.io.Serializable;

/**
 * Ticket value bean.
 * 
 * @author dagafonov
 * 
 */
public class Ticket implements Serializable {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -7979396779386190431L;

	/**
	 * Internal ID.
	 */
	private String ticketId;

	/**
	 * Ticket status.
	 */
	private TicketStatus status;

	/**
	 * External ID.
	 */
	private String referenceId;

	/**
	 * Reference type.
	 */
	private long referenceType;

	/**
	 * Timestamp of created ticket.
	 */
	private long timestamp = System.currentTimeMillis();

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(long referenceType) {
		this.referenceType = referenceType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ticketId == null) ? 0 : ticketId.hashCode());
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
		Ticket other = (Ticket) obj;
		if (ticketId == null) {
			if (other.ticketId != null)
				return false;
		} else if (!ticketId.equals(other.ticketId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", status=" + status + ", referenceId=" + referenceId + ", referenceType=" + referenceType
				+ ", timestamp=" + timestamp + "]";
	}

}