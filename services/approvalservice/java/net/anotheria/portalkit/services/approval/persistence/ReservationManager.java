package net.anotheria.portalkit.services.approval.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;

/**
 * 
 * @author dagafonov
 * 
 * @param <K>
 *            (TicketReservationKey)
 * @param <V>
 *            (TicketReservationBean)
 * @param <T>
 *            (Ticket)
 */
public abstract class ReservationManager<K, V, T> {

	/**
	 * 
	 */
	private static final int MAX_INAPPROVAL_TICKETS_IN_THE_CACHE = 100;

	/**
	 * Queue for cache.
	 */
	private BlockingQueue<T> inApprovalCache = new LinkedBlockingQueue<T>();

	/**
	 * Lock instance.
	 */
	private ReentrantLock reservationLock = new ReentrantLock();

	/**
	 * Reservation process. As key ticket id (String), as value
	 * TicketReservationBean with reservation object and timestamp.
	 */
	private ConcurrentHashMap<K, V> reservation = new ConcurrentHashMap<K, V>();

	public V reserve(K key, int number) throws PortalKitPersistenceServiceException {
		try {
			reservationLock.lock();
			V value = reservation.get(key);
			if (value == null) {
				List<T> cargo = new ArrayList<T>();
				getCargo(key, number, cargo);
				value = construct(key, cargo);
				reservation.put(key, value);
			} else {
				unreserve(key);
				value = reserve(key, number);
			}
			return value;
		} finally {
			reservationLock.unlock();
		}
	}

	private void getCargo(K key, int number, List<T> cargo) throws PortalKitPersistenceServiceException {
		if (number > MAX_INAPPROVAL_TICKETS_IN_THE_CACHE) {
			if (inApprovalCache.size() == 0) {
				Set<T> set = new HashSet<T>(inApprovalCache);
				set.addAll(cargo);
				updateCache(key, set, MAX_INAPPROVAL_TICKETS_IN_THE_CACHE);
			}
			int was = inApprovalCache.size();
			if (was != 0) {
				fillCargo(cargo, was);
				getCargo(key, number - was, cargo);
			}
		} else {
			if (inApprovalCache.size() == 0) {
				Set<T> set = new HashSet<T>(inApprovalCache);
				set.addAll(cargo);
				updateCache(key, new HashSet<T>(inApprovalCache), MAX_INAPPROVAL_TICKETS_IN_THE_CACHE);
			}
			if (number > inApprovalCache.size()) {
				number = inApprovalCache.size();
			}
			fillCargo(cargo, number);
		}
	}

	private void fillCargo(List<T> cargo, int number) {
		for (int i = 0; i < number; i++) {
			T t = inApprovalCache.poll();
			if (t != null) {
				cargo.add(t);
			}
		}
	}

	public V getReserved(K key) {
		return reservation.get(key);
	}

	public V unreserve(K key) {
		try {
			reservationLock.lock();
			V value = reservation.remove(key);
			putBack(value);
		} finally {
			reservationLock.unlock();
		}
		return null;
	}

	protected void updateCache(List<T> values) {
		for (T t : values) {
			inApprovalCache.offer(t);
		}
	}

	public void free(K key, T v) {
		try {
			reservationLock.lock();
			V value = reservation.get(key);
			if (value != null) {
				freeValue(value, v);
			}
		} finally {
			reservationLock.unlock();
		}
	}

	protected abstract void freeValue(V value, T v);

	protected abstract void updateCache(K key, Set<T> exclude, int count) throws PortalKitPersistenceServiceException;

	protected abstract V construct(K key, List<T> cargo);

	protected abstract void putBack(V value);

}
