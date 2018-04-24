/*package com.doooly.common.cxf;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;

import com.doooly.common.IPUtils;

public class CXFContextUtils {
	*//**
	 * 获取客户段IP地址
	 * @return
	 *//*
	public static String getClientIP(){
		try {
			
			WebServiceContext wscontext = new WebServiceContextImpl();
	    	MessageContext mc = wscontext.getMessageContext();

	    	HttpServletRequest request = (HttpServletRequest)(mc.get(MessageContext.SERVLET_REQUEST));

//	    	String remortAddress = request.getRemoteAddr();
//
//            return remortAddress;
	    	return IPUtils.getIpAddr(request);
        } catch (Exception e) {
            System.out.println("无法获取对方主机IP");
            e.printStackTrace();
            return null;
        }
	}
}
*/