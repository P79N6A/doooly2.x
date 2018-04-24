package com.doooly.common.util;

import java.security.MessageDigest;

/**
 * MD5生成工具类
 * @author 赵清江
 * @date 2016年8月18日
 * @version 1.0
 */
public class MD5Utils {

	private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	/**
	 * 支持的输出字符串编码方式
	 */
	private static final String[] CHARSET_TYPE = {"utf-8","utf8","gbk","gbk2312","iso8859-1","iso-8859-1","unicode"};
	/**
	 * 生成MD5签名长度
	 * ThirtyTwo 32位
	 * Sixteen 16位
	 * @author 赵清江
	 * @date 2016年8月18日
	 * @version 1.0
	 */
	public enum Length{
		ThirtyTwo,
		Sixteen
	}
	
	public static String encode(String str , Length length){
		if (str == null || str.equals("")) {
			return str;
		}
		if (length == Length.ThirtyTwo) {
			return encode32(str, "UTF-8");
		} else if (length == Length.Sixteen) {
			
		} else;
		return str;
	}
	
	public static String encode(String str , String charset, Length length){
		if (null == str || "".equals(str) || null == charset(charset)) {
			return str;
		}
		if (length == Length.ThirtyTwo) {
			return encode32(str, charset);
		} else if (length == Length.Sixteen) {
			
		} else;
		return str;
	}
	
	private static String charset(String charset){
		if (charset == null) {
			return null;
		}
		for (String str : CHARSET_TYPE) {
			if (str.equalsIgnoreCase(charset)) {
				return charset;
			}
		}
		return null;
	}
	
	private static String encode32(String string , String charset){
		try {
			byte[] stringByte = string.getBytes(charset);
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(stringByte);
			byte[] md5Byte = messageDigest.digest();
			int j = md5Byte.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byteValue = md5Byte[i];
				str[k++] = hexDigits[byteValue >>> 4 & 0xf];
				str[k++] = hexDigits[byteValue & 0xf];
			}
			return (new String(str));
		} catch (Exception e) {
			e.printStackTrace();
			return string;
		}
	}
	
	/**
     * 对字符串做(32位小写)MD5
     * @param string 需要处理的字符串
     * @return 处理后的字符串。
     */
    public static String encode(String string) {
        try {
            byte[] stringByte = string.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(stringByte);
            byte[] md5Byte = messageDigest.digest();
            int j = md5Byte.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byteValue = md5Byte[i];
                str[k++] = hexDigits[byteValue >>> 4 & 0xf];
                str[k++] = hexDigits[byteValue & 0xf];
            }
            return (new String(str));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	
}
