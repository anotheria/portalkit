package net.anotheria.portalkit.services.approval.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.anotheria.portalkit.services.approval.Ticket;
import net.anotheria.portalkit.services.approval.TicketStatus;
import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

/**
 * Approval DAO.
 * 
 * @author dagafonov
 * 
 */
public class ApprovalDAO extends AbstractDAO implements DAO {

	/**
	 * 
	 */
	private static final String TABLE_NAME = "ticketsapproval";
	/**
	 * 
	 */
	private static final String TICKET_ID_COLUMN_NAME = "ticketid";
	/**
	 * 
	 */
	private static final String STATUS_COLUMN_NAME = "status";
	/**
	 * 
	 */
	private static final String REFERENCE_ID_COLUMN_NAME = "referenceid";
	/**
	 * 
	 */
	private static final String REFERENCE_TYPE_COLUMN_NAME = "referencetype";
	/**
	 * 
	 */
	private static final String TIMESTAMP_COLUMN_NAME = "ts";
	/**
	 * 
	 */
	private static final String UPDATE_STATUS_SQL = "update %s set %s=? where %s=?;";

	@Override
	public void cleanupFromUnitTests(Connection connection) throws DAOException, SQLException {
		super.cleanupFromUnitTests(connection);
	}

	@Override
	protected String[] getTableNames() {
		return new String[] { TABLE_NAME };
	}

	/**
	 * Creates {@link Ticket}.
	 * @param conn
	 * @param ticket
	 * @throws SQLException
	 * @throws DAOException
	 */
	public void createTicket(Connection conn, Ticket ticket) throws SQLException, DAOException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("insert into %s (%s, %s, %s, %s, %s) values (?, ?, ?, ?, ?);", TABLE_NAME,
					TICKET_ID_COLUMN_NAME, STATUS_COLUMN_NAME, REFERENCE_ID_COLUMN_NAME, REFERENCE_TYPE_COLUMN_NAME, TIMESTAMP_COLUMN_NAME));
			stmt.setString(1, ticket.getTicketId());
			stmt.setString(2, ticket.getStatus().name());
			stmt.setString(3, ticket.getReferenceId());
			stmt.setLong(4, ticket.getReferenceType());
			stmt.setLong(5, System.currentTimeMillis());

			int count = stmt.executeUpdate();
			if (count == 0) {
				throw new DAOException("zero inserted rows");
			}
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	/**
	 * Gets {@link Ticket} by id.
	 * @param conn
	 * @param ticketId
	 * @return {@link Ticket}
	 * @throws SQLException
	 * @throws DAOException
	 */
	public Ticket getTicketById(Connection conn, String ticketId) throws SQLException, DAOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(String.format("select %s, %s, %s, %s, %s from %s where %s=?", TICKET_ID_COLUMN_NAME, STATUS_COLUMN_NAME,
					REFERENCE_ID_COLUMN_NAME, REFERENCE_TYPE_COLUMN_NAME, TIMESTAMP_COLUMN_NAME, TABLE_NAME, TICKET_ID_COLUMN_NAME));
			stmt.setString(1, ticketId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Ticket ticket = new Ticket();
				ticket.setTicketId(rs.getString(TICKET_ID_COLUMN_NAME));
				ticket.setReferenceId(rs.getString(REFERENCE_ID_COLUMN_NAME));
				ticket.setReferenceType(rs.getLong(REFERENCE_TYPE_COLUMN_NAME));
				ticket.setStatus(TicketStatus.find(rs.getString(STATUS_COLUMN_NAME)));
				ticket.setTimestamp(rs.getLong(TIMESTAMP_COLUMN_NAME));
				return ticket;
			}
			return null;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(stmt);
		}
	}

	/**
	 * Updates {@link Ticket}.
	 * @param conn
	 * @param ticket
	 * @param newStatus
	 * @throws SQLException
	 * @throws DAOException
	 */
	public void updateStatusOne(Connection conn, Ticket ticket, TicketStatus newStatus) throws SQLException, DAOException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format(UPDATE_STATUS_SQL, TABLE_NAME, STATUS_COLUMN_NAME, TICKET_ID_COLUMN_NAME));
			stmt.setString(1, newStatus.name());
			stmt.setString(2, ticket.getTicketId());
			int count = stmt.executeUpdate();
			if (count == 0) {
				throw new DAOException("zero updated rows");
			}
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	/**
	 * 
	 * @param conn
	 * @param tickets
	 * @param newStatus
	 * @throws SQLException
	 * @throws DAOException
	 */
	public void updateStatusMany(Connection conn, Collection<Ticket> tickets, TicketStatus newStatus) throws SQLException, DAOException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format(UPDATE_STATUS_SQL, TABLE_NAME, STATUS_COLUMN_NAME, TICKET_ID_COLUMN_NAME));
			for (Ticket t : tickets) {
				stmt.setString(1, newStatus.name());
				stmt.setString(2, t.getTicketId());
				stmt.addBatch();
			}
			stmt.executeBatch();
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	/**
	 * Deletes {@link Ticket} by id.
	 * @param conn
	 * @param ticketId
	 * @throws SQLException
	 * @throws DAOException
	 */
	public void deleteTicket(Connection conn, String ticketId) throws SQLException, DAOException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("delete from %s where %s=?;", TABLE_NAME, TICKET_ID_COLUMN_NAME));
			stmt.setString(1, ticketId);
			int count = stmt.executeUpdate();
			if (count == 0) {
				throw new DAOException("there is nothing to delete");
			}
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	/**
	 * 
	 * @param conn
	 * @param ticketsIds
	 * @param number
	 * @param referenceType
	 * @return {@link List<Ticket>}
	 * @throws SQLException
	 * @throws DAOException
	 */
	public List<Ticket> getTicketsByNumberReferenceType(Connection conn, Set<String> ticketsIds, int number, long referenceType) throws SQLException,
			DAOException {
		StringBuilder b = new StringBuilder();
		if (ticketsIds.size() > 0) {
			b.append("and " + TICKET_ID_COLUMN_NAME + " not in (");
			String[] ts = ticketsIds.toArray(new String[] {});
			for (int i = 0; i < ticketsIds.size(); i++) {
				String ids = ts[i];
				b.append("?");
				if (i < ticketsIds.size() - 1) {
					b.append(",");
				}
			}
			b.append(")");
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Ticket> result = new ArrayList<Ticket>();
		try {
			String sql = String.format("select %s, %s, %s, %s, %s from %s where %s=? and %s=? " + b.toString() + " order by %s desc ;",
					TICKET_ID_COLUMN_NAME, STATUS_COLUMN_NAME, REFERENCE_ID_COLUMN_NAME, REFERENCE_TYPE_COLUMN_NAME, TIMESTAMP_COLUMN_NAME,
					TABLE_NAME, REFERENCE_TYPE_COLUMN_NAME, STATUS_COLUMN_NAME, TIMESTAMP_COLUMN_NAME);

			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, referenceType);
			stmt.setString(2, TicketStatus.IN_APPROVAL.name());

			if (ticketsIds.size() > 0) {
				String[] ts = ticketsIds.toArray(new String[] {});
				for (int i = 0; i < ticketsIds.size(); i++) {
					String ids = ts[i];
					stmt.setString(i + 3, ids);
				}
			}

			// stmt.setMaxRows(number);
			rs = stmt.executeQuery();
			while (rs.next() && result.size() < number) {
				Ticket ticket = new Ticket();
				ticket.setTicketId(rs.getString(TICKET_ID_COLUMN_NAME));
				ticket.setStatus(TicketStatus.find(rs.getString(STATUS_COLUMN_NAME)));
				ticket.setReferenceId(rs.getString(REFERENCE_ID_COLUMN_NAME));
				ticket.setReferenceType(rs.getLong(REFERENCE_TYPE_COLUMN_NAME));
				ticket.setTimestamp(rs.getLong(TIMESTAMP_COLUMN_NAME));
				result.add(ticket);
			}
			return result;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(stmt);
		}
	}

	/**
	 * 
	 * @param conn
	 * @param status
	 * @param referenceType
	 * @return {@link List<Ticket>}
	 */
	public List<Ticket> getTickets(Connection conn, TicketStatus status, long referenceType) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Ticket> result = new ArrayList<Ticket>();
		try {
			stmt = conn.prepareStatement(String.format("select %s, %s, %s, %s, %s from %s where %s=? and %s=?", TICKET_ID_COLUMN_NAME,
					STATUS_COLUMN_NAME, REFERENCE_ID_COLUMN_NAME, REFERENCE_TYPE_COLUMN_NAME, TIMESTAMP_COLUMN_NAME, TABLE_NAME, STATUS_COLUMN_NAME,
					REFERENCE_TYPE_COLUMN_NAME));
			stmt.setString(1, status.name());
			stmt.setLong(2, referenceType);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Ticket ticket = new Ticket();
				ticket.setTicketId(rs.getString(TICKET_ID_COLUMN_NAME));
				ticket.setReferenceId(rs.getString(REFERENCE_ID_COLUMN_NAME));
				ticket.setReferenceType(rs.getLong(REFERENCE_TYPE_COLUMN_NAME));
				ticket.setStatus(TicketStatus.find(rs.getString(STATUS_COLUMN_NAME)));
				ticket.setTimestamp(rs.getLong(TIMESTAMP_COLUMN_NAME));
				result.add(ticket);
			}
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(stmt);
		}
		return result;
	}
}
