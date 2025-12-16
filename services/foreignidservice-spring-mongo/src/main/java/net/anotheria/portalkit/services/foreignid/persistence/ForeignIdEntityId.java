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
    private int sourceId;

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
    public  ForeignIdEntityId(String foreignId, int sourceId) {
        this.foreignId = foreignId;
        this.sourceId = sourceId;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ForeignIdEntityId that = (ForeignIdEntityId) o;
        return sourceId == that.sourceId && Objects.equals(foreignId, that.foreignId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreignId, sourceId);
    }
}
