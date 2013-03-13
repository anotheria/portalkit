package net.anotheria.portalkit.services.approval.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.approval.Ticket;

/**
 * Approval service persistence interface.
 * 
 * @author dagafonov
 * 
 */
public interface ApprovalPersistenceService extends Service {

	/**
	 * Creates a ticket.
	 * 
	 * @param newTicket
	 * @throws ApprovalPersistenceServiceException
	 */
	public void createTicket(Ticket newTicket) throws ApprovalPersistenceServiceException;

	/**
	 * Gets tocket by ID.
	 * 
	 * @param ticketId
	 * @return
	 * @throws ApprovalPersistenceServiceException
	 */
	public Ticket getTicketById(String ticketId) throws ApprovalPersistenceServiceException;

	/**
	 * Approves a ticket.
	 * 
	 * @param ticket
	 * @throws ApprovalPersistenceServiceException
	 */
	public void approveTicket(Ticket ticket) throws ApprovalPersistenceServiceException;

	/**
	 * Approves list of tickets.
	 * 
	 * @param tickets
	 * @throws ApprovalPersistenceServiceException
	 */
	public void approveTickets(Collection<Ticket> tickets) throws ApprovalPersistenceServiceException;

	/**
	 * Disapproves a ticket.
	 * 
	 * @param ticket
	 * @throws ApprovalPersistenceServiceException
	 */
	public void disapproveTicket(Ticket ticket) throws ApprovalPersistenceServiceException;

	/**
	 * Disapproves list of tickets.
	 * 
	 * @param tickets
	 * @throws ApprovalPersistenceServiceException
	 */
	public void disapproveTickets(Collection<Ticket> tickets) throws ApprovalPersistenceServiceException;

	/**
	 * Deletes ticket.
	 * 
	 * @param ticket
	 * @throws ApprovalPersistenceServiceException
	 */
	public void deleteTicket(String ticketId) throws ApprovalPersistenceServiceException;

	/**
	 * Retrieve list of tickets IN_APPROVAL status ordered by timestamp descending. List will contains max {@code number} of tickets and specified {@code referenceType}.
	 * @param number
	 * @param referenceType
	 * @return
	 * @throws ApprovalPersistenceServiceException
	 */
	public List<Ticket> getTickets(Set<String> keySet, int number, long referenceType)throws ApprovalPersistenceServiceException;

}
