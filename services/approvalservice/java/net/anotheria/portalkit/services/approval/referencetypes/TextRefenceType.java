package net.anotheria.portalkit.services.approval.referencetypes;

import net.anotheria.portalkit.services.approval.ReferenceType;

public class TextRefenceType implements ReferenceType {

	private static final long serialVersionUID = 8790859134818849396L;
	
	private static final long TEXT_REFERENCE_TYPE_ID = 1;
	
	@Override
	public long getReferenceTypeId() {
		return TEXT_REFERENCE_TYPE_ID;
	}

	@Override
	public String toString() {
		return "TextRefenceType [getReferenceTypeId()=" + getReferenceTypeId() + "]";
	}

}
