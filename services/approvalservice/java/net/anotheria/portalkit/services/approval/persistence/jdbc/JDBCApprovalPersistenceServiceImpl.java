package net.anotheria.portalkit.services.approval.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.anotheria.portalkit.services.approval.Ticket;
import net.anotheria.portalkit.services.approval.TicketStatus;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceServiceException;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

/**
 * JDBC implementation of ApprovalPersistenceService.
 * 
 * @author dagafonov
 */
public class JDBCApprovalPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements ApprovalPersistenceService {

	private ApprovalDAO approvalDAO;

	public JDBCApprovalPersistenceServiceImpl() {
		super("pk-jdbc-approval");
		approvalDAO = new ApprovalDAO();
		addDaos(approvalDAO);
	}

	@Override
	public void createTicket(Ticket newTicket) throws ApprovalPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			approvalDAO.createTicket(conn, newTicket);
		} catch (SQLException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.createTicket failed", e);
		} catch (DAOException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.createTicket failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public Ticket getTicketById(String ticketId) throws ApprovalPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return approvalDAO.getTicketById(conn, ticketId);
		} catch (SQLException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} catch (DAOException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void approveTicket(Ticket ticket) throws ApprovalPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			approvalDAO.updateStatusOne(conn, ticket, TicketStatus.APPROVED);
		} catch (SQLException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} catch (DAOException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void approveTickets(Collection<Ticket> tickets) throws ApprovalPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			approvalDAO.updateStatusMany(conn, tickets, TicketStatus.APPROVED);
		} catch (SQLException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} catch (DAOException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void disapproveTicket(Ticket ticket) throws ApprovalPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			approvalDAO.updateStatusOne(conn, ticket, TicketStatus.DISAPPROVED);
		} catch (SQLException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} catch (DAOException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void disapproveTickets(Collection<Ticket> tickets) throws ApprovalPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			approvalDAO.updateStatusMany(conn, tickets, TicketStatus.DISAPPROVED);
		} catch (SQLException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} catch (DAOException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void deleteTicket(String ticketId) throws ApprovalPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			approvalDAO.deleteTicket(conn, ticketId);
		} catch (SQLException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} catch (DAOException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public List<Ticket> getTickets(Set<String> keySet, int number, long referenceType) throws ApprovalPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			List<Ticket> tickets = approvalDAO.getTicketsByNumberReferenceType(conn, keySet, number, referenceType);
			return tickets;
		} catch (SQLException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} catch (DAOException e) {
			throw new ApprovalPersistenceServiceException("approvalDAO.getTicketById failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	// @Override
	// public void printStatistics(long referenceType) {
	// Connection conn = null;
	// try {
	// conn = getConnection();
	// List<Ticket> approved = approvalDAO.getTickets(conn,
	// TicketStatus.APPROVED, referenceType);
	// List<Ticket> disapproved = approvalDAO.getTickets(conn,
	// TicketStatus.DISAPPROVED, referenceType);
	// List<Ticket> inapproval = approvalDAO.getTickets(conn,
	// TicketStatus.IN_APPROVAL, referenceType);
	//
	// System.out.println("approved=" + approved.size() + ", disapproved=" +
	// disapproved.size() + ", inapproval=" + inapproval.size());
	//
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// } finally {
	// JDBCUtil.close(conn);
	// }
	// }

}
