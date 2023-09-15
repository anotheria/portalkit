package net.anotheria.portalkit.services.phoneverification;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

/**
 * @author vberkunov
 */
@DistributeMe()
@FailBy(strategyClass= RetryCallOnce.class)
public interface PhoneVerificationService extends Service {

    /**
     *
     * Sends a verification code to the provided phone number
     *
     * @param phoneNumber number where code will be sent to
     */
    void sendVerifyCode(String phoneNumber) throws PhoneVerificationServiceException;

    /**
     *
     * Check verification code that was sent to the provided phone number.
     * Throws exception if code is invalid.
     *
     * @param phoneNumber phone number
     * @param code        verification code
     */
    VerifyResponse checkVerifyCode(String phoneNumber, String code) throws PhoneVerificationServiceException;
}
