package net.anotheria.portalkit.adminapi.api.shared;

import java.util.LinkedList;
import java.util.List;

/**
 * Used for paginated lists
 *
 * @param <T>
 */
public class PageResult<T> {

    /**
     * Page number, index
     */
    private int pageNumber;

    /**
     * Amount of items on page
     */
    private int itemsOnPage;

    /**
     * Total items
     */
    private int totalItems;

    /**
     * Result content
     */
    private List<T> content = new LinkedList<>();

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getItemsOnPage() {
        return itemsOnPage;
    }

    public void setItemsOnPage(int itemsOnPage) {
        this.itemsOnPage = itemsOnPage;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "pageNumber=" + pageNumber +
                ", itemsOnPage=" + itemsOnPage +
                ", content=" + content +
                ", totalItems=" + totalItems +
                '}';
    }
}
