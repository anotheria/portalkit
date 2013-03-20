package net.anotheria.portalkit.services.approval.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import net.anotheria.portalkit.services.common.exceptions.PortalKitPersistenceServiceException;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

/**
 * Stores reservation information.
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
	private BlockingQueue<T> inApprovalCache = new LinkedBlockingQueue<T>(MAX_INAPPROVAL_TICKETS_IN_THE_CACHE);

	/**
	 * Reservation storage.
	 */
	private ConcurrentHashMap<K, V> reservation = new ConcurrentHashMap<K, V>();

	/**
	 * Lock per reservationObject.
	 */
	private IdBasedLockManager<K> lockManager = new SafeIdBasedLockManager<K>();

	/**
	 * Reserves value.
	 * 
	 * @param key
	 * @param number
	 * @return
	 * @throws PortalKitPersistenceServiceException
	 */
	public V reserve(K key, int number) throws PortalKitPersistenceServiceException {
		IdBasedLock<K> lock = lockManager.obtainLock(key);
		lock.lock();
		try {
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
			lock.unlock();
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
				updateCache(key, set, MAX_INAPPROVAL_TICKETS_IN_THE_CACHE);
			}
			if (number > inApprovalCache.size()) {
				number = inApprovalCache.size();
			}
			fillCargo(cargo, number);
		}
	}

	/**
	 * Removes <T> from cache and fill
	 * 
	 * @param cargo
	 * @param number
	 */
	private void fillCargo(List<T> cargo, int number) {
		for (int i = 0; i < number; i++) {
			T t = inApprovalCache.poll();
			if (t != null) {
				cargo.add(t);
			}
		}
	}

	/**
	 * Gets reservation value.
	 * 
	 * @param key
	 * @return
	 */
	public V getReserved(K key) {
		return reservation.get(key);
	}

	/**
	 * Unreserves full block.
	 * 
	 * @param key
	 * @return
	 */
	public V unreserve(K key) {
		IdBasedLock<K> lock = lockManager.obtainLock(key);
		lock.lock();
		try {
			V value = reservation.remove(key);
			putBack(value);
		} finally {
			lock.unlock();
		}
		return null;
	}

	protected void updateCache(List<T> values) {
		for (T t : values) {
			inApprovalCache.offer(t);
		}
	}

	/**
	 * Unreserves one.
	 * 
	 * @param key
	 * @param value
	 */
	public void unreserve(K key, T value) {
		IdBasedLock<K> lock = lockManager.obtainLock(key);
		lock.lock();
		try {
			V container = reservation.get(key);
			if (container != null) {
				freeValue(container, value);
			}
		} finally {
			lock.unlock();
		}
	}

	public void checkReservation() {
		for (K key : reservation.keySet()) {
			IdBasedLock<K> lock = lockManager.obtainLock(key);
			lock.lock();
			try {
				checkReservation(key, reservation.get(key));
			} finally {
				lock.unlock();
			}
		}
	}

	protected abstract void freeValue(V value, T v);

	protected abstract void updateCache(K key, Set<T> exclude, int count) throws PortalKitPersistenceServiceException;

	protected abstract V construct(K key, List<T> cargo);

	protected abstract void putBack(V value);

	protected abstract void checkReservation(K key, V v);

}
