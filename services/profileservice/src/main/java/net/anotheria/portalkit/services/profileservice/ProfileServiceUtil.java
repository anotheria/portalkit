package net.anotheria.portalkit.services.profileservice;

import com.mongodb.ServerAddress;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author asamoilich.
 */
public class ProfileServiceUtil {
    /**
     * Default constructor.
     */
    private ProfileServiceUtil() {
        throw new IllegalAccessError();
    }

    /**
     * Get configured addresses for mongo client.
     *
     * @param configuration mongo client configuration
     * @return {@link java.util.List} of {@link com.mongodb.ServerAddress}
     */
    public static List<ServerAddress> getAddresses(final ProfileServiceConfig configuration) {
        List<ServerAddress> result = new ArrayList<ServerAddress>();

        for (ProfileServiceConfig.Host host : configuration.getHosts())
            result.add(new ServerAddress(new InetSocketAddress(host.getHost(), host.getPort())));

        return result;
    }
}
