package net.anotheria.portalkit.services.foreignid.persistence.jdbc;

import java.sql.SQLException;
import java.util.List;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
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
		try {
			return dao.getAccountIdByForeignId(getConnection(), sid, fid);
		} catch (DAOException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<ForeignId> getForeignIdsByAccountId(AccountId accId) throws ForeignIdPersistenceServiceException {
		try {
			return dao.getForeignIdsByAccountId(getConnection(), accId);
		} catch (DAOException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void link(AccountId accId, int sid, String fid) throws ForeignIdPersistenceServiceException {
		try {
			dao.link(getConnection(), accId, sid, fid);
		} catch (DAOException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ForeignIdPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void unlink(AccountId accountId, int sid, String fid) throws ForeignIdPersistenceServiceException {

	}

	@Override
	public void unlink(int sid, String fid) throws ForeignIdPersistenceServiceException {

	}

}
