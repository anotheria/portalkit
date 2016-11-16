package net.anotheria.portalkit.services.personaldata;

import net.anotheria.anoprise.metafactory.AbstractParameterizedServiceFactory;

/**
 * @author asamoilich.
 */
public class PersonalDataServiceFactory extends AbstractParameterizedServiceFactory<PersonalDataService> {

    @Override
    public PersonalDataService create() {
        return new PersonalDataServiceImpl();
    }
}
