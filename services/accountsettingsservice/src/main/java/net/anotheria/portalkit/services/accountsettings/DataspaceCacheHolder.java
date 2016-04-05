package net.anotheria.portalkit.services.accountsettings;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 18.03.14 23:00
 */
public class DataspaceCacheHolder {
	private ConcurrentMap<Integer, Dataspace> dataspaces = new ConcurrentHashMap<Integer, Dataspace>();

	Dataspace get(int byType){
		return dataspaces.get(byType);
	}

	void put (int type, Dataspace dataspace){
		dataspaces.put(type, dataspace);
	}

	void remove(int type) {
		dataspaces.remove(type);
	}
}
