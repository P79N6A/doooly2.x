package com.doooly.common.util;

import java.util.UUID;

/**
 * 
 * @author Albert
 *
 */
public class AppTokenUtils {
	
	public static String getToken(){
		UUID uuid = UUID.randomUUID();
		String token = MD5Utils.encode(uuid.toString());
		return token;
	}
	
}
