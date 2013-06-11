package net.anotheria.portalkit.services.storage.mongo.index;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.configureme.annotations.Configure;
import org.configureme.annotations.DontConfigure;

/**
 * Index configuration.
 * 
 * @author Alexandr Bolbat
 */
public class Index implements Serializable {

	/**
	 * Generated SerialVersionUID.
	 */
	@DontConfigure
	private static final long serialVersionUID = 8533612629850711072L;

	/**
	 * Index 'name' property.
	 */
	@DontConfigure
	public static final String MONGO_INDEX_PROPERTY_NAME = "name";

	/**
	 * Index 'unique' property.
	 */
	@DontConfigure
	public static final String MONGO_INDEX_PROPERTY_UNIQUE = "unique";

	/**
	 * Index 'dropDups' property.
	 */
	@DontConfigure
	public static final String MONGO_INDEX_PROPERTY_DROPDUPS = "dropDups";

	/**
	 * Index 'sparse' property.
	 */
	@DontConfigure
	public static final String MONGO_INDEX_PROPERTY_SPARSE = "sparse";

	/**
	 * Index 'background' property.
	 */
	@DontConfigure
	public static final String MONGO_INDEX_PROPERTY_BACKGROUND = "background";

	/**
	 * Index name.<br>
	 * Will be generated automatically if empty.
	 */
	@Configure
	private String name;

	/**
	 * Fields configuration for compound index.<br>
	 * At least one field should be configured.<br>
	 * For indexes with more than one key (i.e. compound indexes) the sequence of fields is important.
	 */
	@Configure
	private IndexField[] fields;

	/**
	 * MongoDB allows you to specify a unique constraint on an index.<br>
	 * These constraints prevent applications from inserting documents that have duplicate values for the inserted fields.<br>
	 * These indexes enforce uniqueness for the combination of index keys and not for either key individually.
	 */
	@Configure
	private boolean unique = false;

	/**
	 * To force the creation of a unique index index on a collection with duplicate values in the field you are indexing you can use the dropDups option.<br>
	 * This will force MongoDB to create a unique index by deleting documents with duplicate values when building the index.<br>
	 * May delete data from your database. Use with extreme caution.
	 */
	@Configure
	private boolean dropDups = false;

	/**
	 * Sparse indexes are like non-sparse indexes, except that they omit references to documents that do not include the indexed field.<br>
	 * For fields that are only present in some documents sparse indexes may provide a significant space savings.<br>
	 * Sparse indexes can affect the results returned by the query, particularly with respect to sorts on fields not included in the index.
	 */
	@Configure
	private boolean sparse = false;

	/**
	 * By default, MongoDB builds indexes in the foreground.<br>
	 * Which means that these indexes block all other read and write operations to the database while the index builds.<br>
	 * Background index construction allows read and write operations to continue while building the index.<br>
	 * However, these index builds take longer to complete and result in a larger index.
	 */
	@Configure
	private boolean background = false;

	public String getName() {
		return name;
	}

	public void setName(final String aName) {
		this.name = aName;
	}

	/**
	 * Get indexes fields configuration.
	 * 
	 * @return {@link List} of {@link IndexField}
	 */
	public List<IndexField> getFields() {
		return fields != null ? Arrays.asList(fields) : new ArrayList<IndexField>();
	}

	/**
	 * Set index fields configuration.
	 * 
	 * @param aFields
	 *            fields configuration
	 */
	public void setFields(final List<IndexField> aFields) {
		this.fields = aFields != null ? aFields.toArray(new IndexField[aFields.size()]) : null;
	}

	public void setFields(final IndexField[] aFields) {
		this.fields = aFields != null ? aFields.clone() : null;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(final boolean aUnique) {
		this.unique = aUnique;
	}

	public boolean isDropDups() {
		return dropDups;
	}

	public void setDropDups(final boolean aDropDups) {
		this.dropDups = aDropDups;
	}

	public boolean isSparse() {
		return sparse;
	}

	public void setSparse(final boolean aSparse) {
		this.sparse = aSparse;
	}

	public boolean isBackground() {
		return background;
	}

	public void setBackground(final boolean aBackground) {
		this.background = aBackground;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(fields);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Index other = (Index) obj;
		if (!Arrays.equals(fields, other.fields))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
		builder.append(" [name=").append(name);
		builder.append(", fields=").append(fields != null ? Arrays.asList(fields) : null);
		builder.append(", unique=").append(unique);
		builder.append(", dropDups=").append(dropDups);
		builder.append(", sparse=").append(sparse);
		builder.append(", background=").append(background);
		builder.append("]");
		return builder.toString();
	}
}
