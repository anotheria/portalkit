package net.anotheria.portalkit.services.approval;

import java.util.Collection;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceServiceException;
import net.anotheria.portalkit.services.common.id.IdGenerator;
import net.anotheria.util.StringUtils;

/**
 * Approval service implementation.
 * 
 * @author dagafonov
 * 
 */
public class ApprovalServiceImpl implements ApprovalService {

	private ApprovalPersistenceService persistenceService;

	public ApprovalServiceImpl() {
		try {
			persistenceService = MetaFactory.get(ApprovalPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service ", e);
		}
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
		try {
			Ticket ticket = persistenceService.getTicketById(ticketId);
			if (ticket == null) {
				throw new ApprovalPersistenceServiceException("ticket not found");
			}
			return ticket;
		} catch (ApprovalPersistenceServiceException e) {
			throw new ApprovalServiceException("persistenceService.getTicketById failed.", e);
		}
	}

	@Override
	public void approveTicket(Ticket ticket) throws ApprovalServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void approveTickets(Collection<Ticket> tickets) throws ApprovalServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTicket(String ticketId) throws ApprovalServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTicket(Ticket ticket) throws ApprovalServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disapproveTicket(Ticket ticket) throws ApprovalServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disapproveTickets(Collection<Ticket> ticket) throws ApprovalServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Ticket> getAndReserveTickets(String reservationObject, int number, long referenceType) throws ApprovalServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Ticket> getReservedTickets(String reservationObject) throws ApprovalServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void proceedTickets(Collection<Ticket> toApprove, Collection<Ticket> toDisapprove) throws ApprovalServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unReserveTickets(String reservationObject) throws ApprovalServiceException {
		// TODO Auto-generated method stub

	}

}
