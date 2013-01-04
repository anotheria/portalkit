package net.anotheria.portalkit.services.foreignid;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 28.12.12 23:31
 */
public class ForeignId implements Serializable {
	/**
	 * The source id, this is dependent on the concrete source.
	 */
	private String id;
	/**
	 * The id of the source. This is application specific. However, we have collected some common sourceids in
	 * ForeignIdSources class.
	 */
	private int sourceId;

	/**
	 * The account id in our system
	 */
	AccountId accountId;

}
