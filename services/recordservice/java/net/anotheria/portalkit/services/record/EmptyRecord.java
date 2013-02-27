package net.anotheria.portalkit.services.record;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.02.13 09:20
 */
public final class EmptyRecord extends Record {

	public EmptyRecord(String aRecordId){
		super(aRecordId);
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
}
