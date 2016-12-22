package net.anotheria.portalkit.services.photoscammer;

import net.anotheria.portalkit.services.photoscammer.persistence.PhotoData;

/**
 * @author Vlad Lukjanenko
 */
public class PhotoDataBO {
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
     * Photo id.
     * */
    private long photoId;


    /**
     * Default constructor.
     * */
    public PhotoDataBO() {

    }

    /**
     * Constructor.
     *
     * @param photoData     {@link PhotoData} instance.
     * */
    public PhotoDataBO(PhotoData photoData) {
        this.id = photoData.getId();
        this.userId = photoData.getUserId();
        this.photoHash = photoData.getPhotoHash();
        this.perseptiveHash = photoData.getPerseptiveHash();
        this.photoId = photoData.getPhotoId();
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

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoDataBO that = (PhotoDataBO) o;

        if (id != that.id) return false;
        if (photoHash != that.photoHash) return false;
        if (photoId != that.photoId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return perseptiveHash != null ? perseptiveHash.equals(that.perseptiveHash) : that.perseptiveHash == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + photoHash;
        result = 31 * result + (perseptiveHash != null ? perseptiveHash.hashCode() : 0);
        result = 31 * result + (int) (photoId ^ (photoId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "PhotoDataBO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", photoHash='" + photoHash + '\'' +
                ", perseptiveHash='" + perseptiveHash + '\'' +
                ", photoId=" + photoId +
                '}';
    }
}
