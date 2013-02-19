package net.anotheria.portalkit.services.accountlist.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceService;
import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

/**
 * JDBC implementation of AccountListPersistence service.
 * 
 * @author dagafonov
 * 
 */
public class JDBCAccountListPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AccountListPersistenceService {

	/**
	 * DAO definition.
	 */
	private AccountListDAO accountlistDAO;

	/**
	 * Default constructor.
	 */
	public JDBCAccountListPersistenceServiceImpl() {
		super("pk-jdbc-accountlist");
		accountlistDAO = new AccountListDAO();
		addDaos(accountlistDAO);
	}

	@Override
	public List<AccountId> getList(AccountId owner, String listName) throws AccountListPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return accountlistDAO.getList(conn, owner, listName);
		} catch (DAOException e) {
			throw new AccountListPersistenceServiceException("accountlistDAO.getList failed", e);
		} catch (SQLException e) {
			throw new AccountListPersistenceServiceException("getConnection failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public boolean addToList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return accountlistDAO.addToList(conn, owner, listName, targets);
		} catch (DAOException e) {
			throw new AccountListPersistenceServiceException("accountlistDAO.addToList failed", e);
		} catch (SQLException e) {
			throw new AccountListPersistenceServiceException("getConnection failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public boolean removeFromList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return accountlistDAO.removeFromList(conn, owner, listName, targets);
		} catch (DAOException e) {
			throw new AccountListPersistenceServiceException("accountlistDAO.removeFromList failed", e);
		} catch (SQLException e) {
			throw new AccountListPersistenceServiceException("getConnection failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public List<AccountId> getReverseList(AccountId target, String listName) throws AccountListPersistenceServiceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return accountlistDAO.getReverseList(conn, target, listName);
		} catch (DAOException e) {
			throw new AccountListPersistenceServiceException("accountlistDAO.getReverseList failed", e);
		} catch (SQLException e) {
			throw new AccountListPersistenceServiceException("getConnection failed", e);
		} finally {
			JDBCUtil.close(conn);
		}
	}

}
