package net.anotheria.portalkit.services.approval.persistence;

import java.util.List;

import net.anotheria.moskito.aop.annotation.Monitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * JDBC implementation of ApprovalPersistenceService.
 * 
 * @author Vlad Lukjanenko
 */
@Service
@Transactional
@Monitor(subsystem = "approval", category = "portalkit-service")
public class ApprovalPersistenceServiceImpl implements ApprovalPersistenceService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public TicketDO createTicket(TicketDO newTicket) throws ApprovalPersistenceServiceException {
		entityManager.persist(newTicket);
		return newTicket;
	}

	@Override
	public TicketDO getTicketById(long ticketId) throws ApprovalPersistenceServiceException {

		TypedQuery<TicketDO> q = entityManager.createNamedQuery(TicketDO.GET_TICKET_BY_ID, TicketDO.class);
		q.setParameter("ticketId", ticketId);

		List<TicketDO> tickets = q.getResultList();

		if (tickets == null || tickets.isEmpty()) {
			throw new ApprovalPersistenceServiceException("Ticket not found");
		}

		return tickets.get(0);
	}

	@Override
	public void updateTicket(TicketDO ticket) throws ApprovalPersistenceServiceException {
		entityManager.merge(ticket);
	}

	@Override
	public void deleteTicket(long ticketId) throws ApprovalPersistenceServiceException {

		Query query = entityManager.createNamedQuery(TicketDO.DELETE_TICKET_BY_ID)
				.setParameter("ticketId", ticketId);

		query.executeUpdate();
	}

	@Override
	public List<TicketDO> getTickets(long referenceType, String ticketType) throws ApprovalPersistenceServiceException {

		TypedQuery<TicketDO> q = entityManager.createNamedQuery(TicketDO.GET_TICKETS_BY_TYPE, TicketDO.class);
		q.setParameter("referenceType", referenceType);
		q.setParameter("ticketType", ticketType);

		List<TicketDO> tickets = q.getResultList();

		if (tickets == null) {
			throw new ApprovalPersistenceServiceException("Tickets not found");
		}

		return tickets;
	}

	@Override
	public List<TicketDO> getTickets(String locale) throws ApprovalPersistenceServiceException {

		TypedQuery<TicketDO> q = entityManager.createNamedQuery(TicketDO.GET_TICKETS_BY_LOCALE, TicketDO.class);
		q.setParameter("locale", locale);

		List<TicketDO> tickets = q.getResultList();

		if (tickets == null) {
			throw new ApprovalPersistenceServiceException("Tickets not found");
		}

		return tickets;
	}
}
