package net.anotheria.portalkit.services.profileservice;

import java.io.Serializable;

/**
 * @author asamoilich.
 */
public abstract class Profile implements Serializable {

    private static final long serialVersionUID = 6511210742735193227L;

    protected String _id;

    protected Profile() {
    }

    protected Profile(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
