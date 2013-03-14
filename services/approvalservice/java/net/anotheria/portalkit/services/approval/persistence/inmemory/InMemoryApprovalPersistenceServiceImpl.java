package net.anotheria.portalkit.services.approval.persistence.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.anotheria.portalkit.services.approval.Ticket;
import net.anotheria.portalkit.services.approval.TicketStatus;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceServiceException;

/**
 * InMemory implementation of ApprovalPersistenceService.
 * 
 * @author dagafonov
 * 
 */
public class InMemoryApprovalPersistenceServiceImpl implements ApprovalPersistenceService {

	private Map<String, Ticket> storage = new HashMap<String, Ticket>();

	@Override
	public void approveTicket(Ticket ticket) throws ApprovalPersistenceServiceException {
		Ticket t = getTicketById(ticket.getTicketId());
		if (t != null) {
			t.setStatus(TicketStatus.APPROVED);
		}
	}

	@Override
	public void approveTickets(Collection<Ticket> tickets) throws ApprovalPersistenceServiceException {
		for (Ticket t : tickets) {
			approveTicket(t);
		}
	}

	@Override
	public void createTicket(Ticket newTicket) throws ApprovalPersistenceServiceException {
		storage.put(newTicket.getTicketId(), newTicket);
	}

	@Override
	public void deleteTicket(String ticketId) throws ApprovalPersistenceServiceException {
		storage.remove(ticketId);
	}

	@Override
	public void disapproveTicket(Ticket ticket) throws ApprovalPersistenceServiceException {
		Ticket t = getTicketById(ticket.getTicketId());
		if (t != null) {
			t.setStatus(TicketStatus.DISAPPROVED);
		}
	}

	@Override
	public void disapproveTickets(Collection<Ticket> tickets) throws ApprovalPersistenceServiceException {
		for (Ticket t : tickets) {
			disapproveTicket(t);
		}
	}

	@Override
	public Ticket getTicketById(String ticketId) throws ApprovalPersistenceServiceException {
		Ticket ticket = storage.get(ticketId);
		return ticket;
	}

	@Override
	public List<Ticket> getTickets(Set<String> exclude, int number, long referenceType) throws ApprovalPersistenceServiceException {
		List<Ticket> result = new ArrayList<Ticket>();
		for (String ticketId : storage.keySet()) {
			if (!exclude.contains(ticketId)) {
				Ticket t = storage.get(ticketId);
				if (t.getReferenceType() == referenceType && t.getStatus().equals(TicketStatus.IN_APPROVAL)) {
					result.add(t);
				}
			}
			if (result.size() == number) {
				break;
			}
		}
		return result;
	}

}
