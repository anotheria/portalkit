package net.anotheria.portalkit.services.account.persistence.jdbc;

import net.anotheria.portalkit.services.account.Account;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceService;
import net.anotheria.portalkit.services.account.persistence.AccountPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

import java.sql.Connection;
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
		addDaos(dao);
	}


	@Override
	public Account getAccount(AccountId id) throws AccountPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			return dao.getAccount(getConnection(), id);
		}catch(DAOException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}catch(SQLException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}finally{
			if (con!=null)
				release(con);
		}
	}

	@Override
	public void saveAccount(Account account) throws AccountPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			dao.saveAccount(getConnection(), account);
		}catch(DAOException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}catch(SQLException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}finally{
			if (con!=null)
				release(con);
		}
	}

	@Override
	public void deleteAccount(AccountId id) throws AccountPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			dao.deleteAccount(con, id);
		}catch(DAOException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}catch(SQLException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}finally{
			if (con!=null)
				release(con);
		}
	}

	@Override
	public AccountId getIdByName(String name) throws AccountPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			return dao.getIdByName(con, name);
		}catch(DAOException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}catch(SQLException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}finally{
			if (con!=null)
				release(con);
		}
	}

	@Override
	public AccountId getIdByEmail(String email) throws AccountPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			return dao.getIdByEmail(con, email);
		}catch(DAOException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}catch(SQLException e){
			throw new AccountPersistenceServiceException(e.getMessage(), e);
		}finally{
			if (con!=null)
				release(con);
		}
	}

}
