package net.anotheria.portalkit.services.record.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.portalkit.services.record.EmptyRecord;
import net.anotheria.portalkit.services.record.Record;
import net.anotheria.portalkit.services.record.RecordCollection;
import net.anotheria.portalkit.services.record.persistence.RecordPersistenceService;
import net.anotheria.portalkit.services.record.persistence.RecordPersistenceServiceException;

/**
 * 
 * @author dagafonov
 * 
 */
public class JDBCRecordPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements RecordPersistenceService {

	private RecordDAO recordDAO;

	public JDBCRecordPersistenceServiceImpl() {
		super("pk-jdbc-records");
		recordDAO = new RecordDAO();
		addDaos(recordDAO);
	}

	@Override
	public RecordCollection getCollection(String ownerId, String collectionId) throws RecordPersistenceServiceException {
		RecordCollection collection = new RecordCollection(collectionId);
		Connection conn = null;
		try {
			conn = getConnection();
			Collection<Record> records = recordDAO.readCollection(conn, ownerId, collectionId);
			collection.setRecords(records);
			return collection;
		} catch (DAOException e) {
			throw new RecordPersistenceServiceException("recordDAO.readCollection failed", e);
		} catch (SQLException e) {
			throw new RecordPersistenceServiceException("recordDAO.readCollection failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public Record getSingleRecord(String ownerId, String collectionId, String recordId) throws RecordPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			Record rec = recordDAO.getSingleRecord(conn, ownerId, collectionId, recordId);
			if (rec == null) {
				rec = new EmptyRecord(recordId);
			}
			return rec;
		} catch (DAOException e) {
			throw new RecordPersistenceServiceException("recordDAO.getSingleRecord failed", e);
		} catch (SQLException e) {
			throw new RecordPersistenceServiceException("recordDAO.getSingleRecord failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void updateCollection(String ownerId, String collectionId, RecordCollection collection) throws RecordPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			recordDAO.updateCollection(conn, ownerId, collectionId, collection);
		} catch (DAOException e) {
			throw new RecordPersistenceServiceException("recordDAO.updateCollection failed", e);
		} catch (SQLException e) {
			throw new RecordPersistenceServiceException("recordDAO.updateCollection failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void updateSingleRecord(String ownerId, String collectionId, Record record) throws RecordPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			recordDAO.updateSingleRecord(conn, ownerId, collectionId, record);
		} catch (DAOException e) {
			throw new RecordPersistenceServiceException("recordDAO.updateSingleRecord failed", e);
		} catch (SQLException e) {
			throw new RecordPersistenceServiceException("recordDAO.updateSingleRecord failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

}
