package net.anotheria.portalkit.adminapi.rest;

import java.util.HashMap;

public class ReplyObject {

    private boolean success;
    private String message;
    private ErrorKey errorKey;
    private HashMap<String, Object> results = new HashMap<>();

    public ReplyObject() {

    }

    /**
     * Creates a new result object with one result.
     *
     * @param name   name of the result bean.
     * @param result object for the first result bean.
     */
    public ReplyObject(String name, Object result) {
        results.put(name, result);
    }

    /**
     * Adds
     *
     * @param name
     * @param result
     */
    public void addResult(String name, Object result) {
        results.put(name, result);
    }

    /**
     * Factory method that creates a new reply object for successful request.
     *
     * @param name
     * @param result
     * @return
     */
    public static ReplyObject success(String name, Object result) {
        ReplyObject ret = new ReplyObject(name, result);
        ret.success = true;
        return ret;
    }

    /**
     * Factory method that creates a new reply object for successful request.
     *
     * @return
     */
    public static ReplyObject success() {
        ReplyObject ret = new ReplyObject();
        ret.success = true;
        return ret;
    }

    /**
     * Factory method that creates a new erroneous reply object.
     *
     * @param message
     * @return
     */
    public static ReplyObject error(String message) {
        ReplyObject ret = new ReplyObject();
        ret.success = false;
        ret.message = message;
        return ret;
    }

    public static ReplyObject error(Throwable exc) {
        ReplyObject ret = new ReplyObject();
        ret.success = false;
        ret.message = exc.getClass().getSimpleName() + ": " + exc.getMessage();
        return ret;
    }

    public static ReplyObject error(ErrorKey errorKey, String message) {
        ReplyObject ret = new ReplyObject();
        ret.success = false;
        if (message != null)
            ret.message = message;
        ret.errorKey = errorKey;
        return ret;

    }

    public static ReplyObject error(ErrorKey errorKey, String name, Object result) {
        ReplyObject ret = new ReplyObject(name, result);
        ret.success = false;
        ret.errorKey = errorKey;
        return ret;

    }

    public static ReplyObject error(ErrorKey errorKey) {
        return error(errorKey, null);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("ReplyObject ");
        ret.append("Success: ").append(success);
        if (message != null) {
            ret.append(", Message: ").append(message);
        }
        ret.append(", Results: ").append(results);
        return ret.toString();
    }

    public String getMessage() {
        return message;
    }

    public HashMap getResults() {
        return results;
    }

    public ErrorKey getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(ErrorKey errorKey) {
        this.errorKey = errorKey;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResults(HashMap<String, Object> results) {
        this.results = results;
    }
}
