package net.anotheria.portalkit.services.profileservice;

/**
 * @author asamoilich.
 */
public class ProfileNotFoundException extends ProfileServiceException {
    /**
     * Generated SerialVersionUID.
     */
    private static final long serialVersionUID = -7343082391442105443L;

    /**
     * Public constructor.
     *
     * @param uid
     *            entity _id
     */
    public ProfileNotFoundException(final String uid) {
        super("Profile with uid[" + uid + "] not found.");
    }
}
