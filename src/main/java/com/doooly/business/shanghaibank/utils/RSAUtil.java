package com.doooly.business.shanghaibank.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 签名以及验签
 */
public class RSAUtil {

	private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);

	/** RSA:加密算法 */
	private static final String KEY_ALGORITHM = "RSA";

	/** SHA1WithRSA:用SHA算法进行签名，用RSA算法进行加密 */
	private static final String SIGN_ALGORITHMS = "SHA1withRSA";

	/**
	 * 生成报文体和报文体签名
	 * 
	 * @param privateKey
	 * @param content
	 * @param encoding
	 * @return
	 */
	public static String[] signData(String privateKey, String content, String encoding) {
		logger.info("原始报文内容:" + content);
		String param[] = new String[2];
		try {
			// 报文体使用base64编码
			content = Base64.encode(content.getBytes(encoding));
			// 删除base64编码中的空格
			content = content.replaceAll(" ", "");
			// 生成报文体签名
			String contentSign = envolopData(privateKey, content);
			// 报文体签名进行url编码
			 contentSign = URLEncoder.encode(contentSign, encoding);
			logger.info("报文体签名:" + contentSign);
			// 报文体进行url编码
			 content = URLEncoder.encode(content, encoding);
			logger.info("base64编码报文体:" + content);
			// 返回报文体签名
			param[0] = contentSign;
			// 返回报文体
			param[1] = content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return param;
	}

	/**
	 * 生成报文体签名
	 * 
	 * @param privateKey
	 * @param content
	 * @return
	 */
	private static String envolopData(String privateKey, String content) {
		// 报文体签名
		String contentSign = "";
		try {
			// 根据密钥字符创建密钥类
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory fac = KeyFactory.getInstance(KEY_ALGORITHM);
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
			// 创建签名算法类
			Signature sigEng = Signature.getInstance(SIGN_ALGORITHMS);
			sigEng.initSign(rsaPrivateKey);
			// 生成签名
			sigEng.update(content.getBytes("UTF-8"));
			byte[] signature = sigEng.sign();
			// base64编码[请求报文签名]
			contentSign = Base64.encode(signature);
			// 删除换行符[请求报文签名]
			contentSign = contentSign.replaceAll(" ", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentSign;
	}

	/**
	 * 验证返回的报文的签名
	 * @param publicKey 公钥
	 * @param sign 返回报文体的签名
	 * @param src 返回报文体
	 * @param encoding 编码格式
	 * @return
	 */
	public static boolean rsaVerify(String publicKey, String sign, String src, String encoding) {
		// 验签结果
		boolean result = false; 
		try {
			// url解密[返回报文签名]
			sign = URLDecoder.decode(sign, encoding);
			// base64解密[返回报文签名]
			byte[] signature = Base64.decode(sign);
			// url解密[返回报文]
			src = URLDecoder.decode(src, encoding);
			// 根据密钥创建密钥类
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey));
			KeyFactory fac = KeyFactory.getInstance(KEY_ALGORITHM);
			RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);
			// 创建签名方法类
			Signature sigEng = Signature.getInstance(SIGN_ALGORITHMS);
			sigEng.initVerify(rsaPubKey);
			// 生成签名
			sigEng.update(src.getBytes(encoding));
			// 验证签名是一致
			result = sigEng.verify(signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解密返回的报文体信息
	 * 
	 * @param content
	 * @param encoding
	 * @return
	 */
	public static String decodeContent(String content, String encoding){
		String result = "";
		try {
			// 返回报文体的签名执行url解密
			result = URLDecoder.decode(content, encoding);
			// 返回报文体的签名执行base64解密
			result = new String(Base64.decode(result), encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 测试类
	 * @param args
	 */
	public static void main(String[] args) {
		String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAd7BxXHzA50Nsq5uA83DuRJjg/99c5UIxI+awBrL8qB125rmcz5/JOZfnAtTGIQzMdzCdETVgKCjb1gAS9v+AKfdt/0dRybMQzqPzxUTmhUULo0BQvUdCy+nxw0AvWJjjZovVwuB8gm4MbI5d7a8LBRolLu8lZTx7ts0ANFBkJQIDAQAB";
		String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMB3sHFcfMDnQ2yrm4DzcO5EmOD/31zlQjEj5rAGsvyoHXbmuZzPn8k5l+cC1MYhDMx3MJ0RNWAoKNvWABL2/4Ap923/R1HJsxDOo/PFROaFRQujQFC9R0LL6fHDQC9YmONmi9XC4HyCbgxsjl3trwsFGiUu7yVlPHu2zQA0UGQlAgMBAAECgYEAgCVwyhmNSpbwEoOz1si/kTUFIRHcMyrFxpgjuWHMfGKms4rtXJX0ynLZS8tbxcstjVlyebGCe1Jh2fUDOZ1RAWb8jtpCf2REAwSDa1jvls8GMq+64O+26I5gvFGYrIx2Yk0nR/VoXVF10uBRJ1Fnb27K2FXcSRyB8g0hqmVAjoECQQD/GSBtjC7M1wBeisMYE5IXY//qJaOgeG/NJDq6WZ5jOp9PCrzX0KHiiZfKim7oJtdzvfB+C1k/bof8/GTW4svhAkEAwSXhKCSr7uHmapq+JE1GbWRgflyrI0Yqg+OyRNkhW8ND46HN6xDe3goDYN0jakjvLTYfk/HszEOpR+rhpPmAxQJASVBcXH+EV8SWy5UPutStEOHhg9arbwwN/aQi6Lmm5pfLfzjzywaf7+5hXIlqlHfNRetZua/jR3KpzJBxHrzxoQJAb5CQuJ5ODaLAdC1DzTYxivhi4Dpow+xAnIQGMcx1f0qPyUlp5vCMuxZvkyDI1xifxq6vmMYx8F7Yxei/Q4q2vQJAa2AQCuZHxy7NWp2Zn2M3JxYs+ekWUkOtcaJOum0oxrlq3aECbLhz3vOBcsfUVPM4who/IACOEU9pFhGBHyi8xg==";
		String content ="{\"amt\":\"4.00\",\"detailId\":\"afc8954c-ccc2-4543-87e6-b6308d5517bf\",\"noticeId\":\"d7c15340-0540-4b27-b840-2138e7aefee5\",\"otherAccName\":\"测试客户122120\",\"otherAccNo\":\"03002792221\",\"purpose\":\"测试环境虚账户打款激活\",\"tranDirection\":\"2\",\"tranTime\":\"20180523111130\",\"virAccName\":\"擎苍\",\"virAccNo\":\"3111110300285872900006\"}";
		String[] signData = signData(priKey,content,"UTF-8");
        logger.info(decodeContent(signData[1],"UTF-8"));
		rsaVerify(pubKey,signData[0],signData[1],"UTF-8");
	}
}
