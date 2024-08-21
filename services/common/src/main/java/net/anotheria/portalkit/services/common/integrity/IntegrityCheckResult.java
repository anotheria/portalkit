package net.anotheria.portalkit.services.common.integrity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integrity check result implementation.
 *
 * @author lrosenberg
 * @since 23.08.15 19:34
 */
public class IntegrityCheckResult {

    /**
     * {@link Logger} instance
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrityCheckResult.class);

    /**
     * Number of entries scanned.
     */
    private int entriesScanned;
    /**
     * Number of errors found.
     */
    private int errorsFound;
    /**
     * Number of errors that were successfully fixed.
     */
    private int errorsFixed;

    public int getEntriesScanned() {
        return entriesScanned;
    }

    public void setEntriesScanned(int entriesScanned) {
        this.entriesScanned = entriesScanned;
    }

    public int getErrorsFixed() {
        return errorsFixed;
    }

    public void setErrorsFixed(int errorsFixed) {
        this.errorsFixed = errorsFixed;
    }

    public int getErrorsFound() {
        return errorsFound;
    }

    public void setErrorsFound(int errorsFound) {
        this.errorsFound = errorsFound;
    }

    @Override
    public String toString() {
        return "IntegrityCheckResult{" +
                "entriesScanned=" + entriesScanned +
                ", errorsFound=" + errorsFound +
                ", errorsFixed=" + errorsFixed +
                '}';
    }

    public void addScan() {
        entriesScanned++;
        if (entriesScanned % 10_000 == 0)
            LOGGER.info("Temporarly result: {}", this);

    }

    public void addError() {
        errorsFound++;
    }

    public void addFix() {
        errorsFixed++;
    }
}
