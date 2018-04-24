package com.doooly.common.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;


/**
 * RSA加密算法工具类
 * @author Albert
 *
 */
public class RSAUtils {
	
	private static final String KEY_ALGORITHM = "RSA";
	
	private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	
	public static final String PUBLIC_KEY = "publicKey";
	
	public static final String PRIVATE_KEY = "privateKey";
	
	public static final String ENCODE_FAILED = "encodeFailed";
	
	public static final String DECODE_FAILED = "decodeFailed";
	
	public static void main(String[] args){
		String text = "this is a test message ! 测试信息!";
		Map<String, String> map = RSAUtils.getKeys();
		String encode = RSAUtils.RSAEncode(map.get(PUBLIC_KEY), text);
		System.out.println(encode);
		List<String> keys = RSAUtils.encodeKeys(map.get(PUBLIC_KEY),map.get(PRIVATE_KEY));
		System.out.println(keys.size());
		System.out.println(keys.get(0));
		System.out.println(keys.get(1));
		List<String> privatekey = RSAUtils.decodeKeys(keys.get(1)); 
		String decodeStr = RSADecode(privatekey.get(0), encode);
		System.out.println(decodeStr);
	}
	
	
	
	/** 
	 * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 
	 */
	private static int keySize = 1024;
	
	/**
	 * 得到字符串形式的公钥和私钥
	 * @return
	 */
	public static Map<String, String> getKeys(){
		return generateKeyBytes();
	}
	
	/**
	 * base64加密公钥和私钥
	 * @param keys
	 * @return
	 */
	public static List<String> encodeKeys(String... keys){
		List<String> result = new ArrayList<String>();
		try {
			for(int i = 0; i < keys.length; i++){
				result.add(convertStringToASCII(keys[i]));
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	/**
	 * base64解密公钥和私钥
	 * @param keys
	 * @return
	 */
	public static List<String> decodeKeys(String... keys){
		List<String> result = new ArrayList<String>();
		try {
			for(int i = 0; i < keys.length; i++){
				result.add(convertASCIIToString(keys[i]));
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	/**
	 * 加密
	 * 
	 * @param key
	 * @param plainText
	 * @return
	 */
	public static String RSAEncode(String publicKey, String text) {
		PublicKey key = restorePublicKey(Base64.decodeBase64(publicKey));
		byte[] plainText = text.getBytes();
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return Base64.encodeBase64String(cipher.doFinal(plainText));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | 
				InvalidKeyException | IllegalBlockSizeException | 
				BadPaddingException e) {
			
			e.printStackTrace();
			return ENCODE_FAILED;
		}
	}
	 
	/**
	 * 解密
	 * 
	 * @param key
	 * @param encodedText
	 * @return
	 */
	public static String RSADecode(String privateKey, String content) {
		PrivateKey key = restorePrivateKey(Base64.decodeBase64(privateKey));
		byte[] encodedText = Base64.decodeBase64(content);
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(encodedText));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | 
				InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {

			e.printStackTrace();
			return DECODE_FAILED;
		}
		
	}
	/**
	 * 生成公钥和私钥
	 * @return
	 */
	private static Map<String, String> generateKeyBytes() {

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(keySize);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			Map<String, String> keyMap = new HashMap<String, String>();
			keyMap.put(PUBLIC_KEY, Base64.encodeBase64String(publicKey.getEncoded()));
			keyMap.put(PRIVATE_KEY, Base64.encodeBase64String(privateKey.getEncoded()));
			return keyMap;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
	 * 
	 * @param keyBytes
	 * @return
	 */
	private static PublicKey restorePublicKey(byte[] keyBytes) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
	 * 
	 * @param keyBytes
	 * @return
	 */
	private static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 将String转换为ASCII码,每个字符之间以"_"分割
	 * @param data
	 * @return
	 */
	private static String convertStringToASCII(String data){
		// 将json字符串转换为ASCII码
		StringBuffer strBuffer = new StringBuffer();
		char[] charArray = data.toCharArray();
		int length = charArray.length;
		for (int i = 0; i < length; i++) {
			if (i != length - 1) {
				strBuffer.append((int) charArray[i]).append("_");
			} else {
				strBuffer.append((int) charArray[i]);
			}
		}
		return strBuffer.toString();
	}
	/**
	 * 将ASCII转换为String
	 * @param data
	 * @return
	 */
	private static String convertASCIIToString(String data){
		StringBuffer strBuffer = new StringBuffer();
		String[] strs = data.split("_");
		int length = strs.length;
		for (int i = 0; i < length; i++) {
			strBuffer.append((char)Integer.parseInt(strs[i]));
		}
		return strBuffer.toString();
	}

	public static int getKeySize() {
		return keySize;
	}

	public static void setKeySize(int keySize) {
		RSAUtils.keySize = keySize;
	}

	// ============================== RSA签名 与 验签方法如下 ==============================
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	public static final String ALGORITHM = "RSA";

	public static String sign(String content, String privateKey, String charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(charset));
			byte[] signed = signature.sign();
			return Base64.encodeBase64String(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean doCheck(String content, String sign, String publicKey, String charset) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			byte[] encodedKey = Base64.decodeBase64(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(charset));

			boolean bverify = signature.verify(Base64.decodeBase64(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
