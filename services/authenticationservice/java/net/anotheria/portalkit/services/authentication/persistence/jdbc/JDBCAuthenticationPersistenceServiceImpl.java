package net.anotheria.portalkit.services.authentication.persistence.jdbc;

import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.01.13 18:06
 */
public class JDBCAuthenticationPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AuthenticationPersistenceService{

	private PasswordDAO passwordDAO = new PasswordDAO();
	private AuthTokenDAO authTokenDAO = new AuthTokenDAO();

	public JDBCAuthenticationPersistenceServiceImpl(){
		super("pk-jdbc-auth");

		passwordDAO = new PasswordDAO();
		authTokenDAO = new AuthTokenDAO();

		addDaos(passwordDAO, authTokenDAO);
	}

	@Override
	public void saveEncryptedPassword(AccountId id, String password) throws AuthenticationPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			passwordDAO.savePassword(con, id, password);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}finally{
			release(con);
		}
	}

	@Override
	public String getEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			return passwordDAO.getPassword(con, id);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}finally{
			release(con);
		}
	}

	@Override
	public void deleteEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			passwordDAO.deletePassword(con, id);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}finally{
			release(con);
		}
	}


	@Override
	public void saveAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			authTokenDAO.saveAuthToken(con, owner, encryptedToken);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}finally{
			release(con);
		}
	}

	@Override
	public Set<String> getAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			return authTokenDAO.getAuthTokens(con, owner);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}finally{
			release(con);
		}
	}

	@Override
	public boolean authTokenExists(String encryptedToken) throws AuthenticationPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			return authTokenDAO.authTokenExists(con, encryptedToken);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}finally{
			release(con);
		}
	}

	@Override
	public void deleteAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			authTokenDAO.deleteAuthTokens(con, owner);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}finally{
			release(con);
		}
	}

	@Override
	public void deleteAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
		Connection con = null;
		try{
			con = getConnection();
			authTokenDAO.deleteAuthToken(con, owner, encryptedToken);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}finally{
			release(con);
		}
	}
}
