package net.anotheria.portalkit.services.foreignid;

import java.util.List;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceService;
import net.anotheria.portalkit.services.foreignid.persistence.ForeignIdPersistenceServiceException;

public class ForeignIdServiceImpl implements ForeignIdService {

	private ForeignIdPersistenceService persistenceService;

	public ForeignIdServiceImpl() {
		try {
			persistenceService = MetaFactory.get(ForeignIdPersistenceService.class);
		} catch (MetaFactoryException e) {
			throw new IllegalStateException("Can't start without persistence service ", e);
		}
	}

	@Override
	public void addForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException {
		try {
			persistenceService.link(accId, sourceId, foreignId);
		} catch (ForeignIdPersistenceServiceException e) {
			throw new ForeignIdServiceException("persistenceService.link failed", e);
		}
	}

	@Override
	public void removeForeignId(AccountId accId, String foreignId, int sourceId) throws ForeignIdServiceException {
		try {
			persistenceService.unlink(accId, sourceId, foreignId);
		} catch (ForeignIdPersistenceServiceException e) {
			throw new ForeignIdServiceException("persistenceService.unlink failed", e);
		}
	}

	@Override
	public AccountId getAccountIdByForeignId(String foreignId, int sourceId) throws ForeignIdServiceException {
		try {
			return persistenceService.getAccountIdByForeignId(sourceId, foreignId);
		} catch (ForeignIdPersistenceServiceException e) {
			throw new ForeignIdServiceException("persistenceService.getAccountIdByForeignId failed", e);
		}
	}

	@Override
	public List<ForeignId> getForeignIds(AccountId accountId) throws ForeignIdServiceException {
		try {
			return persistenceService.getForeignIdsByAccountId(accountId);
		} catch (ForeignIdPersistenceServiceException e) {
			throw new ForeignIdServiceException("persistenceService.getForeignIdsByAccountId failed", e);
		}
	}

}
