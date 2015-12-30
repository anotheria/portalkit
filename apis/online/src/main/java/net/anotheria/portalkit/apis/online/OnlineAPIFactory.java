package net.anotheria.portalkit.apis.online;

import net.anotheria.anoplass.api.APIFactory;

/**
 * @author h3llka
 */
public class OnlineAPIFactory implements APIFactory<OnlineAPI> {
    @Override
    public OnlineAPI createAPI() {
        return new OnlineAPIImpl();
    }
}
