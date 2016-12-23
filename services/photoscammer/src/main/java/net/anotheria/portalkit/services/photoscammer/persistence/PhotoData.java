package net.anotheria.portalkit.services.photoscammer.persistence;

import net.anotheria.portalkit.services.photoscammer.PhotoDataBO;

import javax.persistence.*;

/**
 * @author Vlad Lukjanenko
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "photodata")
@NamedQueries({
        @NamedQuery(
                name = PhotoData.JPQL_GET_ALL_PHOTO_DATA,
                query = "select p from PhotoData p"
        ),
        @NamedQuery(
                name = PhotoData.JPQL_GET_ALL_PHOTO_DATA_BY_USER,
                query = "select p from PhotoData p where p.userId = :userId"
        ),
        @NamedQuery(
                name = PhotoData.JPQL_GET_ALL_PHOTO_DATA_BY_PHOTO_ID,
                query = "select p from PhotoData p where p.photoId = :photoId"
        )
})
public class PhotoData {

    public static final String JPQL_GET_ALL_PHOTO_DATA = "PhotoData.getAllPhotoData";
    public static final String JPQL_GET_ALL_PHOTO_DATA_BY_USER = "PhotoData.getAllPhotoDataByUser";
    public static final String JPQL_GET_ALL_PHOTO_DATA_BY_PHOTO_ID = "PhotoData.getAllPhotoDataByPhotoId";

    /**
     * Id.
     * */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private long id;

    /**
     * User id.
     * */
    @Column
    private String userId;

    /**
     * Photo hash.
     * */
    @Column
    private int photoHash;

    /**
     * Photo perseptive hash.
     * */
    @Column
    private String perseptiveHash;

    /**
     * Photo id.
     * */
    @Column
    private long photoId;


    /**
     * Default constructor.
     * */
    public PhotoData() {

    }

    /**
     * Constructor.
     *
     * @param photoData     {@link PhotoDataBO} instance.
     * */
    public PhotoData(PhotoDataBO photoData) {
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

        PhotoData data = (PhotoData) o;

        if (id != data.id) return false;
        if (photoHash != data.photoHash) return false;
        if (photoId != data.photoId) return false;
        if (userId != null ? !userId.equals(data.userId) : data.userId != null) return false;
        return perseptiveHash != null ? perseptiveHash.equals(data.perseptiveHash) : data.perseptiveHash == null;
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
        return "PhotoData{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", photoHash='" + photoHash + '\'' +
                ", perseptiveHash='" + perseptiveHash + '\'' +
                ", photoId=" + photoId +
                '}';
    }
}
