package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.portalkit.services.photoscammer.persistence.PhotoScammer;

import java.io.Serializable;

/**
 * @author Vlad Lukjanenko
 */
public class PhotoScammerBO implements Serializable {

    /**
     * Generated serial version UID.
     * */
    private static final long serialVersionUID = 8375259427156693371L;

    /**
     * Id.
     * */
    private long id;

    /**
     * User id.
     * */
    private String userId;

    /**
     * Photo hash.
     * */
    private int photoHash;

    /**
     * Photo perseptive hash.
     * */
    private String perseptiveHash;

    /**
     * Photo perseptive hash.
     * */
    private long created;


    /**
     * Default constructor.
     * */
    public PhotoScammerBO() {

    }

    /**
     * Constructor.
     *
     * @param photoScammer     {@link PhotoScammer} instance.
     * */
    public PhotoScammerBO(PhotoScammer photoScammer) {

        this.id = photoScammer.getId();
        this.userId = photoScammer.getUserId();
        this.photoHash = photoScammer.getPhotoHash();
        this.perseptiveHash = photoScammer.getPerseptiveHash();
        this.created = photoScammer.getCreated();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPhotoHash() {
        return photoHash;
    }

    public void setPhotoHash(int photoHash) {
        this.photoHash = photoHash;
    }

    public String getPerseptiveHash() {
        return perseptiveHash;
    }

    public void setPerseptiveHash(String perseptiveHash) {
        this.perseptiveHash = perseptiveHash;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoScammerBO that = (PhotoScammerBO) o;

        if (id != that.id) return false;
        if (photoHash != that.photoHash) return false;
        if (created != that.created) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return perseptiveHash != null ? perseptiveHash.equals(that.perseptiveHash) : that.perseptiveHash == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + photoHash;
        result = 31 * result + (perseptiveHash != null ? perseptiveHash.hashCode() : 0);
        result = 31 * result + (int) (created ^ (created >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "PhotoScammerBO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", photoHash='" + photoHash + '\'' +
                ", perseptiveHash='" + perseptiveHash + '\'' +
                ", created=" + created +
                '}';
    }
}
