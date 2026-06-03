import net.anotheria.util.crypt.CryptTool;
import org.junit.Ignore;
import org.junit.Test;

import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * TestCryptTool — TODO.
 *
 * @author ykalapusha
 * @since 03.06.2026
 */
public class TestPersonalDataCryptTool {

    private char[] configurationReadKey = {'z', 'F', 'P', (char) 121, 'T', 'b', (char) 97, 'a', (char) 5, (char) 71, 'W', 'n'};
    private String applicationSecret = "4661971E68115138349812C14A8B8E2A";
    private String acc = "6cf884ab-30da-4a89-82ad-d9cf7c1edc26";

    @Test
    public void baldurTest(){
        CryptTool cryptTool = new CryptTool(new String(configurationReadKey));
        String encryptedAppSecret = cryptTool.decryptFromHexTrim(applicationSecret);
        CryptTool cryptTool2 = new CryptTool(encryptedAppSecret + acc);

        String data = "Some data";
        String encrypted = cryptTool2.encryptToHex(data);
        String decrypted = cryptTool2.decryptFromHexTrim(encrypted);

        assertEquals("Strings", data, decrypted);
    }
}
