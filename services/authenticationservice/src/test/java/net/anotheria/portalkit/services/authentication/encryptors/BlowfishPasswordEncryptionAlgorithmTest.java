package net.anotheria.portalkit.services.authentication.encryptors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BlowfishPasswordEncryptionAlgorithmTest{
	@Test public void testEnDecryption(){
		String key = "ABCDEFGHJKLIERHDGHSGJDGH";
		BlowfishPasswordEncryptionAlgorithm alg = new BlowfishPasswordEncryptionAlgorithm();
		alg.customize(key);
		String pass = "password";
		String encPass = alg.encryptPassword(pass);
		assertEquals(pass, alg.decryptPassword(encPass));

	}

	@Test public void testUninitializedBehaviour(){
		BlowfishPasswordEncryptionAlgorithm alg = new BlowfishPasswordEncryptionAlgorithm();
		try{
			alg.encryptPassword("password");
			fail("Expect exception");
		}catch(Exception e){
			//everything ok then
		}
	}
}