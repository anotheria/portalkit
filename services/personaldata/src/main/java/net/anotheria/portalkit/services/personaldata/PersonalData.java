package net.anotheria.portalkit.services.personaldata;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import net.anotheria.portalkit.services.common.AccountId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Vlad Lukjanenko
 */
@Entity(value = "personal_data")
public class PersonalData implements Serializable {

    /**
     * Generated serial version UID.
     * */
    private static final long serialVersionUID = 5509007099048106610L;

    /**
     * User account id.
     * */
    @Id
    private String _id;

    @Indexed(options = @IndexOptions(unique = true))
    private AccountId accountId;

    /**
     * User personal data.
     * */
    private Map<String, String> personalData;


    /**
     * Default constructor.
     * */
    public PersonalData() {
        personalData = new HashMap<>();
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public Map<String, String> getPersonalData() {
        return personalData;
    }

    public void setPersonalData(Map<String, String> personalData) {
        this.personalData = personalData;
    }


    public void addData(String key, String value) {
        this.personalData.put(key, value);
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonalData that = (PersonalData) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) {
            return false;
        }

        return personalData != null ? personalData.equals(that.personalData) : that.personalData == null;

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.accountId);
    }

    @Override
    public String toString() {
        return "PersonalData{" +
                ", accountId=" + accountId +
                ", personalData=" + personalData +
                '}';
    }
}
