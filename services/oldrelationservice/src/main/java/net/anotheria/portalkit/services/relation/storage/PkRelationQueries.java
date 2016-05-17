package net.anotheria.portalkit.services.relation.storage;

/**
 * Created by anotheria on 7/28/14.
 */
@Deprecated
public enum PkRelationQueries {
    IN("inRelations"),
    OUT("outRelations");
    private String queryMethodName;

    PkRelationQueries(String queryValue) {
        this.queryMethodName = queryValue;
    }

    public static PkRelationQueries getByName(String value) {
        for (PkRelationQueries queries : PkRelationQueries.values())
            if (queries.queryMethodName.equals(value))
                return queries;
        return null;
    }

    public String getQueryMethodName() {
        return queryMethodName;
    }
}
