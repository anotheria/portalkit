package net.anotheria.portalkit.services.approval;

import java.util.List;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.Ticket;

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
	 * @param ticketReferenceId
	 * @return
	 * @throws ApprovalServiceException
	 */
	Ticket createTicket(String ticketReferenceId) throws ApprovalServiceException;

	/**
	 * Deletes ticket.
	 * 
	 * @param ticket
	 * @throws ApprovalServiceException
	 */
	void deleteTicket(Ticket ticket) throws ApprovalServiceException;

	/**
	 * Gets ticket by internal ID.
	 * 
	 * @param ticketId
	 * @return
	 * @throws ApprovalServiceException
	 */
	Ticket getTicketById(String ticketId) throws ApprovalServiceException;

	/**
	 * Approves ticket.
	 * 
	 * @throws ApprovalServiceException
	 */
	void approveTicket() throws ApprovalServiceException;

	/**
	 * Disapprove ticket.
	 * 
	 * @throws ApprovalServiceException
	 */
	void disapproveTicket() throws ApprovalServiceException;

	/**
	 * Reserves tickets ticketsToReserve on reservationObject.
	 * 
	 * @param reservationObject
	 * @param ticketsToReserve
	 * @throws ApprovalServiceException
	 */
	void reserveTickets(List<Ticket> ticketsToReserve, String reservationObject) throws ApprovalServiceException;
	
}
