package net.anotheria.portalkit.apis.asynctask.task;

import net.anotheria.net.util.NetUtils;

/**
 * This task has no operation it is used to measure performance of the queue (run-through time).
 *
 * @author lrosenberg
 * @since 2019-05-22 20:30
 */
public class NoOperationTask extends AsyncTask {
    /**
     * Source machine.
     */
    private String sourceMachine;
    /**
     * Creation timestamp.
     */
    private long timestamp;
    /**
     * No operation attribute.
     */
    private boolean noOperation =  true;

    /**
     * Based constructor.
     *
     * @param aTaskType task type.
     */
    public NoOperationTask(String aTaskType) {
        super(aTaskType);
        timestamp = System.currentTimeMillis();
        sourceMachine = NetUtils.getShortComputerName();
    }

    public String getSourceMachine() {
        return sourceMachine;
    }

    public void setSourceMachine(String sourceMachine) {
        this.sourceMachine = sourceMachine;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isNoOperation() {
        return noOperation;
    }

    public void setNoOperation(boolean noOperation) {
        this.noOperation = noOperation;
    }

    @Override
    public String toString(){
        return "No Operation sent @ " + timestamp + " from " + sourceMachine + " via topic " + taskType + " " + (System.currentTimeMillis()-timestamp) + " ms ago. By " + Thread.currentThread().getName();
    }
}
