package net.anotheria.portalkit.services.foreignid.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;
import net.anotheria.portalkit.services.foreignid.ForeignId;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceService;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceServiceException;

/**
 * 
 * @author dagafonov
 * 
 */
public class JDBCForeignIdPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements ForeignIdPersistenceService {

	private ForeignIdDAO dao;

	public JDBCForeignIdPersistenceServiceImpl() {
		super("pk-jdbc-foreignid");

		dao = new ForeignIdDAO();
		addDaos(dao);
	}

	@Override
	public AccountId getAccountIdByForeignId(int sid, String fid) throws ForeignIdPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return dao.getAccountIdByForeignId(conn, sid, fid);
		} catch (DAOException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public List<ForeignId> getForeignIdsByAccountId(AccountId accId) throws ForeignIdPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return dao.getForeignIdsByAccountId(conn, accId);
		} catch (DAOException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void link(AccountId accId, int sid, String fid) throws ForeignIdPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			dao.link(conn, accId, sid, fid);
		} catch (DAOException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void unlink(AccountId accId, int sid, String fid) throws ForeignIdPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			dao.unlink(conn, accId, sid, fid);
		} catch (DAOException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void unlink(int sid, String fid) throws ForeignIdPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			dao.unlink(conn, sid, fid);
		} catch (DAOException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

}
