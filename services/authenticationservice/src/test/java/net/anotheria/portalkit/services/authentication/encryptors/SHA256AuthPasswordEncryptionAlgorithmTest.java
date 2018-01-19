package net.anotheria.portalkit.services.authentication.encryptors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.06.14 21:13
 */
public class SHA256AuthPasswordEncryptionAlgorithmTest {
	@Test
	public void testDifferentPhrases(){
		String enc1 = SHA256PasswordEncryptionAlgorithm().encryptPassword("password1");
		String enc2 = SHA256PasswordEncryptionAlgorithm().encryptPassword("password2");
		assertFalse(enc1.equals(enc2));
	}
	@Test
	public void testSamePhrase(){
		String enc1 = SHA256PasswordEncryptionAlgorithm().encryptPassword("password1");
		String enc2 = SHA256PasswordEncryptionAlgorithm().encryptPassword("password1");
		String enc3 = SHA256PasswordEncryptionAlgorithm().encryptPassword("pass"+"word"+1);
		assertEquals(enc1, enc2);
		assertEquals(enc1, enc3);
	}

	private  SHA256PasswordEncryptionAlgorithm SHA256PasswordEncryptionAlgorithm(){
		SHA256PasswordEncryptionAlgorithm alg = new SHA256PasswordEncryptionAlgorithm();
		alg.customize("MYKEY");
		return alg;
	}
}
