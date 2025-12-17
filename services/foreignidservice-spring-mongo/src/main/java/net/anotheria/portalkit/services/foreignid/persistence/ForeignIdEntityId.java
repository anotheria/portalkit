package net.anotheria.portalkit.services.foreignid.persistence;

import java.io.Serializable;
import java.util.Objects;

/**
 * ForeignIdEntityId — primary key for foreign ids.
 *
 * @author ykalapusha
 * @since 16.12.2025
 */
public class ForeignIdEntityId implements Serializable {
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 8674416313429880135L;
    /**
     * Source of foreign id.
     */
    private String foreignId;
    /**
     * Key of foreign id.
     */
    private String sourceId;

    /**
     * Default constructor.
     */
    public ForeignIdEntityId() {}

    /**
     * Parameterized constructor.
     *
     * @param foreignId foreign id
     * @param sourceId  source id
     */
    public  ForeignIdEntityId(String foreignId, String sourceId) {
        this.foreignId = foreignId;
        this.sourceId = sourceId;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ForeignIdEntityId that = (ForeignIdEntityId) o;
        return Objects.equals(foreignId, that.foreignId) && Objects.equals(sourceId, that.sourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreignId, sourceId);
    }
}
