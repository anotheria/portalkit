package net.anotheria.portalkit.adminapi.api;

public class AdminAPIFactory {

    private static AdminAPI INSTANCE;

    public static AdminAPI getInstanceForUnitTests() {
        return new AdminAPIImpl(true);
    }

    public static AdminAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AdminAPIImpl(false);
        }
        return INSTANCE;
    }

}
