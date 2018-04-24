package com.doooly.common.context;

import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 获取ApplicationContext和Object的工具类
 *
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SpringContextUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		applicationContext = arg0;
	}

	/**
	 * 获取applicationContext对象
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据bean的id来查找对象
	 * 
	 * @param id
	 * @return
	 */
	public static Object getBeanById(String id) {
		return applicationContext.getBean(id);
	}

	/**
	 * 根据bean的class来查找对象
	 * 
	 * @param c
	 * @return
	 */
	public static Object getBeanByClass(Class c) {
		// return applicationContext.getBean(c);
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		Object obj = context.getBean(c);
		return obj;
	}

	/**
	 * 根据bean的class来查找所有的对象(包括子类)
	 * 
	 * @param c
	 * @return
	 */
	public static Map getBeansByClass(Class c) {
		return applicationContext.getBeansOfType(c);
	}

	public static Object getBeanByClass(Object obj, Class c) {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		if (context == null)
			return null;

		if (obj == null) {
			obj = context.getBean(c);
		}
		return obj;
	}
}