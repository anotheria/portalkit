package net.anotheria.portalkit.services.approval;

import net.anotheria.anoprise.metafactory.Service;

import java.util.Collection;

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
	Ticket createTicket(String ticketReferenceId, long referenceType) throws ApprovalServiceException;

	/**
	 * Deletes ticket.
	 * 
	 * @param ticket
	 * @throws ApprovalServiceException
	 */
	void deleteTicket(Ticket ticket) throws ApprovalServiceException;

	/**
	 * 
	 * @param ticketId
	 * @throws ApprovalServiceException
	 */
	void deleteTicket(String ticketId) throws ApprovalServiceException;

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
	void approveTicket(Ticket ticket) throws ApprovalServiceException;

	/**
	 * 
	 * @param tickets
	 * @throws ApprovalServiceException
	 */
	void approveTickets(Collection<Ticket> tickets) throws ApprovalServiceException;

	/**
	 * Disapprove ticket.
	 * 
	 * @throws ApprovalServiceException
	 */
	void disapproveTicket(Ticket ticket) throws ApprovalServiceException;

	/**
	 * 
	 * @param ticket
	 * @throws ApprovalServiceException
	 */
	void disapproveTickets(Collection<Ticket> ticket) throws ApprovalServiceException;

	/**
	 * 
	 * @param toApprove
	 * @param toDisapprove
	 */
	void proceedTickets(Collection<Ticket> toApprove, Collection<Ticket> toDisapprove) throws ApprovalServiceException;

	/**
	 * Reserves tickets ticketsToReserve for {@code reservationObject} and gets
	 * {@code number} of tickets by their {@code referenceType}.
	 * 
	 * @param reservationObject
	 * @param ticketsToReserve
	 * @throws ApprovalServiceException
	 */
	Collection<Ticket> getAndReserveTickets(String reservationObject, int number, long referenceType) throws ApprovalServiceException;

	/**
	 * Unreserves tickets for reservationObject.
	 * 
	 * @param reservationObject
	 * @throws ApprovalServiceException
	 */
	void unReserveTickets(String reservationObject) throws ApprovalServiceException;

	/**
	 * Gets reserved tickets for reservationObject.
	 * 
	 * @param reservationObject
	 * @return
	 * @throws ApprovalServiceException
	 */
	Collection<Ticket> getReservedTickets(String reservationObject) throws ApprovalServiceException;

}
