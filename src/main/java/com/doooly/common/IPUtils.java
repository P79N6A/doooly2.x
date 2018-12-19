package com.doooly.common;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {
	/**
	 * 获取客户IP时候,request.getRemoteAddr()取得客户端的IP地址,
	 * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了。
		经过代理以后，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的IP，
		服务器端应用也无法直接通过转发请求的地址返回给客户端。但是在转发请求的HTTP头信息中，
		增加了X－FORWARDED－FOR信息用以跟踪原有的客户端IP地址和原来客户端请求的服务器地址。
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(null != ip && ip.contains(",")){
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
