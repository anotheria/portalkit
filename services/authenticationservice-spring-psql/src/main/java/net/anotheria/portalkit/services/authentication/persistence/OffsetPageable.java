package net.anotheria.portalkit.services.authentication.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * A {@link Pageable} which is defined by an offset and a limit instead of by a page number and a page size.
 *
 * Spring Data's PageRequest derives the offset from page number times page size, which can only express offsets
 * which are a multiple of the page size. The token inventory takes limit and offset from its caller, so an
 * arbitrary offset has to be expressible, otherwise paging would silently return the wrong window.
 *
 * @author lrosenberg
 * @since 04.09.26 10:00
 */
public class OffsetPageable implements Pageable {

	/**
	 * Number of elements to skip.
	 */
	private final long offset;

	/**
	 * Maximum number of elements to return.
	 */
	private final int limit;

	/**
	 * Sort order.
	 */
	private final Sort sort;

	/**
	 * Creates a new pageable.
	 *
	 * @param offset number of elements to skip, must not be negative.
	 * @param limit  maximum number of elements, must be greater than zero.
	 * @param sort   sort order, must not be null.
	 */
	public OffsetPageable(long offset, int limit, Sort sort) {
		if (offset < 0)
			throw new IllegalArgumentException("offset can't be negative");
		if (limit < 1)
			throw new IllegalArgumentException("limit has to be greater than zero");

		this.offset = offset;
		this.limit = limit;
		this.sort = sort;
	}

	@Override
	public int getPageNumber() {
		return (int) (offset / limit);
	}

	@Override
	public int getPageSize() {
		return limit;
	}

	@Override
	public long getOffset() {
		return offset;
	}

	@Override
	public Sort getSort() {
		return sort;
	}

	@Override
	public Pageable next() {
		return new OffsetPageable(offset + limit, limit, sort);
	}

	@Override
	public Pageable previousOrFirst() {
		return hasPrevious() ? new OffsetPageable(offset - limit, limit, sort) : first();
	}

	@Override
	public Pageable first() {
		return new OffsetPageable(0, limit, sort);
	}

	@Override
	public Pageable withPage(int pageNumber) {
		return new OffsetPageable((long) pageNumber * limit, limit, sort);
	}

	@Override
	public boolean hasPrevious() {
		return offset >= limit;
	}
}
