package com.doooly.business.shanghaibank.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 密钥生成器
 */
public class RSAKeysGenerator {
	/**
	 * RsaKey 密钥生成器
	 * 
	 * @return stirng[0]-pubKey string[1]-privKey
	 * @throws Exception
	 * @author guolei
	 */
	public static String[] generateRsaKeys() throws Exception {
		String[] keys = new String[2];
		String pubKeyStr = "", priKeyStr = "";
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);// 1024 bit
		KeyPair pair = generator.generateKeyPair();
		PublicKey pubKey = pair.getPublic();
		PrivateKey privKey = pair.getPrivate();
		pubKeyStr = Base64.encode(pubKey.getEncoded()).replaceAll(" ", "");
		priKeyStr = Base64.encode(privKey.getEncoded()).replaceAll(" ", "");

		keys[0] = pubKeyStr; // public Key
		keys[1] = priKeyStr; // private key

		return keys;
	}
	public static void main(String[] args) {
		try {
			String[] keys = generateRsaKeys();
			System.out.println("Public Key::" + keys[0]);
			System.out.println("Private Key::" + keys[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
