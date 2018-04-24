package com.doooly.common.token;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.doooly.common.context.SpringContextUtils;

/**
 * 
 * 用户令牌工具类
 * 
 * @Title: TokenUtil.java
 * @Package com.doooly.common.token
 * @author hutao
 * @date 2017年7月21日
 * @version V1.0
 */
public class TokenUtil {
	private static StringRedisTemplate redisService = (StringRedisTemplate) SpringContextUtils
			.getBeanByClass(StringRedisTemplate.class);
	// 会员token，唯一标识，放入缓存
	private static String TOKEN_KEY = "token:%s";
	// token时效性暂定30天内有效
	// private static Long TOKEN_EXPIRE = 30L;
	// 加盐
	private static String TOKEN_SALT = "279125393@qq.com";
	// private static Logger log = Logger.getLogger(TokenUtil.class);

	/**
	 * 
	 * 验证会员token有效性 token标识该用户的唯一标识，接入方请求时需要传入此token,
	 * 作为判断此用户是否已登录依据，若token不存在则不要重新登录后获取此token
	 * 
	 * @Title: TokenUtil.java
	 * @author hutao
	 * @date 2017年7月25日
	 * @version V1.0
	 */
	public static boolean validUserToken(String channel, String userId, String userToken) {
		// log.info("========validUserToken-channel:" + channel + ",userid:" +
		// userId + ",token:" + userToken);
		// 验证失败
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(userToken) || StringUtils.isBlank(channel))
			return false;
		// 使用token:userId 标识用户token对应的key
		String tokenValue = redisService.opsForValue().get(String.format(channel + ":" + TOKEN_KEY, userId));
		// 若Token为空，验证失败
		if (StringUtils.isBlank(tokenValue) || !userToken.equals(tokenValue)) {
			return false;
		} else {
			// todo 验证Token的有效性
			return true;
		}
	}

	/**
	 * 
	 * 获取用户Token，若失效直接生成新的token
	 * 
	 * @Title: TokenUtil.java
	 * @author hutao
	 * @date 2017年7月25日
	 * @version V1.0
	 */
	public static String getUserToken(String channel, String userId) {
		// 1.使用token:userId 标识用户token对应的key
		String userToken = redisService.opsForValue().get(String.format(channel + ":" + TOKEN_KEY, userId));
		if (StringUtils.isBlank(userToken)) {
			// 2.生成token,userId:salt=会员Id+盐
			userToken = DigestUtils.md5Hex(String.format("%s %s", userId, TOKEN_SALT));
			// 3.放入缓存userId:token
			redisService.opsForValue().set(String.format(channel + ":" + TOKEN_KEY, userId), userToken);
			// 4.放入缓存token:userId
			redisService.opsForValue().set(userToken, userId);
			// redisService.opsForValue().set(userToken, userId, TOKEN_EXPIRE,
			// TimeUnit.DAYS);
		}

		return userToken;
	}

	/**
	 * 
	 * 刷新Token
	 * 
	 * @Title: TokenUtil.java
	 * @author hutao
	 * @date 2017年7月25日
	 * @version V1.0
	 */
	public static String refreshUserToken(String channel, String userId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 时间戳
		String time_str = sdf.format(new Date());
		TOKEN_SALT += time_str;
		// 1.生成token,userId:salt=会员Id+盐
		String userToken = DigestUtils.md5Hex(String.format("%s %s", userId, TOKEN_SALT));
		// 2.放入redis缓存,并设置有效期
		redisService.opsForValue().set(String.format(channel + ":" + TOKEN_KEY, userId), userToken);
		// 3.放入缓存token:userId
		redisService.opsForValue().set(userToken, userId);

		return userToken;
	}
}
