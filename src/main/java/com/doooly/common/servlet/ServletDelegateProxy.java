package com.doooly.common.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Servlet代理类(作用:可以让Servlet中使用spring依赖注入如@autowired)
 * @author 赵清江
 * @date 2016年7月12日
 * @version 1.0
 */
public class ServletDelegateProxy extends GenericServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1132705125232180547L;

	private String targetBeanName;
	
	private Servlet proxy;
	
	@Override
	public void init() throws ServletException {
		this.targetBeanName = getServletName();
		getServletBean();
		proxy.init(getServletConfig());
	}
	
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		proxy.service(req, res);
	}

	private void getServletBean(){
		
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		this.proxy = (Servlet) webApplicationContext.getBean(this.targetBeanName);
	}
	
}
