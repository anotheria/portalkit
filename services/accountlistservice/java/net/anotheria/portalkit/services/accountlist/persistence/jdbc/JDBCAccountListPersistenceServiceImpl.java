package net.anotheria.portalkit.services.accountlist.persistence.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceService;
import net.anotheria.portalkit.services.accountlist.persistence.AccountListPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

public class JDBCAccountListPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AccountListPersistenceService {

	private AccountListDAO accountlistDAO;

	public JDBCAccountListPersistenceServiceImpl() {
		super("pk-jdbc-accountlist");
		accountlistDAO = new AccountListDAO();
		addDaos(accountlistDAO);
	}

	@Override
	public List<AccountId> getList(AccountId owner, String listName) throws AccountListPersistenceServiceException {
		try {
			return accountlistDAO.getList(getConnection(), owner, listName);
		} catch (DAOException e) {
			throw new AccountListPersistenceServiceException("accountlistDAO.getList failed", e);
		} catch (SQLException e) {
			throw new AccountListPersistenceServiceException("getConnection failed", e);
		}
	}

	@Override
	public boolean addToList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException {
		try {
			return accountlistDAO.addToList(getConnection(), owner, listName, targets);
		} catch (DAOException e) {
			throw new AccountListPersistenceServiceException("accountlistDAO.addToList failed", e);
		} catch (SQLException e) {
			throw new AccountListPersistenceServiceException("getConnection failed", e);
		}
	}
	
	@Override
	public boolean removeFromList(AccountId owner, String listName, Collection<AccountId> targets) throws AccountListPersistenceServiceException {
		try {
			return accountlistDAO.removeFromList(getConnection(), owner, listName, targets);
		} catch (DAOException e) {
			throw new AccountListPersistenceServiceException("accountlistDAO.removeFromList failed", e);
		} catch (SQLException e) {
			throw new AccountListPersistenceServiceException("getConnection failed", e);
		}
	}
	
	@Override
	public List<AccountId> getReverseList(AccountId target, String listName) throws AccountListPersistenceServiceException {
		try {
			return accountlistDAO.getReverseList(getConnection(), target, listName);
		} catch (DAOException e) {
			throw new AccountListPersistenceServiceException("accountlistDAO.getReverseList failed", e);
		} catch (SQLException e) {
			throw new AccountListPersistenceServiceException("getConnection failed", e);
		}
	}

}
