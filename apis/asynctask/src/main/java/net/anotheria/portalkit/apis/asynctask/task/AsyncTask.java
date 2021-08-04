package net.anotheria.portalkit.apis.asynctask.task;

import net.anotheria.util.IdCodeGenerator;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ynikonchuk
 */
public abstract class AsyncTask implements Serializable {

    /**
     * Creation timestamp helps to determine how long the task have been in queue before it was executed.
     */
    private long creationTimestamp = System.currentTimeMillis();

    private static String vmId = IdCodeGenerator.generateCode(3);
    private static AtomicLong taskId = new AtomicLong();
    private String id;

    /**
     * Unique task type
     */
    protected String taskType;

    protected AsyncTask(String aTaskType) {
        id = generateId();
        taskType = aTaskType;
    }

    public String getId() {
        return id;
    }

    public String getTaskType() {
        return taskType;
    }

    public long getAgeInMilliseconds() {
        return System.currentTimeMillis() - creationTimestamp;
    }

    /**
     * Getter just for gson.
     *
     * @return
     */
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Setter just for gson.
     *
     * @param creationTimestamp
     */
    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    private String generateId() {
        return vmId + taskId.incrementAndGet();
    }

}
