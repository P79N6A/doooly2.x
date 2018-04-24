/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.doooly.business.touristCard.utils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Rsa {

	private static final String ALGORITHM = "RSA";

	/**
	 * @param algorithm
	 * @param ins
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws AlipayException
	 */
	private static PublicKey getPublicKeyFromX509(String algorithm,
			String bysKey) throws NoSuchAlgorithmException, Exception {
		byte[] decodedKey = Base64.decode(bysKey);
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePublic(x509);
	}

	public static String encrypt(String content, String key, String charset) {
		try {
			PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, key);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);

			byte plaintext[] = content.getBytes(charset);
			byte[] output = cipher.doFinal(plaintext);

			String s = new String(Base64.encode(output));

			return s;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	public static String sign(String content, String privateKey, String charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA","BC");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();
//			Log.e("Base64.encode(signed)", "-->"+Base64.encode(signed));
			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	public static String getMD5(String content) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(content.getBytes());
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	public static boolean doCheck(String content, String sign, String publicKey, String charset) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(charset));
//			Log.i("Result", "content :   "+content);
//			Log.i("Result", "sign:   "+sign);
			boolean bverify = signature.verify(Base64.decode(sign));
//			Log.i("Result","bverify = " + bverify);
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 功能:利用公钥解析密文
	 * @param content
	 * @param key
	 * @return
	 */
	public static String pub_Calc(String sign, String key) {
		try {
			PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, key);
			byte[] signBy = ByteUtil.HexStr2Bytes(sign);
			Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
			//Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);

			//byte plaintext[] = content.getBytes("UTF-8");
			byte[] output = cipher.doFinal(signBy);
//			doCheckCalc(Util.bytes2Hex(output));
//			String s = new String(Base64.encode(output));
//			Log.e("ss", "-->"+Util.bytes2Hex(output));
			return doCheckCalc(Util.bytes2Hex(output));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 得到解密后的订单信息，包括：支付订单号、充值结果、支付金额
	 * 如：得到39303230313430353238303030303030303030300000002501，则：
	 * 3930323031343035323830303030303030303030：支付订单号
	 * 00000025：支付金额为25元
	 * 01：充值结果表示成功
	 * @param output
	 * @return
	 */
	public static String doCheckCalc(String output){
		String strOrderInfo = output.substring(206, 256);
		return strOrderInfo;
	}
	
	/**
	 * 
	 * @param strOrderInfo 通过公钥解析获取的订单信息
	 * @param sctcdRechargeOrderNum 原始支付订单号
	 * @param strMerchantOrderFee 原始交易金额
	 * @param payResult 原始支付结果
	 * @return
	 */
	public static boolean verifyOrderInfo(String strOrderInfo,String strMerchantOrderNum, String strMerchantOrderFee, String payResult){
		//将商户订单补齐40位
		strMerchantOrderNum = getAscii(strMerchantOrderNum);
		for(int i = strMerchantOrderNum.length();i<40;i++){
			strMerchantOrderNum += "0";
		}
		String contentOrderInfo = strMerchantOrderNum+getFeeHexStr2(strMerchantOrderFee)+payResult;
		contentOrderInfo = contentOrderInfo.toUpperCase();//将字符串中出现的字母都转成大写
//		Log.e("lib_contentOrderInfo", contentOrderInfo);
//		Log.e("lib_strOrderInfo", strOrderInfo);
		if(strOrderInfo.equals(contentOrderInfo)){
			return true;
		}
		return false;
	}
	/**
	 * 将字符串转成ASCII
	 * @param content
	 * @return
	 */
	public static String getAscii(String content) {
		  String result = "";
	        int max = content.length();
	        for (int i = 0; i < max; i++) {
	            char c = content.charAt(i);
	            String b = Integer.toHexString(c);
	            result = result + b;
	        }
	        return result;
	}
	/**
	 * 将十进制金额转成十六进制并补齐8位
	 * @param fee
	 * @return fee
	 */
	private static String getFeeHexStr2(String fee){
		try {
			int fe = Integer.valueOf(fee);
			fee = Integer.toHexString(fe*100)+"";//后台单位为分，顾需要将元转成分
			//fee = "000000"+fee;
			for(int i=fee.length();i<8;i++){
				fee = "0"+fee;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return fee;
	}
}
