package net.anotheria.portalkit.services.profileservice;

/**
 * @author asamoilich.
 */
public class ProfileAlreadyExistException extends ProfileServiceException {
    /**
     * Generated SerialVersionUID.
     */
    private static final long serialVersionUID = 7304132846779239438L;

    /**
     * Public constructor.
     *
     * @param uid entity _id
     */
    public ProfileAlreadyExistException(final String uid) {
        super("Profile with uid[" + uid + "] already exist.");
    }
}
