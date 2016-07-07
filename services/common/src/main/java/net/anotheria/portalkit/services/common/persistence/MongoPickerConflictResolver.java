package net.anotheria.portalkit.services.common.persistence;

import net.anotheria.anoprise.metafactory.OnTheFlyConflictResolver;

import java.util.Collection;

/**
 * @author Roman Stetsiuk
 */
public class MongoPickerConflictResolver implements OnTheFlyConflictResolver {
	@Override
	public <T> Class<? extends T> resolveConflict(Collection<Class<? extends T>> classes) {
		for (Class<? extends T> clazz : classes){
			if (clazz.getSimpleName().toLowerCase().indexOf("mongo")!=-1)
				return clazz;
		}
		return null;
	}

}
