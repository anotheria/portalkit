package net.anotheria.portalkit.services.personaldata;

import net.anotheria.portalkit.services.common.AccountId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Vlad Lukjanenko
 */
@Entity(value = "personal_data",noClassnameStored = true)
public class PersonalData {

    /**
     * User account id.
     * */
    @Id
    private String _id;

    @Indexed(unique = true)
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
