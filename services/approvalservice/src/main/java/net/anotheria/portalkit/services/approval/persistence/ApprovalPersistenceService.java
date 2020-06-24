package net.anotheria.portalkit.services.approval.persistence;

import java.util.List;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.common.AccountId;
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
	 * @param newTicket	new ticket
	 * @return crated {@link TicketDO}
	 * @throws ApprovalPersistenceServiceException if error
	 */
	TicketDO createTicket(TicketDO newTicket) throws ApprovalPersistenceServiceException;

	/**
	 * Gets tocket by ID.
	 *
	 * @param ticketId ticket id
	 * @return {@link TicketDO} instance
	 * @throws ApprovalPersistenceServiceException if error
	 */
	TicketDO getTicketById(long ticketId) throws ApprovalPersistenceServiceException;

	/**
	 * Gets tocket by ID.
	 *
	 * @param referenceId reference id
	 * @return {@link TicketDO} instance
	 * @throws ApprovalPersistenceServiceException if error
	 */
	TicketDO getTicketByReferenceId(String referenceId) throws ApprovalPersistenceServiceException;

	/**
	 * Deletes ticket by ID.
	 *
	 * @param referenceId reference id
	 * @throws ApprovalPersistenceServiceException if error
	 */
	void deleteTicketByReferenceId(String referenceId) throws ApprovalPersistenceServiceException;

	/**
	 * Approves a ticket.
	 *
	 * @param ticket {@link TicketDO} instance
	 * @throws ApprovalPersistenceServiceException if error
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
	 * Retrieve list of tickets ordered by timestamp
	 * descending. List will contain tickets of specified {@code referenceType} and {@code ticketType}.
	 *
	 * @param referenceType	reference type.
	 * @param ticketType 	ticket type.
	 * @return list of {@link TicketDO} instances
	 * @throws ApprovalPersistenceServiceException if error.
	 */
	List<TicketDO> getTickets(long referenceType, String ticketType) throws ApprovalPersistenceServiceException;

	/**
	 * Retrieve list of tickets ordered by timestamp descending. List will contain tickets
	 * of specified referenceType, ticketType and locale.
	 *
	 * @param referenceType	reference type
	 * @param ticketType	ticket type
	 * @param locale		locale
	 * @return				list of {@link TicketDO} instances
	 * @throws ApprovalPersistenceServiceException if any errors occurs
	 */
	List<TicketDO> getTickets(long referenceType, String ticketType, String locale) throws ApprovalPersistenceServiceException;

	/**
	 * Retrieve list of tickets IN_APPROVAL status ordered by timestamp
	 * descending. List will contain tickets of specified locale
	 *
	 * @param locale	locale
	 * @return list of {@link TicketDO} instances
	 * @throws ApprovalPersistenceServiceException if error
	 */
	List<TicketDO> getTickets(String locale) throws ApprovalPersistenceServiceException;

	/**
	 * Retrieve list of all tickets for given {@link AccountId}.
	 *
	 * @param accountId		{@link AccountId} of tickets owner
	 * @return				list of {@link TicketDO} instances
	 * @throws ApprovalPersistenceServiceException if any error
	 */
	List<TicketDO> getTicketsByAccountId(AccountId accountId) throws ApprovalPersistenceServiceException;

	/**
	 * Delete all tickets, where owner is given account id.
	 *
	 * @param accountId			{@link AccountId} of tickets owner
	 * @throws ApprovalPersistenceServiceException if any error
	 */
	void deleteTicketsByAccountId(AccountId accountId) throws ApprovalPersistenceServiceException;
}
