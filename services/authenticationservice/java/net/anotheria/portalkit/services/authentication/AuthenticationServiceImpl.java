package net.anotheria.portalkit.services.authentication;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceService;
import net.anotheria.portalkit.services.authentication.persistence.AuthenticationPersistenceServiceException;
import net.anotheria.portalkit.services.common.AccountId;

/**
 * Implementation of the AuthenticationService.
 *
 * @author lrosenberg
 * @since 13.12.12 09:30
 */
public class AuthenticationServiceImpl implements AuthenticationService{

	private PasswordEncryptionAlgorithm passwordAlgorithm;

	private AuthenticationPersistenceService persistenceService;

	public AuthenticationServiceImpl(){
		AuthenticationServiceConfig config = new AuthenticationServiceConfig();
		//initialize with configureme.
		try{
			passwordAlgorithm = PasswordEncryptionAlgorithm.class.cast(Class.forName(config.getPasswordAlgorithm()).newInstance());
		}catch(Exception e){
			throw new IllegalStateException("Can't operate without configured and available PasswordEncryptionAlgorithm (config="+config.getPasswordAlgorithm()+")", e);
		}

		passwordAlgorithm.customize(config.getPasswordKey());

		//note, this will work with a) only one AuthenticationPersistenceService impl or b) configured metafactory
		try{
			persistenceService = MetaFactory.get(AuthenticationPersistenceService.class);
		}catch(MetaFactoryException e){
			throw new IllegalStateException("Can't work without a persistence service", e);
		}

	}

	@Override
	public void setPassword(AccountId id, String password) throws AuthenticationServiceException {
		if (id==null)
			throw new IllegalArgumentException("id can't be null");
		try{
			persistenceService.saveEncryptedPassword(id, passwordAlgorithm.encryptPassword(password));
		}catch(AuthenticationPersistenceServiceException e){
			throw new AuthenticationServiceException(e);
		}
	}

	@Override
	public boolean canAuthenticate(AccountId id, String password) throws AuthenticationServiceException {
		if (id==null)
			throw new IllegalArgumentException("id can't be null");
		String storedEncryptedPassword = null;
		try{
			storedEncryptedPassword = persistenceService.getEncryptedPassword(id);
		}catch(AuthenticationPersistenceServiceException e){
			throw new AuthenticationServiceException(e);
		}
		return storedEncryptedPassword!=null && password != null && storedEncryptedPassword.equals(passwordAlgorithm.encryptPassword(password));
	}

	@Override
	public AccountId authenticateByEncryptedToken(String token) throws AuthenticationServiceException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public AuthToken generateEncryptedToken(AccountId accountId, AuthToken prefilledToken) throws AuthenticationServiceException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
