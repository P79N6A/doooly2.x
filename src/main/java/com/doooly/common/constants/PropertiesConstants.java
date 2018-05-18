package com.doooly.common.constants;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class PropertiesConstants {
	public static ResourceBundle smsBundle = ResourceBundle.getBundle("prop/sms");
	public static ResourceBundle dooolyBundle = ResourceBundle.getBundle("doooly");
	public static ResourceBundle sctcdBundle = ResourceBundle.getBundle("prop/sctcd");
	public static ResourceBundle wechatPushBundle = ResourceBundle.getBundle("prop/wechatPush");

	 /**
	  * 处理中文乱码
	  * @param args
	  */
	 public static String string2Utf8(String str){
		 try {
//			 if(str.contains("\\?"))
				 return new String(str.getBytes("ISO-8859-1"),"utf-8");
//			 else
//				 return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 return null;
	 }
}
