package net.anotheria.portalkit.services.record;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.02.13 09:20
 */
public final class EmptyRecord extends Record {

	private static final long serialVersionUID = 4215794916579677723L;

	public EmptyRecord(String aRecordId){
		super(aRecordId);
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public String getValueAsString() {
		return "";
	}

	//TODO this is actually to be discussed, because returning different type from expected in a number of cases.
	//however, actually asking type of a non existing element is already an error ;-).
	@Override
	public RecordType getType() {
		return RecordType.NONE;
	}
}
