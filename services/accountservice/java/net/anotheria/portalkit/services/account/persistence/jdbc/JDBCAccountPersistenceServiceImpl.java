package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.AccountQuery;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;
import net.anotheria.portalkit.services.common.persistence.jdbc.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * JDBC Based implementation of the account persistence service.
 * 
 * @author lrosenberg
 * @since 06.01.13 01:09
 */
@Monitor(category = "portalkit-persistence-service", subsystem = "account")
public class JDBCAccountPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AccountPersistenceService {

	/**
	 * Instance of the account dao.
	 */
	private AccountDAO dao;

	/**
	 * Constructor.
	 */
	public JDBCAccountPersistenceServiceImpl() {
		super("pk-jdbc-account");

		dao = new AccountDAO();
		addDaos(dao);
	}

	@Override
	public Account getAccount(AccountId id) throws AccountPersistenceServiceException {
		Connection con = null;
		try {
			con = getConnection();
			return dao.getAccount(con, id);
		} catch (DAOException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(con);
		}
	}

	@Override
	public void saveAccount(Account account) throws AccountPersistenceServiceException {
		Connection con = null;
		try {
			con = getConnection();
			dao.saveAccount(con, account);
		} catch (DAOException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(con);
		}
	}

	@Override
	public void deleteAccount(AccountId id) throws AccountPersistenceServiceException {
		Connection con = null;
		try {
			con = getConnection();
			dao.deleteAccount(con, id);
		} catch (DAOException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(con);
		}
	}

	@Override
	public AccountId getIdByName(String name) throws AccountPersistenceServiceException {
		Connection con = null;
		try {
			con = getConnection();
			return dao.getIdByName(con, name);
		} catch (DAOException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(con);
		}
	}

	@Override
	public AccountId getIdByEmail(String email) throws AccountPersistenceServiceException {
		Connection con = null;
		try {
			con = getConnection();
			return dao.getIdByEmail(con, email);
		} catch (DAOException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(con);
		}
	}

	@Override
	public Collection<AccountId> getAllAccountIds() throws AccountPersistenceServiceException {
		Connection con = null;
		try {
			con = getConnection();
			return dao.getAccountIds(con);
		} catch (DAOException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(con);
		}
	}

	@Override
	public List<AccountId> getAccountsByType(int accountTypeId) throws AccountPersistenceServiceException {
		Connection con = null;
		try {
			con = getConnection();
			return dao.getAccountIdsByType(con, accountTypeId);
		} catch (DAOException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(con);
		}
	}
	
	@Override
	public List<Account> getAccountsByQuery(final AccountQuery query) throws AccountPersistenceServiceException {
		Connection con = null;
		try {
			con = getConnection();
			return dao.getAccountsByQuery(con, query);
		} catch (SQLException e) {
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		} finally {
			JDBCUtil.close(con);
		}
	}

}
