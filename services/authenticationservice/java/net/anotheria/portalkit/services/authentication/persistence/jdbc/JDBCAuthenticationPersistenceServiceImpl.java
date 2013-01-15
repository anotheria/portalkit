package net.anotheria.portalkit.services.authentication.persistence.jdbc;

import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.persistence.jdbc.BasePersistenceServiceJDBCImpl;
import net.anotheria.portalkit.services.common.persistence.jdbc.DAOException;

import java.sql.SQLException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.01.13 18:06
 */
public class JDBCAuthenticationPersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements AuthenticationPersistenceService{

	PasswordDAO passwordDAO = new PasswordDAO();

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
}
