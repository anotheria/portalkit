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
import java.util.Iterator;
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
	private Cache<Long, TicketBO> cachedTickets;

	/**
	 * Map of locked tickets.
	 * */
	private Map<Long, Long> lockedTickets;

	/**
	 * Map of unlocked tickets by locale.
	 * */
	private Map<String, List<TicketBO>> unlockedTickets;

	/**
	 * Default constructor.
	 */
	public ApprovalServiceImpl() {

		cachedTickets = Caches.createHardwiredCache("cache-tickets");
		lockedTickets = new HashMap<>();
		unlockedTickets = new HashMap<>();

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
	public void deleteTicketByreferenceId(String referenceId) throws ApprovalServiceException {
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
				unlockedTickets.values().forEach(v -> v.removeIf(x -> x.getTicketId() == id));
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
	public List<TicketBO> getTicketsByLocale(String locale) throws ApprovalServiceException {

		List<TicketBO> result = new ArrayList<>();
		List<TicketDO> tickets = null;

		try {
			tickets = approvalPersistenceService.getTickets(locale);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while getting tickets by locale=" + locale, e);
		}

		int totalSize = tickets.size();

		for (TicketDO ticket : tickets) {

			TicketBO t = new TicketBO(ticket);
			t.setTotalAmountOfTickets(totalSize);
			result.add(t);
		}

		return result;
	}

	@Override
	public List<TicketBO> getTicketsByType(TicketType ticketType, ReferenceType referenceType) throws ApprovalServiceException {

		List<TicketBO> result = new ArrayList<>();
		List<TicketDO> tickets = null;

		try {
			tickets = approvalPersistenceService.getTickets(referenceType.getValue(), ticketType.name());
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Error occurred while getting tickets", e);
		}

		int totalSize = tickets.size();

		for (TicketDO ticket : tickets) {

			TicketBO t = new TicketBO(ticket);
			t.setTotalAmountOfTickets(totalSize);
			result.add(t);
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

		unlockTicket(ticket.getTicketId());
		cachedTickets.remove(ticket.getTicketId());
		cachedTickets.put(ticket.getTicketId(), ticket);
	}

	@Override
	public void approveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException {

		for (TicketBO ticket : tickets) {

			ticket.setType(TicketType.APPROVED);
			ticket.setStatus(TicketStatus.CLOSED);

			try {
				approvalPersistenceService.updateTicket(ticket.toDO());
			} catch (ApprovalPersistenceServiceException e) {
				continue;
			}

			unlockTicket(ticket.getTicketId());
			cachedTickets.remove(ticket.getTicketId());
			cachedTickets.put(ticket.getTicketId(), ticket);
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

		unlockTicket(ticket.getTicketId());
		cachedTickets.remove(ticket.getTicketId());
		cachedTickets.put(ticket.getTicketId(), ticket);
	}

	@Override
	public void disapproveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException {

		for (TicketBO ticket : tickets) {

			ticket.setType(TicketType.DISAPPROVED);
			ticket.setStatus(TicketStatus.CLOSED);

			try {
				approvalPersistenceService.updateTicket(ticket.toDO());
			} catch (ApprovalPersistenceServiceException e) {
				continue;
			}

			unlockTicket(ticket.getTicketId());
			cachedTickets.remove(ticket.getTicketId());
			cachedTickets.put(ticket.getTicketId(), ticket);
		}
	}

	@Override
	public void lockTicket(long ticketId, String agentId) throws ApprovalServiceException {

		TicketDO ticket = null;

		try {
			ticket = approvalPersistenceService.getTicketById(ticketId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to get ticket by id=" + ticketId, e);
		}

		ticket.setAgent(agentId);
		lockedTickets.put(ticket.getTicketId(), System.currentTimeMillis());
	}

	@Override
	public void unlockTicket(long ticketId) throws ApprovalServiceException {

		TicketDO ticket = null;

		try {
			ticket = approvalPersistenceService.getTicketById(ticketId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("Unable to get ticket by id=" + ticketId, e);
		}

		lockedTickets.remove(ticket.getTicketId());
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
	public List<TicketBO> getTickets(String locale, String agentId, int size) throws ApprovalServiceException {

		List<TicketBO> result = getLockedTickets(locale);
		List<TicketBO> unlocked = null;

		if (!result.isEmpty() && result.size() > size) {
			return result.subList(0, size);
		}

		unlocked = unlockedTickets.get(locale);

		/* try to get form persistence */
		if (unlocked == null || unlocked.isEmpty()) {
			unlocked = getTicketsByLocale(locale);
			unlockedTickets.remove(locale);
		}

		if (unlocked != null && !unlocked.isEmpty()) {

			for (Iterator<TicketBO> iterator = unlocked.iterator(); iterator.hasNext(); ) {

				TicketBO ticket = iterator.next();

				if (!result.contains(ticket) && !lockedTickets.containsKey(ticket.getTicketId())) {
					lockTicket(ticket.getTicketId(), agentId);
					result.add(ticket);
					iterator.remove();
				}

				if (result.size() == size) {
					unlockedTickets.put(locale, unlocked);
					break;
				}
			}
		}

		return result;
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
			lockedTickets.entrySet().removeIf(entry -> currentTime - entry.getValue() > TimeUnit.MINUTE.getMillis(RELEASE_TIME));
		}
	}
}
