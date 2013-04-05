package net.anotheria.portalkit.apis.online;

import net.anotheria.portalkit.services.online.OnlineAccountReadCriteria;

import java.io.Serializable;

/**
 * Online account read criteria, api layer object.
 *
 * @author h3llka
 */
public final class OnlineAccountReadCriteriaAO implements Serializable {
    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 2252421414518351692L;
    /**
     * OnlineAccountReadCriteriaAO underlying criteria.
     */
    private OnlineAccountReadCriteria criteria;

    /**
     * Constructor.
     *
     * @param original {@link OnlineAccountReadCriteria}
     */
    private OnlineAccountReadCriteriaAO(final OnlineAccountReadCriteria original) {
        this.criteria = original;
    }

    /**
     * Return {@link OnlineAccountReadCriteria} for internal usage.
     *
     * @return {@link OnlineAccountReadCriteria}
     */
    protected OnlineAccountReadCriteria getCriteria() {
        return criteria;
    }

    /**
     * Builder for {@link OnlineAccountReadCriteriaAO}.
     */
    public static final class Builder extends OnlineAccountReadCriteria.Builder {

        /**
         * Build is not supported on API layer cause {@link OnlineAccountReadCriteria} is business object.
         * Please use {@link Builder#buildCriteria()} instead - to receive {@link OnlineAccountReadCriteriaAO} instance.
         * <p/>
         * On call attempt {@link UnsupportedOperationException} will be thrown.
         *
         * @return {@link OnlineAccountReadCriteria}
         */
        @Override
        @Deprecated
        public OnlineAccountReadCriteria build() {
            //Restricting access to parent operation...
            throw new UnsupportedOperationException("Operation not supported on API-layer. Use {@link Builder#buildCriteria()}.");
        }

        /**
         * Perform  {@link OnlineAccountReadCriteriaAO} build.
         *
         * @return {@link OnlineAccountReadCriteriaAO}
         */
        public OnlineAccountReadCriteriaAO buildCriteria() {
            return new OnlineAccountReadCriteriaAO(super.build());
        }

    }

    @Override
    public String toString() {
        return "OnlineAccountReadCriteriaAO{" +
                "criteria=" + criteria +
                '}';
    }

}
