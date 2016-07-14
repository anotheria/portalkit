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
	 * Gets locked tickets for agent.
	 *
	 * @return list of {@link TicketBO}.
	 * */
	Set<TicketBO> getLockedTickets(String agentId) throws ApprovalServiceException;
}
