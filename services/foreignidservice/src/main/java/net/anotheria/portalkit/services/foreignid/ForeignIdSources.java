package net.anotheria.portalkit.services.foreignid;

/**
 * This class contains some predefined sourceIDs for common ontology across
 * different systems. Use large numbers (starting with 1000) for custom systems.
 * 
 * @author lrosenberg
 * @since 28.12.12 23:33
 */
public class ForeignIdSources {

	/**
	 * Constant used as offset for custom id to prevent id mix.
	 */
	public static final int CUSTOM_ID_OFFSET = 1000;

	/**
	 * Facebook.
	 */
	public static final int FACEBOOK = 1;

	/**
	 * Google +.
	 */
	public static final int GOOGLEPLUS = 2;

	/**
	 * Linkedin.
	 */
	public static final int LINKEDIN = 3;

	/**
	 * Twitter.
	 */
	public static final int TWITTER = 4;

	/**
	 * Xing.
	 */
	public static final int XING = 5;

	/**
	 * Mobile app.
	 */
	public static final int MOBILE_NOTIFICATION = 6;

	// ...

	public static final int getCustomIdWithOffset(int customId) {
		if (customId > CUSTOM_ID_OFFSET)
			return customId;
		return CUSTOM_ID_OFFSET + customId;
	}
}
