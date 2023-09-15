package net.anotheria.portalkit.services.phoneverification;

import net.anotheria.anoprise.metafactory.AbstractParameterizedServiceFactory;

/**
 * @author vberkunov.
 */
public class PhoneVerificationServiceFactory extends AbstractParameterizedServiceFactory<PhoneVerificationService> {

    @Override
    public PhoneVerificationService create() {
        return new PhoneVerificationServiceImpl();
    }
}
