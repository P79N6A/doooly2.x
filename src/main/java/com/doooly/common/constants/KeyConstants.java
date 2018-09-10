package com.doooly.common.constants;

import java.util.ResourceBundle;

/**
 * @Description: key常量
 * @author: qing.zhang
 * @date: 2018-07-25
 */
public class KeyConstants {
	private static ResourceBundle appBundle = ResourceBundle.getBundle("prop/key");

	public static final String RSA_PRIBKEY = appBundle.getString("rsa.pribKey");// rsa私钥

	public static final String RSA_PUBKEY = appBundle.getString("rsa.pubKey");// rsa公钥

	public static final String AES_KEY = appBundle.getString("aes.key");// aeskey

	public static final String CHARSET = "UTF-8";// aeskey

}
