package net.anotheria.portalkit.services.approval.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import net.anotheria.portalkit.services.approval.Ticket;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceServiceException;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

/**
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

}
