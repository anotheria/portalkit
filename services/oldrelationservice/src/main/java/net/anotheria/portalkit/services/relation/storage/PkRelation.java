package net.anotheria.portalkit.services.relation.storage;

import java.io.Serializable;

/**
 * Relation object.
 *
 * @author asamoilich
 */
@Deprecated
public class PkRelation implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -4358511966770818234L;
    /**
     * Relation name.
     */
    private String name;
    /**
     * Additional parameter.
     */
    private String parameter;
    /**
     * Created time.
     */
    private long createdTime;
    /**
     * Updated time.
     */
    private long updatedTime;

    /**
     * Constructor.
     *
     * @param name relation name
     */
    public PkRelation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "name='" + name + '\'' +
                ", parameter='" + parameter + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
