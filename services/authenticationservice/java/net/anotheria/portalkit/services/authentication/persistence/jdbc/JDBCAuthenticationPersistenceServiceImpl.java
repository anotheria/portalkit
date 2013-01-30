package net.anotheria.portalkit.services.authentication.persistence.jdbc;

import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

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

	public JDBCAuthenticationPersistenceServiceImpl(){
		super("pk-jdbc-auth");

		passwordDAO = new PasswordDAO();
		addDaos(passwordDAO);
	}

	@Override
	public void saveEncryptedPassword(AccountId id, String password) throws AuthenticationPersistenceServiceException {
		try{
			passwordDAO.savePassword(getConnection(), id, password);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public String getEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		try{
			return passwordDAO.getPassword(getConnection(), id);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteEncryptedPassword(AccountId id) throws AuthenticationPersistenceServiceException {
		try{
			passwordDAO.deletePassword(getConnection(), id);
		}catch(SQLException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}catch(DAOException e){
			throw new AuthenticationPersistenceServiceException(e.getMessage(), e);
		}
	}


	@Override
	public void saveAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Set<String> getAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean authTokenExists(String encryptedToken) throws AuthenticationPersistenceServiceException {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void deleteAuthTokens(AccountId owner) throws AuthenticationPersistenceServiceException {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void deleteAuthToken(AccountId owner, String encryptedToken) throws AuthenticationPersistenceServiceException {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
