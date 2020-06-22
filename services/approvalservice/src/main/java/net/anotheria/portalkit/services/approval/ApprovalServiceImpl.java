package net.anotheria.portalkit.services.approval;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceServiceException;
import net.anotheria.portalkit.services.approval.persistence.TicketDO;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Approval service implementation.
 * 
 * @author Vlad Lukjanenko
 * 
 */
@Service
public class ApprovalServiceImpl implements ApprovalService {

	/**
	 * Persistence service.
	 */
	@Autowired
	private ApprovalPersistenceService approvalPersistenceService;

	/**
	 * Ticket cache.
	 */
	private final Cache<Long, TicketBO> cachedTickets;

	/**
	 * Map of locked tickets.
	 * */
	private final Map<Long, LockedTicket> lockedTickets;

	/**
	 * Default constructor.
	 */
	public ApprovalServiceImpl() {
		cachedTickets = Caches.createHardwiredCache("cache-tickets");
		lockedTickets = new HashMap<>();

		Thread ticketUnlocker = new Thread(new TicketUnlocker());
		ticketUnlocker.setDaemon(true);
		ticketUnlocker.start();
	}

	@Override
	public TicketBO createTicket(TicketBO ticket) throws ApprovalServiceException {
		try {
			ticket = new TicketBO(approvalPersistenceService.createTicket(ticket.toDO()));
			cachedTickets.put(ticket.getTicketId(), ticket);
			return ticket;
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while creating new ticket", e);
		}
	}

	@Override
	public void deleteTicketByReferenceId(String referenceId) throws ApprovalServiceException {
		try {
			TicketBO ticket = new TicketBO(approvalPersistenceService.getTicketByReferenceId(referenceId));
			lockedTickets.remove(ticket.getTicketId());
			approvalPersistenceService.deleteTicket(ticket.getTicketId());
			cachedTickets.remove(ticket.getTicketId());
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while deleteTicketByreferenceId("+referenceId+")", e);
		}
	}

	@Override
	public void deleteTicket(long ticketId) throws ApprovalServiceException {
		try {
			lockedTickets.remove(ticketId);
			approvalPersistenceService.deleteTicket(ticketId);
			cachedTickets.remove(ticketId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while deleteTicket("+ticketId+")", e);
		}
	}

	@Override
	public void deleteTicketsByAccountId(AccountId accountId) throws ApprovalServiceException {
		try {
			List<Long> ids = approvalPersistenceService.getTicketsByAccountId(accountId).stream()
					.map(TicketDO::getTicketId)
					.collect(Collectors.toList());

			for (Long id: ids) {
				lockedTickets.remove(id);
				cachedTickets.remove(id);
			}
			approvalPersistenceService.deleteTicketsByAccountId(accountId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while delete tickets for " + accountId);
		}
	}

	@Override
	public TicketBO getTicketById(long ticketId) throws ApprovalServiceException {

		TicketBO ticket = cachedTickets.get(ticketId);
		if (ticket != null) {
			return ticket;
		}

		try {
			ticket = new TicketBO(approvalPersistenceService.getTicketById(ticketId));
			return ticket;
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to get ticket by id=" + ticketId, e);
		}
	}

	@Override
	public List<TicketBO> getTicketsByLocale(String locale, String agentId) throws ApprovalServiceException {
		List<TicketDO> tickets;
		try {
			tickets = approvalPersistenceService.getTickets(locale);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while getting tickets by locale=" + locale, e);
		}
		return mapWithoutLockedTickets(tickets, agentId);
	}

	@Override
	public List<TicketBO> getTicketsByType(TicketType ticketType, ReferenceType referenceType, String agentId) throws ApprovalServiceException {
		List<TicketDO> tickets = null;
		try {
			tickets = approvalPersistenceService.getTickets(referenceType.getValue(), ticketType.name());
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while getting tickets", e);
		}
		return mapWithoutLockedTickets(tickets, agentId);
	}

	@Override
	public List<TicketBO> getTicketsByTypeAndLocale(TicketType ticketType, ReferenceType referenceType, String locale, String agentId) throws ApprovalServiceException {
		List<TicketDO> tickets = null;
		try {
			tickets = approvalPersistenceService.getTickets(referenceType.getValue(), ticketType.name(), locale);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while getting tickets", e);
		}
		return mapWithoutLockedTickets(tickets, agentId);
	}

	private List<TicketBO> mapWithoutLockedTickets(List<TicketDO> tickets, String agentId) {
		List<TicketBO> result = new ArrayList<>();
		for (TicketDO ticket: tickets) {
			LockedTicket lock = lockedTickets.get(ticket.getTicketId());
			if (lock != null && !lock.getAgentId().equals(agentId)) {
				continue;
			}

			lockedTickets.put(ticket.getTicketId(), new LockedTicket(agentId, System.currentTimeMillis()));
			result.add(new TicketBO(ticket));
		}
		return result;
	}

	@Override
	public List<TicketBO> getTicketsByType(TicketType ticketType, ReferenceType referenceType) throws ApprovalServiceException {
		List<TicketDO> tickets = null;
		List<TicketBO> result = new ArrayList<>();
		try {
			tickets = approvalPersistenceService.getTickets(referenceType.getValue(), ticketType.name());
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while getting tickets", e);
		}

		for (TicketDO ticketDO: tickets) {
			result.add(new TicketBO(ticketDO));
		}
		return result;
	}

	@Override
	public void approveTicket(TicketBO ticket) throws ApprovalServiceException {

		ticket.setType(TicketType.APPROVED);
		ticket.setStatus(TicketStatus.CLOSED);

		try {
			approvalPersistenceService.updateTicket(ticket.toDO());
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to approve ticket with id=" + ticket.getTicketId(), e);
		}

		lockedTickets.remove(ticket.getTicketId());
		cachedTickets.remove(ticket.getTicketId());
		cachedTickets.put(ticket.getTicketId(), ticket);
	}

	@Override
	public void approveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException {
		for (TicketBO ticket: tickets) {
			try {
				approveTicket(ticket);
			} catch (ApprovalServiceException e) {
				//ignore
			}
		}
	}

	@Override
	public void disapproveTicket(TicketBO ticket) throws ApprovalServiceException {

		ticket.setType(TicketType.DISAPPROVED);
		ticket.setStatus(TicketStatus.CLOSED);

		try {
			approvalPersistenceService.updateTicket(ticket.toDO());
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to disapprove ticket with id=" + ticket.getTicketId(), e);
		}

		lockedTickets.remove(ticket.getTicketId());
		cachedTickets.remove(ticket.getTicketId());
		cachedTickets.put(ticket.getTicketId(), ticket);
	}

	@Override
	public void disapproveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException {
		for (TicketBO ticket: tickets) {
			try {
				disapproveTicket(ticket);
			} catch (ApprovalServiceException e) {
				//ignore
			}
		}
	}

	@Override
	public Set<TicketBO> getLockedTickets() throws ApprovalServiceException {

		Set<TicketBO> tickets = new HashSet<>();
		Set<Long> ids = new HashSet<>(lockedTickets.keySet());

		for (long ticketId : ids) {
			try {
				tickets.add(new TicketBO(approvalPersistenceService.getTicketById(ticketId)));
			} catch (ApprovalPersistenceServiceException e) {
				continue;
			}
		}

		return tickets;
	}

	@Override
	public List<TicketBO> getLockedTickets(String locale) throws ApprovalServiceException {

		List<TicketBO> result = new ArrayList<>();
		Set<Long> ids = new HashSet<>(lockedTickets.keySet());

		for (Long id : ids) {
			try {
				TicketDO ticket = approvalPersistenceService.getTicketById(id);

				if (ticket.getLocale().equals(locale)) {
					result.add(new TicketBO(ticket));
				}
			} catch (ApprovalPersistenceServiceException e) {
				continue;
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
		private final int RELEASE_TIME = 2;

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
			lockedTickets.entrySet().removeIf(entry -> currentTime - entry.getValue().getTime() > TimeUnit.MINUTE.getMillis(RELEASE_TIME));
		}
	}

	/**
	 * Ticket lock object.
	 */
	private static class LockedTicket {
		/**
		 * Agent id.
		 */
		private final String agentId;
		/**
		 * time.
		 */
		private final long time;

		/**
		 * Constructor.
		 *
		 * @param agentId	agent id
		 * @param time		time, when lock
		 */
		public LockedTicket(String agentId, long time) {
			this.agentId = agentId;
			this.time = time;
		}

		public String getAgentId() {
			return agentId;
		}

		public long getTime() {
			return time;
		}
	}
}
