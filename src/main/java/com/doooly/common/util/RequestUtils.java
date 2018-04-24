package com.doooly.common.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
	public static String getRequstURLRootPath(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		return url.substring(0, url.indexOf("/jersey"));
	}
}
