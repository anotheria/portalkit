package net.anotheria.portalkit.services.coin;

import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.portalkit.services.common.spring.SpringHolder;

/**
 * {@link CoinService} factory.
 */
public class CoinServiceFactory implements ServiceFactory<CoinService> {

    @Override
    public CoinService create() {
        return SpringHolder.get(CoinService.class);
    }
}
