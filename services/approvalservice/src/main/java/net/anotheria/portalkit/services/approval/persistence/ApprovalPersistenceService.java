package net.anotheria.portalkit.services.approval.persistence;

import java.util.Collection;
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
	 * @param newTicket
	 * @throws ApprovalPersistenceServiceException
	 */
	TicketDO createTicket(TicketDO newTicket) throws ApprovalPersistenceServiceException;

	/**
	 * Gets tocket by ID.
	 *
	 * @param ticketId
	 * @return {@link TicketDO}
	 * @throws ApprovalPersistenceServiceException
	 */
	TicketDO getTicketById(long ticketId) throws ApprovalPersistenceServiceException;

	/**
	 * Approves a ticket.
	 *
	 * @param ticket
	 * @throws ApprovalPersistenceServiceException
	 */
	void updateTicket(TicketDO ticket) throws ApprovalPersistenceServiceException;

	/**
	 * Deletes ticket.
	 *
	 * @param ticketId
	 * @throws ApprovalPersistenceServiceException
	 */
	void deleteTicket(long ticketId) throws ApprovalPersistenceServiceException;

	/**
	 * Retrieve list of tickets IN_APPROVAL status ordered by timestamp
	 * descending. List will contain tickets of specified {@code referenceType}.
	 *
	 * @param referenceType
	 * @return {@link List<TicketDO>}
	 * @throws ApprovalPersistenceServiceException
	 */
	List<TicketDO> getTickets(long referenceType) throws ApprovalPersistenceServiceException;

}
