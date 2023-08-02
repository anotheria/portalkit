package net.anotheria.portalkit.adminapi.rest.account.request;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AccountsGetRequest {

    private int pageIndex;
    private int itemsOnPage;
    private String searchTerm;
    private SortCriteria<AccountSortProperty> sort;
    private AccountRegistrationDateRange registrationRange;
    private List<String> includedStatuses = new LinkedList<>();
    private List<String> excludedStatuses = new LinkedList<>();

    public AccountsGetRequest() {
        this.sort = new SortCriteria<>(SortDirection.DESC, AccountSortProperty.REGISTRATION_DATE);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getItemsOnPage() {
        return itemsOnPage;
    }

    public void setItemsOnPage(int itemsOnPage) {
        this.itemsOnPage = itemsOnPage;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public SortCriteria<AccountSortProperty> getSort() {
        return sort;
    }

    public void setSort(SortCriteria<AccountSortProperty> sort) {
        this.sort = sort;
    }

    public AccountRegistrationDateRange getRegistrationRange() {
        return registrationRange;
    }

    public void setRegistrationRange(AccountRegistrationDateRange registrationRange) {
        this.registrationRange = registrationRange;
    }

    public List<String> getIncludedStatuses() {
        return includedStatuses;
    }

    public void setIncludedStatuses(List<String> includedStatuses) {
        this.includedStatuses = includedStatuses;
    }

    public List<String> getExcludedStatuses() {
        return excludedStatuses;
    }

    public void setExcludedStatuses(List<String> excludedStatuses) {
        this.excludedStatuses = excludedStatuses;
    }

    @Override
    public String toString() {
        return "AccountsGetRequest{" +
                "pageIndex=" + pageIndex +
                ", itemsOnPage=" + itemsOnPage +
                ", searchTerm='" + searchTerm + '\'' +
                ", sort=" + sort +
                ", registrationRange=" + registrationRange +
                ", includedStatuses=" + includedStatuses +
                ", excludedStatuses=" + excludedStatuses +
                '}';
    }

    public static class AccountRegistrationDateRange {

        private long from;
        private long to;

        public long getFrom() {
            return from;
        }

        public void setFrom(long from) {
            this.from = from;
        }

        public long getTo() {
            return to;
        }

        public void setTo(long to) {
            this.to = to;
        }

        @Override
        public String toString() {
            return "AccountRegistrationDateRange{" +
                    "from=" + from +
                    ", to=" + to +
                    '}';
        }
    }

    public enum AccountSortProperty {

        NAME,
        EMAIL,
        REGISTRATION_DATE,
        STATUS

    }

    public enum SortDirection {

        ASC,
        DESC

    }

    public static class SortCriteria<S extends AccountSortProperty> implements Serializable {

        private static final long serialVersionUID = 4039481480889913965L;

        private SortDirection direction;
        private S field;

        public SortCriteria() {
        }

        public SortCriteria(SortDirection direction, S field) {
            this.direction = direction;
            this.field = field;
        }

        public SortDirection getDirection() {
            return direction;
        }

        public void setDirection(SortDirection direction) {
            this.direction = direction;
        }

        public S getField() {
            return field;
        }

        public void setField(S field) {
            this.field = field;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SortCriteria<?> that = (SortCriteria<?>) o;
            return Objects.equals(field, that.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field);
        }

        @Override
        public String toString() {
            return "SortCriteria{" +
                    "order=" + direction +
                    ", field=" + field +
                    '}';
        }
    }

}
