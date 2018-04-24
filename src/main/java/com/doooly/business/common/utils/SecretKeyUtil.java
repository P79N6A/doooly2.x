package com.doooly.business.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.doooly.common.constants.Constants.SecretKeyOwner;
import com.doooly.common.constants.Constants.SecretKeyStatus;
import com.doooly.common.util.RSAUtils;
import com.doooly.entity.doooly.SecretKey;

/**
 * 密钥生成工具类
 * @author 赵清江
 * @date 2016年7月12日
 * @version 1.0
 */
public class SecretKeyUtil {

	public static final String SERVER ="server";
	
	public static final String CLIENT = "client";
	
	/**
	 * 生成服务器和客服端密钥(公钥和私钥)(两对)
	 * @return List<SecretKey>
	 */
	public static Map<String, SecretKey> generateSecretKeyMap(){
		Map<String, SecretKey> keys = new HashMap<>();
		keys.put(SERVER, getNewKey(SecretKeyOwner.Server));
		keys.put(CLIENT, getNewKey(SecretKeyOwner.Client));
		return keys;
	}
	/**
	 * 生成服务器和客服端密钥(公钥和私钥)(两对)
	 * @return List<SecretKey>
	 */
	public static List<SecretKey> generateSecretKeyList(){
		List<SecretKey> keys = new ArrayList<>();
		keys.add(getNewKey(SecretKeyOwner.Server));
		keys.add(getNewKey(SecretKeyOwner.Client));
		return keys;
	}
	/**
	 * 根据参数生成密钥对
	 * @param SecretKeyOwner owner
	 * @return SecretKey
	 */
	private static SecretKey getNewKey(SecretKeyOwner owner){
		Map<String, String> keys = RSAUtils.getKeys();
		SecretKey secretKey = new SecretKey();
		secretKey.setCreateDate(new Date());
		secretKey.setOwner(owner);
		secretKey.setIsValid(SecretKeyStatus.VALID);
		secretKey.setPrivateKey(keys.get(RSAUtils.PRIVATE_KEY));
		secretKey.setPublicKey(keys.get(RSAUtils.PUBLIC_KEY));
		return secretKey;
	}
	
}
