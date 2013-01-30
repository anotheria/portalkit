package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This utility provides handling for encryption and decryption of auth tokens.
 * An encryption token has following form: algorithm-identifier:encryptedstring.
 * The encrypted string is algorithm specific. The utility supports multiple
 * algorithms. The utility is configured via pk-auth.json.
 * Example encrypted strings:
 * B:1FBDA322F38F8D6105500A92E9D9C9F5F6A9119489E1FAF895009D4A9C9877B1ACC94EB4EB797AA29AB1861402F38602E48FF38549807301B6412DB8700CF385C505423F8629C923A77F9CF505F05CC17E397D62E8699FF79B23C0CB5921F58A6D45492DF835CE80098B9F19326D30E51EE651BD4F9A8990D53E70DDEE7313C6
 * P:t:42&exl:true&R:-7717072686164024024&expTs:9223372036854775807&mu:false&exlt:true&accId:e4a6374b-edc7-4b00-aff4-c134a2157e36
 *
 * @author lrosenberg
 * @since 28.01.13 11:19
 */
public class AuthTokenEncryptors {

	public static final String P_ACCOUNTID = "accId";
	public static final String P_EXPIRYTS = "expTs";
	public static final String P_MULTIUSE = "mu";
	public static final String P_EXCLUSIVE = "exl";
	public static final String P_EXCLUSIVE_IN_TYPE = "exlt";
	public static final String P_TYPE = "t";
	public static final String P_RANDOM = "R";

	/**
	 * Logger.
	 */
	private static final Logger log;

	/**
	 * Map with classes for auth token encryption.
	 */
	private static Map<String, Class<? extends AuthTokenEncryptionAlgorithm>> encryptorMap = new HashMap<String, Class<? extends AuthTokenEncryptionAlgorithm>>();
	/**
	 * Parametrization of the auth token encryption algorithms.
	 */
	private static Map<String, String> encryptorConfigParameter = new HashMap<String, String>();
	/**
	 * Default encryption algorithm.
	 */
	private static String defaultEncryptionAlgorithm = "";


	static{
		log = Logger.getLogger(AuthTokenEncryptors.class);
		AuthTokenEncryptorsConfig config = new AuthTokenEncryptorsConfig();
		try{
			ConfigurationManager.INSTANCE.configure(config);
		}catch(IllegalArgumentException e){
			//perform one time initialization manually
			e.printStackTrace();
			afterConfiguration(config);
		}
	}


	/**
	 * Random.
	 */
	private static final Random rnd = new Random(System.currentTimeMillis());

	public static AuthTokenEncryptionAlgorithm getEncryptionAlgorithm(String shortcut){
		Class<? extends AuthTokenEncryptionAlgorithm> clazz = encryptorMap.get(shortcut);

		AuthTokenEncryptionAlgorithm alg = null;
		try{
			alg = AuthTokenEncryptionAlgorithm.class.cast(clazz.newInstance());
		}catch(IllegalAccessException e){
			throw new IllegalStateException("Can't instantiate "+clazz.getName()+" for "+shortcut);
		}catch(InstantiationException e){
			throw new IllegalStateException("Can't instantiate "+clazz.getName()+" for "+shortcut);
		}

		alg.customize(encryptorConfigParameter.get(shortcut));
		return alg;


	}

	public static AuthTokenEncryptionAlgorithm getDefaultEncryptionAlgorithm(){
		return getEncryptionAlgorithm(defaultEncryptionAlgorithm);

	}

	public static String toParameterString(AuthToken token){
		StringBuilder ret = new StringBuilder();

		for (Map.Entry<String,String> entry : toParameterMap(token).entrySet()){
			if (ret.length()>0)
				ret.append('&');
			ret.append(entry.getKey()).append(':').append(entry.getValue());
		}

		return ret.toString();
	}

	public static Map<String,String> toParameterMap(AuthToken token){
		HashMap<String,String> parameterMap = new HashMap<String, String>();
		if (token.getAccountId()==null)
			throw new IllegalArgumentException("Can't encode anonymous AuthToken, AccountId is missing");
		parameterMap.put(P_ACCOUNTID, token.getAccountId().getInternalId());
		parameterMap.put(P_EXPIRYTS, ""+token.getExpiryTimestamp());
		parameterMap.put(P_MULTIUSE, ""+token.isMultiUse());
		parameterMap.put(P_EXCLUSIVE, ""+token.isExclusive());
		parameterMap.put(P_EXCLUSIVE_IN_TYPE, ""+token.isExclusiveInType());
		parameterMap.put(P_TYPE, ""+token.getType());
		parameterMap.put(P_RANDOM, ""+rnd.nextLong());
		return parameterMap;
	}

	public static AuthToken fromParameterMap(Map<String, String> parameters){
		AuthToken ret = new AuthToken();

		ret.setAccountId(new AccountId(parameters.get(P_ACCOUNTID)));
		ret.setExclusive(Boolean.parseBoolean(parameters.get(P_EXCLUSIVE)));
		ret.setExclusiveInType(Boolean.parseBoolean(parameters.get(P_EXCLUSIVE_IN_TYPE)));
		ret.setMultiUse(Boolean.parseBoolean(parameters.get(P_MULTIUSE)));

		ret.setType(Integer.parseInt(parameters.get(P_TYPE)));
		ret.setExpiryTimestamp(Long.parseLong(parameters.get(P_EXPIRYTS)));

		if (Long.valueOf(parameters.get(P_EXPIRYTS))==null)
			throw new IllegalArgumentException("No random element");


		return ret;
	}

	public static AuthToken fromParameterString(String parameterString){
		HashMap<String, String> parameters = new HashMap<String, String>();
		String[] pairs = StringUtils.tokenize(parameterString, '&');
		for (String p : pairs){
			String[] kv = StringUtils.tokenize(p, ':');
			parameters.put(kv[0], kv[1]);
		}
		return fromParameterMap(parameters);
	}

	static void afterConfiguration(AuthTokenEncryptorsConfig config){
		defaultEncryptionAlgorithm = config.getAuthenticationAlgorithm();

		HashMap<String, Class<? extends AuthTokenEncryptionAlgorithm>> newEncryptorMap = new HashMap<String, Class<? extends AuthTokenEncryptionAlgorithm>>();
		HashMap<String, String> newEncryptorConfigParameter = new HashMap<String, String>();
		for (AuthenticationAlgorithmConfig aac : config.getAuthenticationAlgorithms()){
			try{
				newEncryptorMap.put(aac.getShortcut(), (Class<? extends AuthTokenEncryptionAlgorithm>)Class.forName(aac.getClazz()));
				newEncryptorConfigParameter.put(aac.getShortcut(), aac.getKey());
			}catch(ClassNotFoundException e){
				log.error("Couldn't load AuthTokenEncryptionAlgorithm of class "+aac+", skipped.");
			}
		}

		//the following two lines are possible a problem in very high traffic because the app is in strange state between the lines.
		encryptorConfigParameter = newEncryptorConfigParameter;
		encryptorMap = newEncryptorMap;


	}

	/**
	 * Encrypt the AuthToken and returns full encryption string incl algorithm identifier.
	 * @param token
	 * @return
	 */
	public static String encrypt(AuthToken token) {
		return defaultEncryptionAlgorithm+":"+getDefaultEncryptionAlgorithm().encryptAuthToken(token);
	}

	public static AuthToken decrypt(String encryptedString){
		String[] tokens = StringUtils.tokenize(encryptedString, ':');
		String alg = tokens[0];
		String enc = encryptedString.substring(encryptedString.indexOf(':')+1);
		return getEncryptionAlgorithm(alg).decryptAuthToken(enc);
	}

	@ConfigureMe (name="pk-auth", allfields = true, watch = true)
	public static class AuthTokenEncryptorsConfig{
		@Configure private String authenticationAlgorithm;

		@Configure private AuthenticationAlgorithmConfig[] authenticationAlgorithms;

		public AuthenticationAlgorithmConfig[] getAuthenticationAlgorithms() {
			return authenticationAlgorithms;
		}

		public void setAuthenticationAlgorithms(AuthenticationAlgorithmConfig[] authenticationAlgorithms) {
			this.authenticationAlgorithms = authenticationAlgorithms;
		}

		public String getAuthenticationAlgorithm() {
			return authenticationAlgorithm;
		}

		public void setAuthenticationAlgorithm(String authenticationAlgorithm) {
			this.authenticationAlgorithm = authenticationAlgorithm;
		}

		@AfterConfiguration public void afterConfiguration(){
			AuthTokenEncryptors.afterConfiguration(this);
		}

		@Override public String toString(){
			return "Select: "+authenticationAlgorithm+", Avail: "+ Arrays.toString(authenticationAlgorithms);
		}

	}

	@ConfigureMe(allfields = true)
	public static class AuthenticationAlgorithmConfig{
		@Configure private String clazz;
		@Configure private String shortcut;
		@Configure private String key;

		public String getClazz() {
			return clazz;
		}

		public void setClazz(String clazz) {
			this.clazz = clazz;
		}

		public String getShortcut() {
			return shortcut;
		}

		public void setShortcut(String shortcut) {
			this.shortcut = shortcut;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return "AuthenticationAlgorithmConfig{" +
					"clazz='" + clazz + '\'' +
					", shortcut='" + shortcut + '\'' +
					", key='" + key + '\'' +
					'}';
		}
	}
}
