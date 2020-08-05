package net.anotheria.portalkit.services.approval;

/**
 * Reference type of ticket.
 *
 * @author ynikonchuk
 */
public interface IReferenceType {

    /**
     * Get reference type id.
     *
     * @return int
     */
    long getId();

    /**
     * Get reference type name.
     *
     * @return String
     */
    String getName();
}
