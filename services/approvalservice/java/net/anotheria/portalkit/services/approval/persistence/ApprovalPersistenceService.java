package net.anotheria.portalkit.services.approval.persistence;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.approval.Ticket;

/**
 * 
 * @author dagafonov
 *
 */
public interface ApprovalPersistenceService extends Service {

	public void createTicket(Ticket newTicket) throws ApprovalPersistenceServiceException;

	public Ticket getTicketById(String ticketId) throws ApprovalPersistenceServiceException;

}
