package com.doooly.common.async;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.doooly.common.token.TokenUtil;

@Component
public class AsyncDooolyService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StringRedisTemplate redisService;

	@Async
	public void setUserTokeExpire(String channel, String userId) {
		String tokenKey = String.format(channel + ":" + TokenUtil.TOKEN_KEY, userId);
		Long tokenExpireDay = redisService.getExpire(tokenKey, TimeUnit.DAYS);
		// 将永不失效及过期时间小于30天的token进行重置(120天)
		if (tokenExpireDay < 30) {
			redisService.expire(tokenKey, TokenUtil.TOKEN_EXPIRE, TimeUnit.DAYS);
			log.info("重新设置用户token有效期，tokenKey={}, expireDay={}", tokenKey, TokenUtil.TOKEN_EXPIRE);
		}
	}
}
