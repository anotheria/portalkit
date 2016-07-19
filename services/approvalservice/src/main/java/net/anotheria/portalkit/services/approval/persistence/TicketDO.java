package net.anotheria.portalkit.services.approval.persistence;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Ticket value bean.
 * 
 * @author Vlad Lukjanenko
 * 
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "ticketsapproval")
@NamedQueries({
		@NamedQuery(
				name = TicketDO.GET_TICKET_BY_ID,
				query = "select t from TicketDO t where t.ticketId = :ticketId"
		),
		@NamedQuery(
				name = TicketDO.DELETE_TICKET_BY_ID,
				query = "delete from TicketDO t where t.ticketId = :ticketId"
		),
		@NamedQuery(
				name = TicketDO.GET_TICKETS_BY_TYPE,
				query = "select t from TicketDO t where t.referenceType = :referenceType order by t.created desc"
		),
		@NamedQuery(
				name = TicketDO.GET_TICKETS_BY_LOCALE,
				query = "select t from TicketDO t where t.locale = :locale and ticketType = 'IN_APPROVAL' order by t.created asc"
		)
})
public class TicketDO implements Serializable {

	public static final String GET_TICKET_BY_ID = "TicketDO.getTicketById";
	public static final String DELETE_TICKET_BY_ID = "TicketDO.deleteTicketById";
	public static final String GET_TICKETS_BY_TYPE = "TicketDO.getTicketsByType";
	public static final String GET_TICKETS_BY_LOCALE = "TicketDO.getTicketsByLocale";

	/**
	 * Internal ID.
	 */
	@Column @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long ticketId;

	/**
	 * Ticket status.
	 */
	@Column
	private String status;

	/**
	 * Ticket ticketType.
	 */
	@Column(name = "type")
	private String ticketType;

	/**
	 * Agent.
	 */
	@Column
	private String agent;

	/**
	 * External ID.
	 */
	@Column
	private String referenceId;

	/**
	 * Reference ticketType.
	 */
	@Column
	private long referenceType;

	/**
	 * Locale.
	 */
	@Column
	private String locale;

	/**
	 * Timestamp of created ticket.
	 */
	@Column
	private long created = System.currentTimeMillis();

	/**
	 * Timestamp of viewed ticket.
	 */
	@Column
	private long presentation;

	/**
	 * Timestamp of fulfilled ticket.
	 */
	@Column
	private long fulfillment;


	public TicketDO() {

	}


	public long getTicketId() {
		return ticketId;
	}

	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return ticketType;
	}

	public void setType(String type) {
		this.ticketType = type;
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

	public long getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(long referenceType) {
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

		TicketDO other = (TicketDO) obj;

		if (this.ticketId != other.getTicketId()) {
			return false;
		}

		return true;
	}
}
