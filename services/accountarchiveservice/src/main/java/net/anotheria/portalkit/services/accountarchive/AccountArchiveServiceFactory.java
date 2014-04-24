package net.anotheria.portalkit.services.accountarchive;


import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * @author VKoulakov
 * @since 21.04.14 19:02
 */
public class AccountArchiveServiceFactory implements ServiceFactory<AccountArchiveService> {
    @Override
    public AccountArchiveService create() {
        return new AccountArchiveServiceImpl();
    }
}
