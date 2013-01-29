package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class helps to find out which encryptor was used to encrypt a string. This is pretty straight forward by
 * adding a prefix to each string.
 * The downside of this approach is that we need to explicitely name all encryptors here. Someday in the future
 * we will provide a configurable way to handle with this problem.
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


	private Map<String, Class<AuthTokenEncryptionAlgorithm>> encryptorMap;
	private Map<String, String> encryptorConfigParameter;

	private static final Random rnd = new Random(System.currentTimeMillis());

	//public AuthTokenEncryptionAlgorithm getEncryptor

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
}
