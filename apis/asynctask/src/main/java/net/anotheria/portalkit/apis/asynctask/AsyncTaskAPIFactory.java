package net.anotheria.portalkit.apis.asynctask;

import net.anotheria.anoplass.api.APIFactory;
import net.anotheria.portalkit.apis.asynctask.broker.AsyncTaskMessageBroker;

/**
 * @author ynikonchuk
 */
public class AsyncTaskAPIFactory implements APIFactory<AsyncTaskAPI> {

    private final AsyncTaskMessageBroker messageBroker;

    public AsyncTaskAPIFactory(AsyncTaskMessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Override
    public AsyncTaskAPI createAPI() {
        return new AsyncTaskAPIImpl(messageBroker);
    }
}
