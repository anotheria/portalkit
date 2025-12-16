package net.anotheria.portalkit.services.foreignid.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ForeignIdEntity — persistence data object for user's foreign ids.
 *
 * @author ykalapusha
 * @since 16.12.2025
 */
@Document(collection = "pk-foreign-ids")
@CompoundIndexes({
        @CompoundIndex(
                name = "foreignid_sourceid_idx",
                def = "{ 'foreignId': 1, 'sourceId': 1 }",
                unique = true
        ),
        @CompoundIndex(
                name = "accountid_idx",
                def = "{ 'accountId': 1 }"
        )
})
public class ForeignIdEntity {
    /**
     * Foreign id.
     */
    @Id
    private ForeignIdEntityId id;
    /**
     * Account id linked to foreign id.
     */
    private String accountId;

    /**
     * Default constructor.
     */
    public ForeignIdEntity() {}

    public ForeignIdEntityId getId() {
        return id;
    }

    public void setId(ForeignIdEntityId id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
