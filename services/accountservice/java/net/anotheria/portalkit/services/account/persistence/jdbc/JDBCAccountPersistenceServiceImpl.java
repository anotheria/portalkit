package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

import java.sql.SQLException;


/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 06.01.13 01:09
 */
public class JDBCAccountPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AccountPersistenceService{

	private AccountDAO dao;

	public JDBCAccountPersistenceServiceImpl(){
		super("pk-jdbc-account");

		dao = new AccountDAO();
	}


	@Override
	public Account getAccount(AccountId id) throws AccountPersistenceServiceException {
		try{
			return dao.getAccount(getConnection(), id);
		}catch(DAOException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}catch(SQLException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void saveAccount(Account account) throws AccountPersistenceServiceException {
		try{
			dao.saveAccount(getConnection(), account);
		}catch(DAOException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}catch(SQLException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteAccount(AccountId id) throws AccountPersistenceServiceException {
		try{
			dao.deleteAccount(getConnection(), id);
		}catch(DAOException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}catch(SQLException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public AccountId getIdByName(String name) throws AccountPersistenceServiceException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public AccountId getIdByEmail(String name) throws AccountPersistenceServiceException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public void cleanupFromUnitTests() throws Exception {
		dao.cleanupFromUnitTests(getConnection());
	}
}
