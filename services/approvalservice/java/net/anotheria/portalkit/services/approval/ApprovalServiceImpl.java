package net.anotheria.portalkit.services.approval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceServiceException;
import net.anotheria.portalkit.services.approval.persistence.ReservationManager;
import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;
import net.anotheria.portalkit.services.common.id.IdGenerator;
import net.anotheria.util.StringUtils;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

/**
 * Approval service implementation.
 * 
 * @author dagafonov
 * 
 */
public class ApprovalServiceImpl implements ApprovalService {

	/**
	 * Update time interval.
	 */
	protected static final long TICKET_RESERVATION_LIFETIME = 1000 * 60 * 3;

	/**
	 * Persistence service.
	 */
	private ApprovalPersistenceService persistenceService;

	/**
	 * 
	 */
	private Cache<String, Ticket> cacheTickets;

	/**
	 * Id based lock manager.
	 */
	private IdBasedLockManager<String> ticketsLockManager = new SafeIdBasedLockManager<String>();

	/**
	 * 
	 */
	private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	private ReservationManager<TicketReservationKey, TicketReservationBean, Ticket> approvalReservationManager = new ReservationManager<TicketReservationKey, TicketReservationBean, Ticket>() {

		@Override
		protected TicketReservationBean construct(TicketReservationKey key, List<Ticket> tickets) {
			TicketReservationBean bean = new TicketReservationBean();
			bean.setReferenceType(key.getReferenceType());
			bean.setReservationObject(key.getReservationObject());
			bean.setTickets(tickets);
			return bean;
		}

		@Override
		protected void updateCache(TicketReservationKey key, Set<Ticket> exclude, int number) throws PortalKitPersistenceServiceException {
			Set<String> set = new HashSet<String>();
			for (Ticket t : exclude) {
				set.add(t.getTicketId());
			}
			List<Ticket> tickets = persistenceService.getTickets(set, number, key.getReferenceType());
			updateCache(tickets);
		}

		@Override
		protected void putBack(TicketReservationBean value) {
			updateCache(value.getTickets());
		}

		@Override
		protected void freeValue(TicketReservationBean value, Ticket v) {
			if (value == null || value.getTickets() == null) {
				return;
			}
			if (value.getTickets().size() > 0) {
				value.getTickets().remove(v);
			} else {
				unreserve(new TicketReservationKey(value.getReservationObject(), value.getReferenceType()));
			}
		}

		@Override
		protected void checkReservation(TicketReservationKey key, TicketReservationBean value) {
			if (System.currentTimeMillis() - value.getTimestamp() > TICKET_RESERVATION_LIFETIME) {
				unreserve(key);
			}
		}

	};

	/**
	 * Default constructor.
	 */
	public ApprovalServiceImpl() {
		cacheTickets = Caches.createHardwiredCache("cache-tickets");
		try {
			persistenceService = MetaFactory.get(ApprovalPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service ", e);
		}
		executorService.scheduleWithFixedDelay(new CheckReservationThread(), 60, 60, TimeUnit.SECONDS);
	}

	@Override
	public Ticket createTicket(String ticketReferenceId, long referenceType) throws ApprovalServiceException {
		if (StringUtils.isEmpty(ticketReferenceId)) {
			throw new IllegalArgumentException("ticket reference id is not provided...");
		}
		Ticket newTicket = new Ticket();
		newTicket.setReferenceId(ticketReferenceId);
		newTicket.setReferenceType(referenceType);
		newTicket.setStatus(TicketStatus.IN_APPROVAL);
		newTicket.setTicketId(IdGenerator.generateUniqueRandomId());
		try {
			persistenceService.createTicket(newTicket);
			return newTicket;
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("persistenceService.createTicket failed.", e);
		}
	}

	@Override
	public Ticket getTicketById(String ticketId) throws ApprovalServiceException {
		if (StringUtils.isEmpty(ticketId)) {
			throw new IllegalArgumentException("ticket id is not provided...");
		}
		Ticket ticket = cacheTickets.get(ticketId);
		if (ticket != null) {
			return ticket;
		}
		IdBasedLock<String> lock = ticketsLockManager.obtainLock(ticketId);
		lock.lock();
		try {
			ticket = persistenceService.getTicketById(ticketId);
			if (ticket == null) {
				throw new ApprovalPersistenceServiceException("ticket not found");
			}
			cacheTickets.put(ticket.getTicketId(), ticket);
			return ticket;
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("persistenceService.getTicketById failed.", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void deleteTicket(String ticketId) throws ApprovalServiceException {
		if (StringUtils.isEmpty(ticketId)) {
			throw new IllegalArgumentException("ticket id does not set...");
		}
		IdBasedLock<String> lock = ticketsLockManager.obtainLock(ticketId);
		lock.lock();
		try {
			persistenceService.deleteTicket(ticketId);
			cacheTickets.remove(ticketId);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("persistenceService.deleteTicket failed.", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void deleteTicket(Ticket ticket) throws ApprovalServiceException {
		if (ticket == null || StringUtils.isEmpty(ticket.getTicketId())) {
			throw new IllegalArgumentException("ticket or ticket id do not set...");
		}
		deleteTicket(ticket.getTicketId());
	}

	@Override
	public void approveTicket(Ticket ticket, String reservationObject) throws ApprovalServiceException {
		if (ticket == null) {
			throw new IllegalArgumentException("ticket is null...");
		}
		IdBasedLock<String> lock = ticketsLockManager.obtainLock(ticket.getTicketId());
		lock.lock();
		try {
			persistenceService.approveTicket(ticket);
			cacheTickets.remove(ticket.getTicketId());
			unreserveTicket(new TicketReservationKey(reservationObject, ticket.getReferenceType()), ticket);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("persistenceService.approveTicket failed.", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void approveTickets(Collection<Ticket> tickets, String reservationObject) throws ApprovalServiceException {
		if (tickets == null || tickets.size() == 0) {
			return;
		}
		for (Ticket ticket : tickets) {
			approveTicket(ticket, reservationObject);
		}
	}

	@Override
	public void disapproveTicket(Ticket ticket, String reservationObject) throws ApprovalServiceException {
		if (ticket == null) {
			throw new IllegalArgumentException("ticket is null...");
		}
		IdBasedLock<String> lock = ticketsLockManager.obtainLock(ticket.getTicketId());
		lock.lock();
		try {
			persistenceService.disapproveTicket(ticket);
			cacheTickets.remove(ticket.getTicketId());

			unreserveTicket(new TicketReservationKey(reservationObject, ticket.getReferenceType()), ticket);
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("persistenceService.approveTicket failed.", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void disapproveTickets(Collection<Ticket> tickets, String reservationObject) throws ApprovalServiceException {
		if (tickets == null || tickets.size() == 0) {
			return;
		}
		for (Ticket ticket : tickets) {
			disapproveTicket(ticket, reservationObject);
		}
	}

	@Override
	public void proceedTickets(Collection<Ticket> toApprove, Collection<Ticket> toDisapprove, String reservationObject)
			throws ApprovalServiceException {
		if (toApprove != null && toApprove.size() > 0) {
			approveTickets(toApprove, reservationObject);
		}
		if (toDisapprove != null && toDisapprove.size() > 0) {
			disapproveTickets(toDisapprove, reservationObject);
		}
	}

	@Override
	public Collection<Ticket> getAndReserveTickets(String reservationObject, int number, long referenceType) throws ApprovalServiceException {
		try {
			TicketReservationBean bean = approvalReservationManager.reserve(new TicketReservationKey(reservationObject, referenceType), number);
			return new ArrayList<Ticket>(bean.getTickets());
		} catch (PortalKitPersistenceServiceException e) {
			throw new ApprovalServiceException("persistenceService.getTicketsForReservation failed.", e);
		}
	}

	@Override
	public Collection<Ticket> getReservedTickets(String reservationObject, long referenceType) throws ApprovalServiceException {
		TicketReservationBean bean = approvalReservationManager.getReserved(new TicketReservationKey(reservationObject, referenceType));
		if (bean == null) {
			return new ArrayList<Ticket>();
		}
		return bean.getTickets();
	}

	@Override
	public void unreserveTickets(String reservationObject, long referenceType) throws ApprovalServiceException {
		approvalReservationManager.unreserve(new TicketReservationKey(reservationObject, referenceType));
	}

	private void unreserveTicket(TicketReservationKey key, Ticket ticket) {
		approvalReservationManager.unreserve(key, ticket);
	}

	/**
	 * Reservation checker Thread.
	 */
	public class CheckReservationThread implements Runnable {

		@Override
		public void run() {
			approvalReservationManager.checkReservation();
		}
	}

	private class TicketReservationKey {

		private long referenceType;
		private String reservationObject;

		/**
		 * Default constructor with parameters.
		 * 
		 * @param reservationObject
		 * @param referenceType
		 */
		public TicketReservationKey(String reservationObject, long referenceType) {
			this.reservationObject = reservationObject;
			this.referenceType = referenceType;
		}

		public long getReferenceType() {
			return referenceType;
		}

		public String getReservationObject() {
			return reservationObject;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (referenceType ^ (referenceType >>> 32));
			result = prime * result + ((reservationObject == null) ? 0 : reservationObject.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TicketReservationKey other = (TicketReservationKey) obj;
			if (referenceType != other.referenceType)
				return false;
			if (reservationObject == null) {
				if (other.reservationObject != null)
					return false;
			} else if (!reservationObject.equals(other.reservationObject))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TicketReservationKey [referenceType=" + referenceType + ", reservationObject=" + reservationObject + "]";
		}

	}

	private class TicketReservationBean {

		private List<Ticket> tickets;
		private long referenceType;
		private String reservationObject;
		private long timestamp = System.currentTimeMillis();

		public String getReservationObject() {
			return reservationObject;
		}

		public void setReservationObject(String reservationObject) {
			this.reservationObject = reservationObject;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public List<Ticket> getTickets() {
			return tickets;
		}

		public void setTickets(List<Ticket> tickets) {
			this.tickets = tickets;
		}

		public long getReferenceType() {
			return referenceType;
		}

		public void setReferenceType(long referenceType) {
			this.referenceType = referenceType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (referenceType ^ (referenceType >>> 32));
			result = prime * result + ((reservationObject == null) ? 0 : reservationObject.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TicketReservationBean other = (TicketReservationBean) obj;
			if (referenceType != other.referenceType)
				return false;
			if (reservationObject == null) {
				if (other.reservationObject != null)
					return false;
			} else if (!reservationObject.equals(other.reservationObject))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TicketReservationBean [tickets=" + tickets + ", referenceType=" + referenceType + ", reservationObject=" + reservationObject
					+ ", timestamp=" + timestamp + "]";
		}

	}

}
