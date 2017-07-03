package net.anotheria.portalkit.services.approval.persistence;

import java.util.List;

import net.anotheria.moskito.aop.annotation.Monitor;
import org.springframework.stereotype.Service;

/**
 * Approval service persistence interface.
 * 
 * @author Vlad Lukjanenko
 * 
 */
@Monitor
@Service
public interface ApprovalPersistenceService {

	/**
	 * Creates a ticket.
	 *
	 * @param newTicket	new ticket.
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	TicketDO createTicket(TicketDO newTicket) throws ApprovalPersistenceServiceException;

	/**
	 * Gets tocket by ID.
	 *
	 * @param ticketId ticket id.
	 * @return {@link TicketDO}
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	TicketDO getTicketById(long ticketId) throws ApprovalPersistenceServiceException;

	/**
	 * Gets tocket by ID.
	 *
	 * @param referenceId reference id.
	 * @return {@link TicketDO}
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	TicketDO getTicketByReferenceId(String referenceId) throws ApprovalPersistenceServiceException;

	/**
	 * Deletes ticket by ID.
	 *
	 * @param referenceId reference id.
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	void deleteTicketByReferenceId(String referenceId) throws ApprovalPersistenceServiceException;

	/**
	 * Approves a ticket.
	 *
	 * @param ticket
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	void updateTicket(TicketDO ticket) throws ApprovalPersistenceServiceException;

	/**
	 * Deletes ticket.
	 *
	 * @param ticketId ticket id.
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	void deleteTicket(long ticketId) throws ApprovalPersistenceServiceException;

	/**
	 * Retrieve list of tickets IN_APPROVAL status ordered by timestamp
	 * descending. List will contain tickets of specified {@code referenceType} and {@code ticketType}.
	 *
	 * @param referenceType	reference type.
	 * @param ticketType 	ticket type.
	 * @return list of {@link TicketDO}
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	List<TicketDO> getTickets(long referenceType, String ticketType) throws ApprovalPersistenceServiceException;

	/**
	 * Retrieve list of tickets IN_APPROVAL status ordered by timestamp
	 * descending. List will contain tickets of specified locale
	 *
	 * @param locale	locale.
	 * @return list of {@link TicketDO}
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	List<TicketDO> getTickets(String locale) throws ApprovalPersistenceServiceException;

}
