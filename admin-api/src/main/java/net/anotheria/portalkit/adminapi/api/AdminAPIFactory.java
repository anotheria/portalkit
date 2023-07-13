package net.anotheria.portalkit.adminapi.api;

public class AdminAPIFactory {

    private static AdminAPI INSTANCE;

    public static AdminAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AdminAPIImpl();
        }
        return INSTANCE;
    }

}
