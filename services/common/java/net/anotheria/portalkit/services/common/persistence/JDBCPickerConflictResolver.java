package net.anotheria.portalkit.services.common.persistence;

import net.anotheria.anoprise.metafactory.OnTheFlyConflictResolver;

import java.util.Collection;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 07.01.13 23:54
 */
public class JDBCPickerConflictResolver implements OnTheFlyConflictResolver {
	@Override
	public <T> Class<? extends T> resolveConflict(Collection<Class<? extends T>> classes) {
		for (Class <? extends T> clazz : classes){
			if (clazz.getSimpleName().toLowerCase().indexOf("jdbc")!=-1)
				return clazz;
		}
		return null;
	}
}
