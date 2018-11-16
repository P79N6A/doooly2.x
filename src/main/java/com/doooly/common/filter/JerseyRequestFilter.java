package com.doooly.common.filter;

import com.doooly.common.constants.Constants;
import com.doooly.common.context.SpringContextHolder;
import com.doooly.common.exception.GlobalException;
import com.doooly.common.token.TokenUtil;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.ws.rs.container.PreMatching;

/**
 * 
 * Jesery请求拦截器，验证接口访问accessToken是否有效 若无效则会重新生成accessToken
 * 
 * @Title: JeseryRequestFilter.java
 * @Package com.doooly.common.filter
 * @author hutao
 * @date 2017年7月20日
 * @version V1.0
 */
@PreMatching
// @Provider
public class JerseyRequestFilter implements ContainerRequestFilter {
	private Logger log = Logger.getLogger(this.getClass());

	/** 缓存对象 */
	private static StringRedisTemplate redisTemplate = (StringRedisTemplate) SpringContextHolder
			.getBean("redisTemplate");

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		// 1. todo...过滤非验证url
		String reqUrl = request.getRequestUri().toString().toLowerCase();
		log.info("====【JerseyRequestFilter】-reqUrl：" + request.getRequestUri() + ",==headers数据:"
				+ request.getRequestHeaders() + ",==cookie数据：" + request.getCookieNameValueMap());

		// 对于登录请求不拦截、设置专属优惠券id的请求不拦截
		if (reqUrl.contains("/login") || reqUrl.contains("/exclusiveCoupon/setActivityId")
				|| reqUrl.contains("/token/validateUserToken") || reqUrl.contains("/share/getFamilyInviteInfoRestFul")) {
			return request;
		}

		/*
		 * if (reqUrl.contains("/exclusiveCoupon/setActivityId")){ return
		 * request; }
		 */
		// 2. cookie设置token或url含有/app/进行token验证
		// MultivaluedMap<String, String> tokenCookies =
		// request.getCookieNameValueMap();
		// tokenCookies != null &&
		// StringUtils.isNotBlank(request.getRequestHeaders().getFirst("token"))
		// String userToken = tokenCookies.getFirst("token");
		// reqUrl.contains("/app/")

		if (request.getRequestHeaders() != null && request.getRequestHeaders().containsKey(Constants.TOKEN_NAME)
				&& request.getRequestHeaders().containsKey(Constants.CHANNEL)) {
			String userToken = request.getRequestHeader(Constants.TOKEN_NAME).get(0);
			// String uid = request.getRequestHeader("uid").get(0);
			String userId = redisTemplate.opsForValue().get(userToken);
			String channel = request.getRequestHeader(Constants.CHANNEL).get(0);
			log.info("====【JerseyRequestFilter】-request headers="+request.getRequestHeaders());

			// 3.验证token有效性
			boolean validResult = TokenUtil.validUserToken(channel, userId, userToken);
			// 验证失败返回token信息
			if (!validResult) {
				log.info("====【JerseyRequestFilter】-validResult：" + validResult + ",token验证失败或token不存在");
				throw new GlobalException(GlobalException.TOKEN_CODE, "token验证失败或token不存在");
			} else {
				log.info("====【JerseyRequestFilter】-validResult：" + validResult + ",token验证成功");
			}
		} else {
			log.info("====【JerseyRequestFilter】-无需过滤器拦截========");
		}
		return request;
	}

}
