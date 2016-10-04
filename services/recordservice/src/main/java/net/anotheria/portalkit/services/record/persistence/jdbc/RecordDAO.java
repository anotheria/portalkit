package net.anotheria.portalkit.services.record.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import net.anotheria.portalkit.services.common.persistence.jdbc.AbstractDAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAO;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.portalkit.services.record.EmptyRecord;
import net.anotheria.portalkit.services.record.IntRecord;
import net.anotheria.portalkit.services.record.LongRecord;
import net.anotheria.portalkit.services.record.Record;
import net.anotheria.portalkit.services.record.RecordCollection;
import net.anotheria.portalkit.services.record.RecordType;
import net.anotheria.portalkit.services.record.StringRecord;

/**
 * Record DAO implementation.
 * 
 * @author dagafonov
 * 
 */
public class RecordDAO extends AbstractDAO implements DAO {

	/**
	 * 
	 */
	private static final String TABLE_NAME = "recordcollection";

	/**
	 * 
	 */
	private static final String OWNER_ID_COLUMN_NAME = "ownerid";
	/**
	 * 
	 */
	private static final String COLLECTION_ID_COLUMN_NAME = "collectionid";
	/**
	 * 
	 */
	private static final String RECORD_ID_COLUMN_NAME = "recordid";
	/**
	 * 
	 */
	private static final String RECORD_TYPE_COLUMN_NAME = "recordtype";
	/**
	 * 
	 */
	private static final String RECORD_VALUE_COLUMN_NAME = "recordvalue";

	@Override
	protected String[] getTableNames() {
		return new String[] { TABLE_NAME };
	}

	@Override
	public void cleanupFromUnitTests(Connection connection) throws DAOException, SQLException {
		super.cleanupFromUnitTests(connection);
	}

	private Record conctructRecord(int recordType, String recordId, String recordValue) {
		switch (RecordType.find(recordType)) {
			case INT:
				IntRecord ir = new IntRecord(recordId, Integer.valueOf(recordValue));
				return ir;
			case LONG:
				LongRecord lr = new LongRecord(recordId, Long.valueOf(recordValue));
				return lr;
			case STRING:
				StringRecord sr = new StringRecord(recordId, recordValue);
				return sr;
			case NONE:
				EmptyRecord er = new EmptyRecord(recordId);
				return er;
			default:
				// nothing to do
		}
		return new EmptyRecord(recordId);
	}

	/**
	 * Gets collection of records from storage.
	 * 
	 * @param connection database connection.
	 * @param ownerId owner id.
	 * @param collectionId collection id.
	 * @return collection of {@link Record}
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public Collection<Record> readCollection(Connection connection, String ownerId, String collectionId) throws DAOException, SQLException {
		Collection<Record> result = new ArrayList<Record>();

		PreparedStatement stat = null;
		ResultSet resultSet = null;
		try {
			stat = connection.prepareStatement(String.format("select * from %s where %s=? and %s=?", TABLE_NAME, OWNER_ID_COLUMN_NAME,
					COLLECTION_ID_COLUMN_NAME));
			stat.setString(1, ownerId);
			stat.setString(2, collectionId);
			resultSet = stat.executeQuery();
			while (resultSet.next()) {
				int recordType = resultSet.getInt(RECORD_TYPE_COLUMN_NAME);
				Record r = conctructRecord(recordType, resultSet.getString(RECORD_ID_COLUMN_NAME), resultSet.getString(RECORD_VALUE_COLUMN_NAME));
				result.add(r);
			}
			return result;
		} finally {
			JDBCUtil.close(resultSet);
			JDBCUtil.close(stat);
		}
	}

	/**
	 * Gets single record from storage.
	 * 
	 * @param conn database connection.
	 * @param ownerId owner id.
	 * @param collectionId collection id.
	 * @param recordId	record id.
	 * @return {@link Record}
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public Record getSingleRecord(Connection conn, String ownerId, String collectionId, String recordId) throws DAOException, SQLException {
		PreparedStatement stat = null;
		ResultSet resultSet = null;
		try {
			stat = conn.prepareStatement(String.format("select * from %s where %s=? and %s=? and %s=?", TABLE_NAME, OWNER_ID_COLUMN_NAME,
					COLLECTION_ID_COLUMN_NAME, RECORD_ID_COLUMN_NAME));
			stat.setString(1, ownerId);
			stat.setString(2, collectionId);
			stat.setString(3, recordId);
			resultSet = stat.executeQuery();
			if (resultSet.next()) {
				int recordType = resultSet.getInt(RECORD_TYPE_COLUMN_NAME);
				Record r = conctructRecord(recordType, recordId, resultSet.getString(RECORD_VALUE_COLUMN_NAME));
				return r;
			}
			return null;
		} finally {
			JDBCUtil.close(resultSet);
			JDBCUtil.close(stat);
		}
	}

	/**
	 * 
	 * @param connection database connection.
	 * @param ownerId owner id.
	 * @param collectionId collection id.
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	private void deleteRecordCollection(Connection connection, String ownerId, String collectionId) throws DAOException, SQLException {
		PreparedStatement deleteStmt = null;
		try {
			String prefSQL = String.format("delete from %s where %s=? and %s=?", TABLE_NAME, OWNER_ID_COLUMN_NAME, COLLECTION_ID_COLUMN_NAME);
			deleteStmt = connection.prepareStatement(prefSQL);
			deleteStmt.setString(1, ownerId);
			deleteStmt.setString(2, collectionId);
			deleteStmt.executeUpdate();
		} finally {
			JDBCUtil.close(deleteStmt);
		}
	}

	/**
	 * 
	 * @param connection database connection
	 * @param ownerId owner id.
	 * @param collectionId collection id.
	 * @param recordId	record id.
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	private void deleteSingleRecord(Connection connection, String ownerId, String collectionId, String recordId) throws DAOException, SQLException {
		PreparedStatement deleteStmt = null;
		try {
			String prefSQL = String.format("delete from %s where %s=? and %s=? and %s=?", TABLE_NAME, OWNER_ID_COLUMN_NAME,
					COLLECTION_ID_COLUMN_NAME, RECORD_ID_COLUMN_NAME);
			deleteStmt = connection.prepareStatement(prefSQL);
			deleteStmt.setString(1, ownerId);
			deleteStmt.setString(2, collectionId);
			deleteStmt.setString(3, recordId);
			deleteStmt.executeUpdate();
		} finally {
			JDBCUtil.close(deleteStmt);
		}
	}

	/**
	 * 
	 * @param conn database connection.
	 * @param ownerId owner id.
	 * @param collectionId collection id.
	 * @param collection	{@link RecordCollection}
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public void updateCollection(Connection conn, String ownerId, String collectionId, RecordCollection collection) throws DAOException, SQLException {
		PreparedStatement insertStmt = null;
		try {
			conn.setAutoCommit(false);

			deleteRecordCollection(conn, ownerId, collectionId);

			String prefSQL = String.format("insert into %s (%s, %s, %s, %s, %s) values (?, ?, ?, ?, ?)", TABLE_NAME, OWNER_ID_COLUMN_NAME,
					COLLECTION_ID_COLUMN_NAME, RECORD_ID_COLUMN_NAME, RECORD_TYPE_COLUMN_NAME, RECORD_VALUE_COLUMN_NAME);
			insertStmt = conn.prepareStatement(prefSQL);
			for (Record record : collection.getRecordSet().getRecords()) {
				insertStmt.setString(1, ownerId);
				insertStmt.setString(2, collectionId);
				insertStmt.setString(3, record.getRecordId());
				insertStmt.setInt(4, record.getType().ordinal());
				insertStmt.setString(5, record.getValueAsString());
				insertStmt.addBatch();
			}
			insertStmt.executeBatch();
			conn.setAutoCommit(true);
		} finally {
			JDBCUtil.close(insertStmt);
		}
	}

	/**
	 * 
	 * @param conn database connection.
	 * @param ownerId owner id.
	 * @param collectionId collection id.
	 * @param record	record.
	 * @throws DAOException if error.
	 * @throws SQLException if error.
	 */
	public void updateSingleRecord(Connection conn, String ownerId, String collectionId, Record record) throws DAOException, SQLException {
		PreparedStatement updateStmt = null;
		PreparedStatement insertStmt = null;
		try {
			conn.setAutoCommit(false);

			String prefSQL = String.format("update %s set %s=?, %s=? where %s=? and %s=? and %s=?", TABLE_NAME, RECORD_TYPE_COLUMN_NAME,
					RECORD_VALUE_COLUMN_NAME, OWNER_ID_COLUMN_NAME, COLLECTION_ID_COLUMN_NAME, RECORD_ID_COLUMN_NAME);
			updateStmt = conn.prepareStatement(prefSQL);
			updateStmt.setInt(1, record.getType().ordinal());
			updateStmt.setString(2, record.getValueAsString());
			updateStmt.setString(3, ownerId);
			updateStmt.setString(4, collectionId);
			updateStmt.setString(5, record.getRecordId());
			int count = updateStmt.executeUpdate();
			if (count == 0) {
				prefSQL = String.format("insert into %s (%s, %s, %s, %s, %s) values (?, ?, ?, ?, ?)", TABLE_NAME, OWNER_ID_COLUMN_NAME,
						COLLECTION_ID_COLUMN_NAME, RECORD_ID_COLUMN_NAME, RECORD_TYPE_COLUMN_NAME, RECORD_VALUE_COLUMN_NAME);
				insertStmt = conn.prepareStatement(prefSQL);
				insertStmt.setString(1, ownerId);
				insertStmt.setString(2, collectionId);
				insertStmt.setString(3, record.getRecordId());
				insertStmt.setInt(4, record.getType().ordinal());
				insertStmt.setString(5, record.getValueAsString());
				insertStmt.executeUpdate();
			}
			conn.setAutoCommit(true);
		} finally {
			JDBCUtil.close(updateStmt);
		}
	}

}
