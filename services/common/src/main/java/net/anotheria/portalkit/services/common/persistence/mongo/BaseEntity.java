package net.anotheria.portalkit.services.common.persistence.mongo;

import org.bson.types.ObjectId;

/**
 * Helper class to store entities in MongoDB
 */
public abstract class BaseEntity {
    public abstract void setId(ObjectId id);

    public abstract ObjectId getId();
}
