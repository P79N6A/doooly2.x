package com.doooly.common.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.annotation.UserToken;
import com.doooly.common.constants.Constants;
import com.doooly.common.exception.GlobalException;

@Aspect
@Component
public class UserTokenCheck {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Before("within(com.doooly.publish.rest..*) && @annotation(userToken)")
	public void doBefore(JoinPoint joinPoint, UserToken userToken) {
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			//HttpServletRequest代理类
			if(arg.getClass().getName().contains("com.sun.proxy.$")){
				Method method = null;
				try {
					//获取getHeader方法
					method = arg.getClass().getMethod("getHeader", String.class);
					if (method != null) {
						// 获取request
						HttpServletRequest request = (HttpServletRequest) arg;
						String token = request.getHeader(Constants.TOKEN_NAME);
						if (StringUtils.isBlank(token)) {
							log.warn("用户token前置拦截，用户token为空， req json={}", joinPoint.getArgs());
							throw new GlobalException(GlobalException.TOKEN_CODE, GlobalException.TOKEN_DESC);
						}
						break;
					}
				} catch (NoSuchMethodException e) {
					log.error("用户token前置拦截，获取getHeader方法错误，error=",e);
				} catch (SecurityException e) {
					log.error("用户token前置拦截，获取getHeader方法错误，error=",e);
				}
			}
		}
	}
}
