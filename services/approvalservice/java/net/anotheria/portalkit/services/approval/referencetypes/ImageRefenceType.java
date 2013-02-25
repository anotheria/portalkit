package net.anotheria.portalkit.services.approval.referencetypes;

import net.anotheria.portalkit.services.approval.ReferenceType;

public class ImageRefenceType implements ReferenceType {

	private static final long serialVersionUID = -3699465266850742912L;

	private static final long IMAGE_REFERENCE_TYPE_ID = 2;
	
	@Override
	public long getReferenceTypeId() {
		return IMAGE_REFERENCE_TYPE_ID;
	}

	@Override
	public String toString() {
		return "ImageRefenceType [getReferenceTypeId()=" + getReferenceTypeId() + "]";
	}

}
