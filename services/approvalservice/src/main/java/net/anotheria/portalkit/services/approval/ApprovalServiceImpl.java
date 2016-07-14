package net.anotheria.portalkit.services.approval;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceServiceException;
import net.anotheria.portalkit.services.approval.persistence.TicketDO;
import net.anotheria.util.TimeUnit;
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
	 * List of locked tickets
	 * */
	private Map<TicketBO, Long> lockedTickets;

	/**
	 * Default constructor.
	 */
	public ApprovalServiceImpl() {

		cachedTickets = Caches.createHardwiredCache("cache-tickets");
		lockedTickets = new HashMap();

		Thread ticketUnlocker = new Thread(new TicketUnlocker());
		ticketUnlocker.setDaemon(true);
		ticketUnlocker.start();
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
		lockedTickets.put(new TicketBO(ticket), System.currentTimeMillis());
	}

	@Override
	public void unlockTicket(long ticketId) throws ApprovalServiceException {

		TicketDO ticket = null;

		try {
			ticket = persistenceService.getTicketById(ticketId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to get ticket by id=" + ticketId, e);
		}

		lockedTickets.remove(new TicketBO(ticket));
	}

	@Override
	public Set<TicketBO> getLockedTickets() throws ApprovalServiceException {
		return lockedTickets.keySet();
	}

	@Override
	public Set<TicketBO> getLockedTickets(String agentId) throws ApprovalServiceException {

		Set<TicketBO> result = new HashSet<>();

		for (TicketBO ticket : lockedTickets.keySet()) {

			if (ticket.getAgent().equals(agentId)) {
				result.add(ticket);
			}
		}

		return result;
	}


	/**
	 * Unlocks tickets after some period of time.
	 * */
	private class TicketUnlocker implements Runnable {

		/**
		 * Unlocking time in minutes.
		 * */
		private final int RELEASE_TIME = 5;

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(TimeUnit.MINUTE.getMillis(1));
					unlockTickets();
				} catch (InterruptedException e) {
					/* ignore */
				}
			}
		}

		public void unlockTickets() {

			long currentTime = System.currentTimeMillis();

			for(Iterator<Map.Entry<TicketBO, Long>> iterator = lockedTickets.entrySet().iterator(); iterator.hasNext(); ) {

				Map.Entry<TicketBO, Long> entry = iterator.next();

				if(currentTime - entry.getValue() > TimeUnit.MINUTE.getMillis(RELEASE_TIME)) {
					iterator.remove();
				}
			}
		}
	}
}
