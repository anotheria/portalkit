package net.anotheria.portalkit.engines.mailhunter.transformer;

import net.anotheria.util.StringUtils;

/**
 * Transforms simple words (crypted) to decrypted values.
 *
 * @author another
 * @author Vlad Lukjanenko
 */
public class DecryptionTransformer extends LocalizedTransformer {

	@Override
	public String transform(String s, String locale) {

		String[] crypted = localeCryptedValues;
		String[] uncrypted = localeUncryptedValues;

		if (crypted == null || uncrypted == null) {
			return s;
		}

		for (int i = 0; i < crypted.length; i++) {
			if (s.indexOf(crypted[i]) != -1) {
				s = StringUtils.replace(s, crypted[i], uncrypted[i]);
			}
		}

		return s;
	}
}
