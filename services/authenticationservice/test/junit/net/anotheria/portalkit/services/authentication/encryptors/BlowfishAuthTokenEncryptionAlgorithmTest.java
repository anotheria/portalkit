package net.anotheria.portalkit.services.authentication.encryptors;

import net.anotheria.portalkit.services.authentication.AuthToken;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 29.01.13 10:52
 */
public class BlowfishAuthTokenEncryptionAlgorithmTest {
	@Test
	public void testEncryptDecrypt(){
		AuthToken token = new AuthToken();

		AccountId acc = AccountId.generateNew();
		token.setAccountId(acc);
		token.setType(42);
		token.setExclusive(true);
		token.setExclusiveInType(true);

		BlowfishAuthTokenEncryptionAlgorithm alg = new BlowfishAuthTokenEncryptionAlgorithm();
		alg.customize("ABCDEFGHIJKLMN");

		String encrypted = alg.encryptAuthToken(token);
		AuthToken token2 = alg.decryptAuthToken(encrypted);

		assertEquals(token, token2);

	}
}
