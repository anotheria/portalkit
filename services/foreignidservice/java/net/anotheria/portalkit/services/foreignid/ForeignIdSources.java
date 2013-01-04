package net.anotheria.portalkit.services.foreignid;

/**
 * This class contains some predefined sourceIDs for common ontology across different systems. Use large numbers
 * (starting with 1000) for custom systems.
 *
 * @author lrosenberg
 * @since 28.12.12 23:33
 */
public class ForeignIdSources {

	/**
	 * Constant used as offset for custom id to prevent id mix.
	 */
	public static final int CUSTOM_ID_OFFSET = 1000;

	public static final int FACEBOOK = 1;
	public static final int GPLUS = 2;
	public static final int LINKEDIN = 3;
	public static final int TWITTER = 4;
	public static final int XING = 5;
	//...

	public static final int getCustomIdWithOffset(int customId){
		if (customId<CUSTOM_ID_OFFSET)
			return customId;
		return CUSTOM_ID_OFFSET + customId;
	}
}
