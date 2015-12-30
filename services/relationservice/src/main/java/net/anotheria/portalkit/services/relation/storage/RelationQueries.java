package net.anotheria.portalkit.services.relation.storage;

/**
 * Created by anotheria on 7/28/14.
 */
public enum RelationQueries {
    IN("inRelations"),
    OUT("outRelations");
    private String queryMethodName;

    RelationQueries(String queryValue) {
        this.queryMethodName = queryValue;
    }

    public static RelationQueries getByName(String value) {
        for (RelationQueries queries : RelationQueries.values())
            if (queries.queryMethodName.equals(value))
                return queries;
        return null;
    }

    public String getQueryMethodName() {
        return queryMethodName;
    }
}
