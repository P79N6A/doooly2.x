package com.doooly.common.constants;

import java.util.Properties;

/**
 * 属性文件配置
 * 
 * @author WANG
 * 
 */
public class PropertiesHolder {

	private static Properties properties;

	public static void setProperties(Properties properties) {
		PropertiesHolder.properties = properties;
	}

	public static String getProperty(String key) {
		return PropertiesHolder.properties.getProperty(key);
	}
	
	public static Properties getProperties() {
		return properties;
	}

}