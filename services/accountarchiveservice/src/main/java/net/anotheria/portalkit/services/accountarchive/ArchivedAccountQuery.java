package net.anotheria.portalkit.services.accountarchive;

import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Query parameters.
 *
 * @author vkoulakov
 */
public class ArchivedAccountQuery implements Serializable {

    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 1157988002892952051L;
    /**
     * Account identifiers.
     */
    private final List<AccountId> ids = new ArrayList<AccountId>();
    /**
     * Included types.
     */
    private final List<Integer> typesIncluded = new ArrayList<Integer>();
    /**
     * Excluded types.
     */
    private final List<Integer> typesExcluded = new ArrayList<Integer>();
    /**
     * Included statuses.
     */
    private final List<Long> statusesIncluded = new ArrayList<Long>();
    /**
     * Excluded statuses.
     */
    private final List<Long> statusesExcluded = new ArrayList<Long>();
    /**
     * Account name mask.
     */
    private String nameMask;
    /**
     * Account email mask.
     */
    private String emailMask;
    /**
     * Account identifier mask.
     */
    private String idMask;
    /**
     * Registered from.
     */
    private Long registeredFrom;
    /**
     * Registered till.
     */
    private Long registeredTill;
    /**
     * The lower edge when account was deleted.
     */
    private Long deletedFrom;
    /**
     * The highest bound when account was deleted.
     */
    private Long deletedTill;

    public Long getDeletedTill() {
        return deletedTill;
    }

    public void setDeletedTill(Long deletedTill) {
        this.deletedTill = deletedTill;
    }

    public Long getDeletedFrom() {
        return deletedFrom;
    }

    public void setDeletedFrom(Long deletedFrom) {
        this.deletedFrom = deletedFrom;
    }

    public List<AccountId> getIds() {
        return ids;
    }

    public String getNameMask() {
        return nameMask;
    }

    public void setNameMask(final String aNameMask) {
        this.nameMask = aNameMask;
    }

    public String getEmailMask() {
        return emailMask;
    }

    public void setEmailMask(final String aEmailMask) {
        this.emailMask = aEmailMask;
    }

    public String getIdMask() {
        return idMask;
    }

    public void setIdMask(final String aIdMask) {
        this.idMask = aIdMask;
    }

    public List<Integer> getTypesIncluded() {
        return typesIncluded;
    }

    public List<Integer> getTypesExcluded() {
        return typesExcluded;
    }

    public List<Long> getStatusesIncluded() {
        return statusesIncluded;
    }

    public List<Long> getStatusesExcluded() {
        return statusesExcluded;
    }

    public Long getRegisteredFrom() {
        return registeredFrom;
    }

    public void setRegisteredFrom(final Long aRegisteredFrom) {
        this.registeredFrom = aRegisteredFrom;
    }

    public Long getRegisteredTill() {
        return registeredTill;
    }

    public void setRegisteredTill(final Long aRegisteredTill) {
        this.registeredTill = aRegisteredTill;
    }

}
