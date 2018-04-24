package com.doooly.common.constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.doooly.common.webservice.WebService;

public class SSLConfigConstants {
	public static InputStream TRUSTSTORE_INPUTSTREAM;
	public static InputStream P12_INPUTSTREAM;
	public static String TRUSTSTORE_PASSWORD = "qscesz123456";
	public static String P12_PASSWORD = "qweasdzxc123456";
	public static String TRUSTSTORE_TYPE_JKS = "JKS";
	public static String P12_TYPE_PKCS12 = "PKCS12";
	static {
		try {
			//TRUSTSTORE_INPUTSTREAM = ClassLoader.getSystemResource("ssl/client.truststore").openStream();
			//P12_INPUTSTREAM = ClassLoader.getSystemResource("ssl/liketry.p12").openStream();
			
			//TRUSTSTORE_INPUTSTREAM = new FileInputStream("/Users/john/Documents/workspace/Doooly/src/main/resources/ssl/client.truststore");
			//P12_INPUTSTREAM = new FileInputStream("/Users/john/Documents/workspace/Doooly/src/main/resources/ssl/liketry.p12");
			
			TRUSTSTORE_INPUTSTREAM =  new FileInputStream(getRootPath() + "/SSL/client.truststore");
			P12_INPUTSTREAM = new FileInputStream(getRootPath() + "/SSL/liketry.p12");
			
//	        URL classPath = Thread.currentThread().getContextClassLoader().getResource("");  
//	        String proFilePath = classPath.toString();  
//	        proFilePath = proFilePath.substring(6);   
//	        proFilePath = proFilePath.replace("/", java.io.File.separator);  
//	        if(!proFilePath.endsWith(java.io.File.separator)){  
//	            proFilePath = proFilePath + java.io.File.separator;  
//	        } 
//			TRUSTSTORE_INPUTSTREAM = new FileInputStream(proFilePath + "ssl/client.truststore");
//			P12_INPUTSTREAM = new FileInputStream(proFilePath + "ssl/liketry.p12");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getRootPath() {
		String classPath = WebService.class.getClassLoader().getResource("/").getPath();
		String rootPath = "";
		//windows下
		if("\\".equals(File.separator)){
		rootPath = classPath.substring(1,classPath.indexOf("/WEB-INF"));
		rootPath = rootPath.replace("%20", " ");
		}
		//linux下
		if("/".equals(File.separator)){
		rootPath = classPath.substring(0,classPath.indexOf("/WEB-INF"));
		}
		return rootPath;
	}
	public static void main(String[] args) {
		try {
			System.out.println(ClassLoader.getSystemResource("ssl/client.truststore").openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
