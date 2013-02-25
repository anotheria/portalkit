package net.anotheria.portalkit.services.approval.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.anotheria.portalkit.services.approval.ReferenceType;
import net.anotheria.portalkit.services.approval.Ticket;
import net.anotheria.portalkit.services.approval.TicketStatus;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

/**
 * 
 * @author dagafonov
 * 
 */
public class ApprovalDAO extends AbstractDAO implements DAO {

	private static final String TABLE_NAME = "ticketsapproval";
	private static final String TICKET_ID_COLUMN_NAME = "ticketid";
	private static final String STATUS_COLUMN_NAME = "status";
	private static final String REFERENCE_ID_COLUMN_NAME = "referenceid";
	private static final String REFERENCE_TYPE_COLUMN_NAME = "referencetype";

	@Override
	public void cleanupFromUnitTests(Connection connection) throws DAOException, SQLException {
		super.cleanupFromUnitTests(connection);
	}

	@Override
	protected String[] getTableNames() {
		return new String[] { TABLE_NAME };
	}

	public void createTicket(Connection conn, Ticket ticket) throws SQLException, DAOException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("insert into %s (%s, %s, %s, %s) values (?, ?, ?, ?);", TABLE_NAME, TICKET_ID_COLUMN_NAME,
					STATUS_COLUMN_NAME, REFERENCE_ID_COLUMN_NAME, REFERENCE_TYPE_COLUMN_NAME));
			stmt.setString(1, ticket.getTicketId());
			stmt.setString(2, ticket.getStatus().name());
			stmt.setString(3, ticket.getReferenceId());
			stmt.setLong(4, ticket.getReferenceType());

			int count = stmt.executeUpdate();
			if (count == 0) {
				throw new DAOException("zero inserted rows");
			}
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	public Ticket getTicketById(Connection conn, String ticketId) throws SQLException, DAOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(String.format("select %s, %s, %s from %s where %s=?", STATUS_COLUMN_NAME, REFERENCE_ID_COLUMN_NAME, REFERENCE_TYPE_COLUMN_NAME, TABLE_NAME, TICKET_ID_COLUMN_NAME));
			stmt.setString(1, ticketId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Ticket ticket = new Ticket();
				ticket.setTicketId(ticketId);
				ticket.setReferenceId(rs.getString(REFERENCE_ID_COLUMN_NAME));
				ticket.setReferenceType(rs.getLong(REFERENCE_TYPE_COLUMN_NAME));
				ticket.setStatus(TicketStatus.find(rs.getString(STATUS_COLUMN_NAME)));
				return ticket;
			}
			return null;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(stmt);
		}
	}

}
