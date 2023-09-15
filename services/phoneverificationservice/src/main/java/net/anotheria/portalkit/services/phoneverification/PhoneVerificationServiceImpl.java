package net.anotheria.portalkit.services.phoneverification;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class PhoneVerificationServiceImpl implements PhoneVerificationService {

    /**
     * {@link Logger} instance.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(PhoneVerificationServiceImpl.class);
    /**
     * {@link PhoneVerificationConfig} instance.
     * */
    private final PhoneVerificationConfig phoneVerificationConfig;

    public PhoneVerificationServiceImpl() {
        this.phoneVerificationConfig = PhoneVerificationConfig.getInstance();
        Twilio.init(phoneVerificationConfig.getUsername(), phoneVerificationConfig.getPassword());
    }

    @Override
    public void sendVerifyCode(String phoneNumber) throws PhoneVerificationServiceException {
        try {
            Verification verification = Verification.creator(
                            phoneVerificationConfig.getPathServiceSid(),
                            phoneNumber,
                            "sms")
                    .create();
            LOGGER.info("Verify code successfully send to {}", phoneNumber);
        } catch (Exception e) {
            LOGGER.error("Invalid phone number {}", e.getMessage());
            throw new PhoneVerificationServiceException("Cannot send verify code");
        }
    }

    @Override
    public VerifyResponse checkVerifyCode(String phoneNumber, String code) throws PhoneVerificationServiceException {
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(
                            phoneVerificationConfig.getPathServiceSid())
                    .setTo(phoneNumber)
                    .setCode(code)
                    .create();
            VerifyResponse response = VerifyResponse.getResponseByValue(verificationCheck.getStatus());
            return Objects.nonNull(response) && response == VerifyResponse.VERIFICATION_SUCCESS ?
                    VerifyResponse.VERIFICATION_SUCCESS : VerifyResponse.VERIFICATION_FAILED;
        } catch (Exception e) {
            return VerifyResponse.VERIFICATION_FAILED;
        }
    }
}
