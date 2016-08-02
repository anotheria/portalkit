package net.anotheria.portalkit.services.approval;

import net.anotheria.anoprise.metafactory.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Approval service interface.
 * 
 * @author dagafonov
 * 
 */
public interface ApprovalService extends Service {

	/**
	 * Creates a ticket.
	 * 
	 * @param ticket	new ticket
	 * @return {@link TicketBO}
	 * @throws ApprovalServiceException
	 */
	TicketBO createTicket(TicketBO ticket) throws ApprovalServiceException;

	/**
	 * Removes ticket.
	 * 
	 * @param ticketId
	 * @throws ApprovalServiceException
	 */
	void deleteTicket(long ticketId) throws ApprovalServiceException;

	/**
	 * Gets ticket by internal ID.
	 * 
	 * @param ticketId
	 * @return {@link TicketBO}
	 * @throws ApprovalServiceException
	 */
	TicketBO getTicketById(long ticketId) throws ApprovalServiceException;

	/**
	 * Gets ticket by locale.
	 *
	 * @param locale
	 * @return list of {@link TicketBO}
	 * @throws ApprovalServiceException
	 */
	List<TicketBO> getTicketsByLocale(String locale) throws ApprovalServiceException;

	/**
	 * Gets ticket by type.
	 *
	 * @param referenceType		reference type.
	 * @param ticketType		ticket type.
	 * @return list of {@link TicketBO}
	 * @throws ApprovalServiceException
	 */
	List<TicketBO> getTicketsByType(TicketType ticketType, ReferenceType referenceType) throws ApprovalServiceException;

	/**
	 * Approves ticket.
	 * 
	 * @throws ApprovalServiceException
	 */
	void approveTicket(TicketBO ticket) throws ApprovalServiceException;

	/**
	 * 
	 * @param tickets
	 * @throws ApprovalServiceException
	 */
	void approveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException;

	/**
	 * Disapprove ticket.
	 * 
	 * @throws ApprovalServiceException
	 */
	void disapproveTicket(TicketBO ticket) throws ApprovalServiceException;

	/**
	 * 
	 * @param tickets
	 * @throws ApprovalServiceException
	 */
	void disapproveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException;

	/**
	 * Locks ticket for agent.
	 *
	 * @param agentId 	agent id.
	 * @param ticketId 	ticket id.
	 * */
	void lockTicket(long ticketId, String agentId) throws ApprovalServiceException;

	/**
	 * Unlocks ticket.
	 *
	 * @param ticketId 	ticket id.
	 * */
	void unlockTicket(long ticketId) throws ApprovalServiceException;

	/**
	 * Gets all locked tickets.
	 *
	 * @return list of {@link TicketBO}.
	 * */
	Set<TicketBO> getLockedTickets() throws ApprovalServiceException;

	/**
	 * Gets all unlocked tickets by locale.
	 *
	 * @param locale 	locale.
	 * @param size 		number of elements to print.
	 *
	 * @return list of {@link TicketBO}.
	 * */
	List<TicketBO> getTickets(String locale, String agentId, int size) throws ApprovalServiceException;

	/**
	 * Gets locked tickets for agent.
	 *
	 * @return list of {@link TicketBO}.
	 * */
	List<TicketBO> getLockedTickets(String agentId, String locale) throws ApprovalServiceException;
}
