package net.anotheria.portalkit.services.approval;

import net.anotheria.portalkit.services.approval.persistence.TicketDO;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * Ticket value bean.
 * 
 * @author dagafonov
 * 
 */
public class TicketBO implements Serializable {

	/**
	 * Internal ID.
	 */
	private long ticketId;

	/**
	 * Ticket status.
	 */
	private TicketStatus status;

	/**
	 * Ticket type.
	 */
	private TicketType type;

	/**
	 * Agent.
	 */
	private String agent;

	/**
	 * External ID.
	 */
	private String referenceId;

	/**
	 * Reference type.
	 */
	private ReferenceType referenceType;

	/**
	 * Locale.
	 */
	private String locale;

	/**
	 * Timestamp of created ticket.
	 */
	private long created;

	/**
	 * Timestamp of viewed ticket.
	 */
	private long presentation;

	/**
	 * Timestamp of fulfilled ticket.
	 */
	private long fulfillment;

	/**
	 * Total amount of tickets.
	 * */
	private int totalAmountOfTickets;


	public TicketBO() {

	}

	public TicketBO(TicketDO ticket) {

		this.ticketId = ticket.getTicketId();
		this.status = TicketStatus.valueOf(ticket.getStatus());
		this.type = TicketType.valueOf(ticket.getType());
		this.agent = ticket.getAgent();
		this.referenceId = ticket.getReferenceId();
		this.referenceType = ReferenceType.valueOf(ticket.getReferenceType());
		this.locale = ticket.getLocale();
		this.created = ticket.getCreated();
		this.presentation = ticket.getPresentation();
		this.fulfillment = ticket.getFulfillment();
	}


	public long getTicketId() {
		return ticketId;
	}

	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public TicketType getType() {
		return type;
	}

	public void setType(TicketType type) {
		this.type = type;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getPresentation() {
		return presentation;
	}

	public void setPresentation(long presentation) {
		this.presentation = presentation;
	}

	public long getFulfillment() {
		return fulfillment;
	}

	public void setFulfillment(long fulfillment) {
		this.fulfillment = fulfillment;
	}

	public int getTotalAmountOfTickets() {
		return totalAmountOfTickets;
	}

	public void setTotalAmountOfTickets(int totalAmountOfTickets) {
		this.totalAmountOfTickets = totalAmountOfTickets;
	}

	public TicketDO toDO() {

		TicketDO ticket = new TicketDO();

		ticket.setTicketId(this.ticketId);
		ticket.setStatus(this.status.name());
		ticket.setPresentation(this.presentation);
		ticket.setFulfillment(this.fulfillment);
		ticket.setAgent(this.agent);
		ticket.setLocale(this.locale);
		ticket.setReferenceId(this.referenceId);
		ticket.setReferenceType(this.referenceType.getValue());
		ticket.setType(this.type.name());

		return ticket;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.referenceId, this.referenceType, this.status, this.ticketId);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		TicketBO other = (TicketBO) obj;

		if (this.ticketId != other.getTicketId()) {
			return false;
		}

		return true;
	}
}
