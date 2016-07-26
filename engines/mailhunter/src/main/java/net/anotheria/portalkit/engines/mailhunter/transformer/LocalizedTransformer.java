package net.anotheria.portalkit.engines.mailhunter.transformer;


import java.util.HashMap;
import java.util.Map;

/**
 * Contains crypted and uncrypted values for transformer for all available locales.
 *
 * @author Vlad Lukjanenko
 */
public abstract class LocalizedTransformer extends AbstractTransformer {

    /**
     * Crypted values for locales.
     * */
    protected String[] localeCryptedValues;

    /**
     * Uncrypted values for locales.
     * */
    protected String[] localeUncryptedValues;


    protected LocalizedTransformer(int order) {

        super(order);

        localeCryptedValues = new String[] {};
        localeUncryptedValues = new String[] {};
    }

    protected LocalizedTransformer() {

        super();

        localeCryptedValues = new String[] {};
        localeUncryptedValues = new String[] {};
    }


    public String[] getLocaleCryptedValues() {
        return localeCryptedValues;
    }

    public void setLocaleCryptedValues(String[] localeCryptedValues) {
        this.localeCryptedValues = localeCryptedValues;
    }

    public String[] getLocaleUncryptedValues() {
        return localeUncryptedValues;
    }

    public void setLocaleUncryptedValues(String[] localeUncryptedValues) {
        this.localeUncryptedValues = localeUncryptedValues;
    }

    @Override
    public String transform(String s) {
        return s;
    }
}
