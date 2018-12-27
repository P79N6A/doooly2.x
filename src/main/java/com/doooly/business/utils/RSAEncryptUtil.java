package com.doooly.business.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类名称：RSAEncryptUtil
 * 类描述：RSA加密解密工具类
 * @author aboruo
 * @date 2017年3月14日 下午6:03:29
 */
@Component
public class RSAEncryptUtil {
	/** RSA 非对称加密方式 */
	public static final String RSA_KEY_ALGORITHM = "RSA";
	public static final String RSA_CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	public static final String RSA_SIGN_ALGORITHM = "SHA1withRSA";
	/** 钥匙对的初始化大小 */
	public static final int KEYPAIR_KEYSIZE = 1024;
	/** 编码过程中涉及到的字符编码格式 */
	public static final String CHARACTERENCODING = "UTF-8";
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(RSAEncryptUtil.class);
	/** RSA 公钥、私钥对 */
	public static final String RSAPUBLICKEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMeROxYWBQada7c4PY+oBZ07aj8426815padAjwTOlfbisdpIE+AVPfEatdaktPUgfke5/dLTJ1s1MDaygtIgmDgzhAuYRA7QZ9abfSH2u9aQcmWLQRyZMKZ9alEeQwfVAHXjxI4HCKSlND/9iJD119bKoSPao9KqVHwec8wOnWQIDAQAB";
	public static Map<String,byte[]> generateKeyPair(String algorithmStr) throws Exception{
		Map<String,byte[]> keyPairMap = new ConcurrentHashMap<String,byte[]>();
		/** 1. initiallize KeyPairGenerator wiht rsa and parameter */
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_KEY_ALGORITHM);
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
		secureRandom.setSeed((algorithmStr).getBytes(CHARACTERENCODING));
		keyPairGenerator.initialize(KEYPAIR_KEYSIZE, secureRandom);
		
		/** 2. get the KeyPair */
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		/** 3. generate publickey and privatekey,and then put them into the result map */
		byte[] publicKeybytes = keyPair.getPublic().getEncoded();
		keyPairMap.put("publicKeybytes", publicKeybytes);
		
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		keyPairMap.put("privateKeyBytes", privateKeyBytes);
		
		/** 4. collect all of unused obj */
		publicKeybytes = null;
		privateKeyBytes = null;
		keyPairGenerator = null;
		secureRandom = null;
		
		/** 5. return the keyPairMap */
		return keyPairMap;
	}
	/**
	 * @Title: encryptByRSAPubKey
	 * @Description: 使用rsa 公钥进行紧密
	 * @author:aboruo
	 * @param pubKeyByte
	 * @param data
	 * @return byte[]
	 * @date 2017年3月14日 下午3:38:36
	 */
	public static byte[] encryptByRSAPubKey(byte[] pubKeyByte,byte[] data){
		if(pubKeyByte == null || pubKeyByte.length < 1 || data == null || data.length < 1) return null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyByte);
			PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
			Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 加密时超过117字节就报错。为此采用分段加密的办法来加密 
			byte[] enBytes = null;
			for (int i = 0; i < data.length; i += 64) {  
			// 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
			    byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i,i + 64));
			    enBytes = ArrayUtils.addAll(enBytes, doFinal);
			}
			return enBytes;
		} catch (NoSuchAlgorithmException e) {
			logger.error("encryptByRSAPubKey错误：" + e.getMessage());
			return null;
		} catch (InvalidKeySpecException e) {
			logger.error("encryptByRSAPubKey错误：" + e.getMessage());
			return null;
		} catch (NoSuchPaddingException e) {
			logger.error("encryptByRSAPubKey错误：" + e.getMessage());
			return null;
		} catch (InvalidKeyException e) {
			logger.error("encryptByRSAPubKey错误：" + e.getMessage());
			return null;
		} catch (IllegalBlockSizeException e) {
			logger.error("encryptByRSAPubKey错误：" + e.getMessage());
			return null;
		} catch (BadPaddingException e) {
			logger.error("encryptByRSAPubKey错误：" + e.getMessage());
			return null;
		}
	}
	/**
	 * @Title:encryptByRSAPubKey
	 * @Description: 使用rsa 公钥进行紧密
	 * @author:aboruo
	 * @param pubKeyByte
	 * @param source
	 * @return String
	 * @date 2017年3月14日 下午5:40:28
	 */
	public static String encryptByRSAPubKey(byte[] pubKeyByte,String source){
		if(pubKeyByte == null || pubKeyByte.length < 1 || source == null || source.length() < 1) return null;
		byte[] result = null;
		try {
			result = encryptByRSAPubKey(pubKeyByte, source.getBytes(CHARACTERENCODING));
			return result == null ? null :  Base64.encodeBase64String(result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @Title:encryptByRSAPriKey
	 * @Description: 使用rsa 私钥进行加密
	 * @author:aboruo
	 * @param priKeyBytes
	 * @param data
	 * @return byte[]
	 * @date 2017年3月14日 下午3:58:41
	 */
	public static byte[] encryptByRSAPriKey(byte[] priKeyBytes,byte[] data){
		if(priKeyBytes == null || priKeyBytes.length < 1 || data == null || data.length < 1) return null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
			PrivateKey privateKey = keyFactory.generatePrivate(priKeySpec);
			Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			// 加密时超过117字节就报错。为此采用分段加密的办法来加密 
			byte[] enBytes = null;
			for (int i = 0; i < data.length; i += 64) {  
			// 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
			    byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i,i + 64));
			    enBytes = ArrayUtils.addAll(enBytes, doFinal);
			}
			return enBytes;
		} catch (NoSuchAlgorithmException e) {
			logger.error("encryptByRSAPriKey错误：" + e.getMessage());
			return null;
		} catch (InvalidKeySpecException e) {
			logger.error("encryptByRSAPriKey错误：" + e.getMessage());
			return null;
		} catch (NoSuchPaddingException e) {
			logger.error("encryptByRSAPriKey错误：" + e.getMessage());
			return null;
		} catch (InvalidKeyException e) {
			logger.error("encryptByRSAPriKey错误：" + e.getMessage());
			return null;
		} catch (IllegalBlockSizeException e) {
			logger.error("encryptByRSAPriKey错误：" + e.getMessage());
			return null;
		} catch (BadPaddingException e) {
			logger.error("encryptByRSAPriKey错误：" + e.getMessage());
			return null;
		}
	}
	/**
	 * @Title:encryptByRSAPriKey
	 * @Description: 使用rsa 私钥进行加密
	 * @author:aboruo
	 * @param priKeyBytes
	 * @param source
	 * @return String
	 * @date 2017年3月14日 下午5:40:50
	 */
	public static String encryptByRSAPriKey(byte[] priKeyBytes,String source){
		if(priKeyBytes == null || priKeyBytes.length < 1 || source == null || source.length() < 1) return null;
		byte[] result = null;
		try {
			result = encryptByRSAPriKey(priKeyBytes, source.getBytes(CHARACTERENCODING));
			return result == null ? null :  Base64.encodeBase64String(result);
		} catch (UnsupportedEncodingException e) {
			logger.error("encryptByRSAPriKey错误：" + e.getMessage());
		}
		return null;
	}
	/**
	 * @Title:decryptByRSAPriKey
	 * @Description: 用私钥解密消息
	 * @author:aboruo
	 * @param priKeyBytes
	 * @param data
	 * @return byte[]
	 * @date 2017年3月14日 下午4:07:42
	 */
	public static byte[] decryptByRSAPriKey(byte[] priKeyBytes,byte[] data){
		if(priKeyBytes == null || priKeyBytes.length < 1 || data == null || data.length < 1) return null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
			PrivateKey privateKey = keyFactory.generatePrivate(priKeySpec);
			Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 解密时超过128字节就报错。为此采用分段解密的办法来解密
			byte[] retByte = null;
			for (int i = 0; i < data.length; i += 128) {
			    byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
			    retByte = ArrayUtils.addAll(retByte, doFinal);
			}
			return retByte;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			logger.error("decryptByRSAPriKey错误：" + e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error("decryptByRSAPriKey错误：" + e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error("decryptByRSAPriKey错误：" + e.getMessage());
		} catch (IllegalBlockSizeException e) {
			logger.error("decryptByRSAPriKey错误：" + e.getMessage());
		} catch (BadPaddingException e) {
			logger.error("decryptByRSAPriKey错误：" + e.getMessage());
		}
		return null;
	}
	/**
	 * @Title:decryptByRSAPriKey
	 * @Description: 用私钥解密消息
	 * @author:aboruo
	 * @param priKeyBytes
	 * @param data
	 * @return String
	 * @date 2017年3月14日 下午6:01:18
	 */
	public static String decryptByRSAPriKey(byte[] priKeyBytes,String data){
		if(priKeyBytes == null || priKeyBytes.length < 1 || data == null || data.length() < 1) return null;
		byte[] result = null;
		try {
			result = decryptByRSAPriKey(priKeyBytes, Base64.decodeBase64(data));
			return result == null ? null :  new String(result,CHARACTERENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error("decryptByRSAPriKey错误：" + e.getMessage());
		}
		return null;
	}
	/**
	 * @Title:decryptByRSAPubKey
	 * @Description: 用rsa公钥解密
	 * @author:aboruo
	 * @param pubKeyBytes
	 * @param data
	 * @return byte[]
	 * @date 2017年3月14日 下午4:34:43
	 */
	public static byte[] decryptByRSAPubKey(byte[] pubKeyBytes,byte[] data){
		if(pubKeyBytes == null || pubKeyBytes.length < 1 || data == null || data.length < 1) return null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
			PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
			Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			// 解密时超过128字节就报错。为此采用分段解密的办法来解密
			byte[] retByte = null;
			for (int i = 0; i < data.length; i += 128) {
			    byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
			    retByte = ArrayUtils.addAll(retByte, doFinal);
			}
			return retByte;
		} catch (NoSuchAlgorithmException e) {
			logger.error("decryptByRSAPubKey错误：" + e.getMessage());
		} catch (InvalidKeySpecException e) {
			logger.error("decryptByRSAPubKey错误：" + e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error("decryptByRSAPubKey错误：" + e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error("decryptByRSAPubKey错误：" + e.getMessage());
		} catch (IllegalBlockSizeException e) {
			logger.error("decryptByRSAPubKey错误：" + e.getMessage());
		} catch (BadPaddingException e) {
			logger.error("decryptByRSAPubKey错误：" + e.getMessage());
		}
		return null;
	}
	public static String decryptByRSAPubKey(byte[] pubKeyBytes,String data){
		if(pubKeyBytes == null || pubKeyBytes.length < 1 || data == null || data.length() < 1) return null;
		byte[] result = null;
		try {
			result = decryptByRSAPubKey(pubKeyBytes, Base64.decodeBase64(data));
			return result == null ? null :  new String(result,CHARACTERENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error("decryptByRSAPubKey错误：" + e.getMessage());
		}
		return null;
	}
	/**
	 * @Title:MdigestSHA
	 * @Description: 计算字符串的SHA数字摘要，以byte[]形式返回
	 * @author:aboruo
	 * @param source
	 * @return byte[]
	 * @date 2017年3月14日 下午5:18:34
	 */
	public static byte[] MdigestSHA(String source) {
		if(source == null || source.length() < 1) return null;
		 try {
		  MessageDigest thisMD = MessageDigest.getInstance("SHA");
		  byte[] digest = thisMD.digest(source.getBytes(CHARACTERENCODING));
		  return digest;
		 } catch (Exception e) {
			 logger.error("MdigestSHA错误：" + e.getMessage());
		  return null;
		 }
	}
	/**
	 * @Title:rsa_sign
	 * @Description: 使用私钥对消息进行签名
	 * @author:aboruo
	 * @param priKeyInByte
	 * @param source
	 * @return byte[]
	 * @date 2017年3月14日 下午4:49:35
	 */
	public static byte[] rsa_sign(byte[] priKeyInByte,byte[] source){
		if(priKeyInByte == null || priKeyInByte.length < 1 || source == null || source.length < 1) return null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
			PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priKeyInByte);
			PrivateKey privateKey = keyFactory.generatePrivate(priKeySpec);
			Signature signature = Signature.getInstance(RSA_SIGN_ALGORITHM);
			signature.initSign(privateKey);
			signature.update(source);
			return signature.sign();
		} catch (NoSuchAlgorithmException e) {
			logger.error("rsa_sign方法数字签名出错：" + e.getStackTrace().toString());
		} catch (InvalidKeySpecException e) {
			logger.error("rsa_sign方法数字签名出错：" + e.getStackTrace().toString());
		} catch (SignatureException e) {
			logger.error("rsa_sign方法数字签名出错：" + e.getStackTrace().toString());
		} catch (InvalidKeyException e) {
			logger.error("rsa_sign方法数字签名出错：" + e.getStackTrace().toString());
		}
		return null;
	}
	/**
	 * @Title:rsa_verify
	 * @Description: 用rsa公钥验证数字签名
	 * @author:aboruo
	 * @param pubKeyBytes
	 * @param source
	 * @param sign
	 * @return boolean
	 * @date 2017年3月14日 下午5:06:46
	 */
	public static boolean rsa_verify(byte[] pubKeyBytes,byte[] source,byte[] sign){
		if(pubKeyBytes == null || pubKeyBytes.length < 1 || source == null || source.length < 1|| sign == null || sign.length < 1) return false;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
			PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
			Signature signature = Signature.getInstance(RSA_SIGN_ALGORITHM);
			signature.initVerify(publicKey);
			signature.update(source);
			return signature.verify(sign);
		} catch (NoSuchAlgorithmException e) {
			logger.error("rsa_verify方法数字签名出错：" + e.getStackTrace().toString());
		} catch (InvalidKeySpecException e) {
			logger.error("rsa_verify方法数字签名出错：" + e.getStackTrace().toString());
		} catch (InvalidKeyException e) {
			logger.error("rsa_verify方法数字签名出错：" + e.getStackTrace().toString());
		} catch (SignatureException e) {
			logger.error("rsa_verify方法数字签名出错：" + e.getStackTrace().toString());
		}
		return false;
	}
	
	/*public static void main(String[] args){
		Map<String,byte[]> keyPairMap = null;
		try {
			//** 1 获取公钥，私钥对 *//*
			keyPairMap = generateKeyPair("zmit_v2_2017");
			byte[] privateKeyBytes = keyPairMap.get("privateKeyBytes");
			byte[] publicKeybytes0 = keyPairMap.get("publicKeybytes");
			
			//** 验证Base64 编码再解码后数组是否一样 *//*
			String privateKeyBytesBase64 = Base64.encodeBase64String(privateKeyBytes);
			String privateKeyBytesBase640 = Base64.encodeBase64String(publicKeybytes0);
			byte[] privateKeyBytesTmp = Base64.decodeBase64(privateKeyBytesBase64);
			System.out.println(Arrays.equals(privateKeyBytes,privateKeyBytesTmp));
			System.out.println("privateKeyBytesBase64:" + privateKeyBytesBase64);
			
			//** 2 数字签名验证 *//*
			String testMessage = "这是一个数字签名验证消息";
			byte[] source = MdigestSHA(testMessage);
			byte[] messageSign = rsa_sign(keyPairMap.get("privateKeyBytes"), source);
			String messageSignStr = Base64.encodeBase64String(messageSign);
			System.out.println("messageSignStr:" + messageSignStr);
			byte[] publicKeybytes = keyPairMap.get("publicKeybytes");
			boolean result = rsa_verify(keyPairMap.get("publicKeybytes"), source, Base64.decodeBase64(messageSignStr));
			System.out.println("消息的数字签名验证结果为：" + result);
			
			//** 3 rsa消息加密解密 *//*
//			byte[] messageByte = testMessage.getBytes(CHARACTERENCODING);
			String encryptResult = encryptByRSAPriKey(privateKeyBytes, testMessage);
			System.out.println("encryptResult:" + encryptResult);
			String descryptResut = decryptByRSAPubKey(publicKeybytes, encryptResult);
			System.out.println("解密结果：" + descryptResut);
			System.out.println("publicKeybytesBase64:" + Base64.encodeBase64String(keyPairMap.get("publicKeybytes")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
/*	public static void main(String[] args) {
		 JSONObject backJson = new JSONObject();
	    	backJson.put("mobile", "11000000000");
	    	backJson.put("channelCode", "0000");
	    	String ss = encryptByRSAPubKey(Base64.decodeBase64("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/0zAG4XN6ogMqUwg9Wz68/SsgkN6020IXOkdrWPG9eY5C3VhdekAkm3Yxjg+kF0I1/slUaEHo8zRjCHBfs1vse65O561AG+qPqPPKwxToAxU4Q6R4FHc5cnYRveUczRLwQwM3LQKgWTK1StxA3iiNR99m4D+AoDC7793Al77h8QIDAQAB"),backJson.toJSONString());
			System.out.println("输出公钥加密串：" + ss);
			String sss = decryptByRSAPriKey(Base64.decodeBase64("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL/TMAbhc3qiAypTCD1bPrz9KyCQ3rTbQhc6R2tY8b15jkLdWF16QCSbdjGOD6QXQjX+yVRoQejzNGMIcF+zW+x7rk7nrUAb6o+o88rDFOgDFThDpHgUdzlydhG95RzNEvBDAzctAqBZMrVK3EDeKI1H32bgP4CgMLvv3cCXvuHxAgMBAAECgYB6h2WYH45p+O7YrC0JNC2A/p37sU3wbFVtDNbt5OSVSP3sDAMTwrESMRNwV/P5trmeT98+QVJIgIEn+Q3+s0y1R90aXcHjTX/J+OXvKlqgbKwGN7ziUP7ZS2bZfAg745yGePU2fN3GEzeQJfIZCnihXwGv0ZXVQNjWW+W8NAf5WQJBAO+Q7dqbApCjrliojcXUmkriRErgN7o84GAY2JjnKwMO2J76HYYaKymIR2DArY+Ujf5/wvtlz0iTjV4J7Sm9GN8CQQDM+92ddwO7w8hriDtvKBgnTLSRxxarzJ/4qqmQOEwmc+qyPL3Pu335Vt+TuJYte+Q8D2bqk5G4yTq48MZ3dc8vAkEA3921B0U3PmZWc+7+nNGxZSzP2JByQwzEN8jADxJmdTMYZmcf08L8dpEs8lLbXYLateo6EKbw7sdDoY7xGsHkTQJBAKDR5c/5pNM2SlKbQk32PeTvWCea8917n/I/KuM8V/o/LV4DK8QDzFTH5IcH5cLvlWPNjafyePxp3Alm1b65jf8CQA/O33UMYh7d396bfiDn8yeiG7mdvi61h1p+74WhZ685EMGUXPRkvderCYYJvMz8VTZPBAn2Ilh0zeYgEWt+qzU="),ss);
			System.out.println("输出私钥解密串：" + sss);
	    	
	    	
	    	
//		String ss = encryptByRSAPriKey(Base64.decodeBase64("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL/TMAbhc3qiAypTCD1bPrz9KyCQ3rTbQhc6R2tY8b15jkLdWF16QCSbdjGOD6QXQjX+yVRoQejzNGMIcF+zW+x7rk7nrUAb6o+o88rDFOgDFThDpHgUdzlydhG95RzNEvBDAzctAqBZMrVK3EDeKI1H32bgP4CgMLvv3cCXvuHxAgMBAAECgYB6h2WYH45p+O7YrC0JNC2A/p37sU3wbFVtDNbt5OSVSP3sDAMTwrESMRNwV/P5trmeT98+QVJIgIEn+Q3+s0y1R90aXcHjTX/J+OXvKlqgbKwGN7ziUP7ZS2bZfAg745yGePU2fN3GEzeQJfIZCnihXwGv0ZXVQNjWW+W8NAf5WQJBAO+Q7dqbApCjrliojcXUmkriRErgN7o84GAY2JjnKwMO2J76HYYaKymIR2DArY+Ujf5/wvtlz0iTjV4J7Sm9GN8CQQDM+92ddwO7w8hriDtvKBgnTLSRxxarzJ/4qqmQOEwmc+qyPL3Pu335Vt+TuJYte+Q8D2bqk5G4yTq48MZ3dc8vAkEA3921B0U3PmZWc+7+nNGxZSzP2JByQwzEN8jADxJmdTMYZmcf08L8dpEs8lLbXYLateo6EKbw7sdDoY7xGsHkTQJBAKDR5c/5pNM2SlKbQk32PeTvWCea8917n/I/KuM8V/o/LV4DK8QDzFTH5IcH5cLvlWPNjafyePxp3Alm1b65jf8CQA/O33UMYh7d396bfiDn8yeiG7mdvi61h1p+74WhZ685EMGUXPRkvderCYYJvMz8VTZPBAn2Ilh0zeYgEWt+qzU="),backJson.toJSONString());
//		System.out.println("输出公钥加密串：" + ss);
//		String sss = decryptByRSAPubKey(Base64.decodeBase64("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/0zAG4XN6ogMqUwg9Wz68/SsgkN6020IXOkdrWPG9eY5C3VhdekAkm3Yxjg+kF0I1/slUaEHo8zRjCHBfs1vse65O561AG+qPqPPKwxToAxU4Q6R4FHc5cnYRveUczRLwQwM3LQKgWTK1StxA3iiNR99m4D+AoDC7793Al77h8QIDAQAB"),ss);
//		System.out.println("输出私钥解密串：" + sss);
		
	}*/
	
	public static void main(String[] args) throws Exception {
		Map<String,byte[]> keyPairMap = null;
			//** 1 获取公钥，私钥对 *//*
			keyPairMap = generateKeyPair("zmit_v2_itou");
			byte[] privateKeyBytes = keyPairMap.get("privateKeyBytes");
			byte[] publicKeybytes0 = keyPairMap.get("publicKeybytes");
			
			//** 验证Base64 编码再解码后数组是否一样 *//*
			String privateKeyBytesBase64 = Base64.encodeBase64String(privateKeyBytes);
			String publicKeybytesBase640 = Base64.encodeBase64String(publicKeybytes0);
			byte[] privateKeyBytesTmp = Base64.decodeBase64(privateKeyBytesBase64);
			System.out.println(Arrays.equals(privateKeyBytes,privateKeyBytesTmp));
//			System.out.println("privateKeyBytesBase64:" + privateKeyBytesBase64);
			
			System.out.println("privateKeyBytesBase64:" + privateKeyBytesBase64);
			System.out.println("publicKeybytesBase640:" + publicKeybytesBase640);
			
	}
}
