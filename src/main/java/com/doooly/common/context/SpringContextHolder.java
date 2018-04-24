package com.doooly.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * spring上下文的持有者: ApplicationContextAware 可感知ApplicationContex,
 * 只要实现了该接口的类，交给Spring管理后，Spring就能感知到, 感知后就会调用setApplicationContext注入当前容器的上下文.
 * 
 * @author lne
 */
public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext ctx = null;

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		SpringContextHolder.ctx = ctx;
	}

	public static Object getBean(String name) {
		return ctx.getBean(name);
	}

	public static Object getBean(Class clazz) {
		return ctx.getBean(clazz);
	}

	public static <T> Map<String, T> getBeansOfType(Class clazz) {
		return ctx.getBeansOfType(clazz);
	}
}