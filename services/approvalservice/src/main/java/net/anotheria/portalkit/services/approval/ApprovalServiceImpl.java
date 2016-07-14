package net.anotheria.portalkit.services.approval;

import java.util.Collection;
import java.util.List;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceServiceException;
import net.anotheria.portalkit.services.approval.persistence.TicketDO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Approval service implementation.
 * 
 * @author Vlad Lukjanenko
 * 
 */
public class ApprovalServiceImpl implements ApprovalService {

	/**
	 * Persistence service.
	 */
	@Autowired
	private ApprovalPersistenceService persistenceService;

	/**
	 * Ticket cache.
	 */
	private Cache<Long, TicketBO> cachedTickets;

	/**
	 * Default constructor.
	 */
	public ApprovalServiceImpl() {
		cachedTickets = Caches.createHardwiredCache("cache-tickets");
	}

	@Override
	public TicketBO createTicket(TicketBO ticket) throws ApprovalServiceException {
		try {
			ticket = new TicketBO(persistenceService.createTicket(ticket.toDO()));
			cachedTickets.put(ticket.getTicketId(), ticket);

			return ticket;
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while creating new ticket", e);
		}
	}

	@Override
	public void deleteTicket(long ticketId) throws ApprovalServiceException {
		try {
			persistenceService.deleteTicket(ticketId);
			cachedTickets.remove(ticketId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while creating new ticket", e);
		}
	}

	@Override
	public TicketBO getTicketById(long ticketId) throws ApprovalServiceException {

		TicketBO ticket = cachedTickets.get(ticketId);

		if (ticket != null) {
			return ticket;
		}

		try {
			ticket = new TicketBO(persistenceService.getTicketById(ticketId));

			return ticket;
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to get ticket by id=" + ticketId, e);
		}
	}

	@Override
	public void approveTicket(TicketBO ticket) throws ApprovalServiceException {

		ticket.setType(TicketType.APPROVED);
		ticket.setStatus(TicketStatus.CLOSED);

		try {
			persistenceService.updateTicket(ticket.toDO());
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to approve ticket with id=" + ticket.getTicketId(), e);
		}

		cachedTickets.remove(ticket.getTicketId());
		cachedTickets.put(ticket.getTicketId(), ticket);
	}

	@Override
	public void approveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException {

		for (TicketBO ticket : tickets) {

			ticket.setType(TicketType.APPROVED);
			ticket.setStatus(TicketStatus.CLOSED);

			try {
				persistenceService.updateTicket(ticket.toDO());
			} catch (ApprovalPersistenceServiceException e) {
				continue;
			}

			cachedTickets.remove(ticket.getTicketId());
			cachedTickets.put(ticket.getTicketId(), ticket);
		}
	}

	@Override
	public void disapproveTicket(TicketBO ticket) throws ApprovalServiceException {

		ticket.setType(TicketType.DISAPPROVED);
		ticket.setStatus(TicketStatus.CLOSED);

		try {
			persistenceService.updateTicket(ticket.toDO());
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to disapprove ticket with id=" + ticket.getTicketId(), e);
		}

		cachedTickets.remove(ticket.getTicketId());
		cachedTickets.put(ticket.getTicketId(), ticket);
	}

	@Override
	public void disapproveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException {

		for (TicketBO ticket : tickets) {

			ticket.setType(TicketType.DISAPPROVED);
			ticket.setStatus(TicketStatus.CLOSED);

			try {
				persistenceService.updateTicket(ticket.toDO());
			} catch (ApprovalPersistenceServiceException e) {
				continue;
			}

			cachedTickets.remove(ticket.getTicketId());
			cachedTickets.put(ticket.getTicketId(), ticket);
		}
	}

	@Override
	public void lockTicket(long ticketId, String agentId) throws ApprovalServiceException {

		TicketDO ticket = null;

		try {
			ticket = persistenceService.getTicketById(ticketId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to get ticket by id=" + ticketId, e);
		}

		ticket.setAgent(agentId);

		try {
			persistenceService.updateTicket(ticket);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to lock ticket with id=" + ticketId + " for agent id=" + agentId, e);
		}

		cachedTickets.remove(ticket.getTicketId());
		cachedTickets.put(ticket.getTicketId(), new TicketBO(ticket));
	}

	@Override
	public void unlockTicket(long ticketId) throws ApprovalServiceException {

		TicketDO ticket = null;

		try {
			ticket = persistenceService.getTicketById(ticketId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to get ticket by id=" + ticketId, e);
		}

		ticket.setAgent(null);

		try {
			persistenceService.updateTicket(ticket);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to unlock ticket with id=" + ticketId, e);
		}

		cachedTickets.remove(ticket.getTicketId());
		cachedTickets.put(ticket.getTicketId(), new TicketBO(ticket));
	}

	@Override
	public List<TicketBO> getLockedTickets() throws ApprovalServiceException {
		return null;
	}

	@Override
	public List<TicketBO> getLockedTickets(String agentId) throws ApprovalServiceException {
		return null;
	}

	@Override
	public List<TicketBO> getUnlockedTickets() throws ApprovalServiceException {
		return null;
	}
}
