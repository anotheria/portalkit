package net.anotheria.portalkit.services.authentication.encryptors;

import org.junit.Test;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.06.14 21:13
 */
public class SHA256AuthTokenEncryptionAlgorithmTest {
	@Test
	public void nix(){
		System.out.println(new SHA256AuthTokenEncryptionAlgorithm().encryptPassword("password1"));
		System.out.println(new SHA256AuthTokenEncryptionAlgorithm().encryptPassword("password2"));
		System.out.println(new SHA256AuthTokenEncryptionAlgorithm().encryptPassword("1"));
		System.out.println(new SHA256AuthTokenEncryptionAlgorithm().encryptPassword("2"));
	}
}
