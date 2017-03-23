package net.anotheria.portalkit.services.scamscore;

import net.anotheria.portalkit.services.scamscore.persistence.UserScamRecordDO;

/**
 * @author Vlad Lukjanenko
 */
public class UserScamRecordBO {

    /**
     * Id.
     * */
    private String id;

    /**
     * Total score.
     * */
    private int total_score;

    /**
     * Profile was manually checked.
     * */
    private boolean checked;

    /**
     * Creation timestamp.
     * */
    private long created = System.currentTimeMillis();

    /**
     * Time of updating.
     * */
    private long updated;


    /**
     * Default constructor.
     * */
    public UserScamRecordBO() {

    }

    /**
     * Default constructor.
     * */
    public UserScamRecordBO(UserScamRecordDO record) {

        this.id = record.getId();
        this.total_score = record.getTotal_score();
        this.checked = record.isChecked();
        this.created = record.getCreated();
        this.updated = record.getUpdated();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public UserScamRecordDO toDO() {

        UserScamRecordDO scamRecord = new UserScamRecordDO();

        scamRecord.setId(this.id);
        scamRecord.setTotal_score(this.total_score);
        scamRecord.setChecked(this.checked);
        scamRecord.setCreated(this.created);
        scamRecord.setUpdated(this.updated);

        return scamRecord;
    }


    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof UserScamRecordBO)) {
            return false;
        }

        UserScamRecordBO scamRecord = (UserScamRecordBO) obj;

        if (!this.id.equals(scamRecord.getId())) {
            return false;
        }

        return true;
    }
}
