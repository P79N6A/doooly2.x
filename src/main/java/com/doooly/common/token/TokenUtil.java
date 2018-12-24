package com.doooly.common.token;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.doooly.common.async.AsyncDooolyService;
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
	private static Logger log = LoggerFactory.getLogger(TokenUtil.class);
	// 会员token，唯一标识，放入缓存
	public static String TOKEN_KEY = "token:%s";
	// token时效性暂定120天内有效
	public static Long TOKEN_EXPIRE = 120L;
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
		StringRedisTemplate redisService = (StringRedisTemplate) SpringContextUtils.getBeanById("redisTemplate");
		AsyncDooolyService asyncDooolyService = (AsyncDooolyService) SpringContextUtils.getBeanByClass(AsyncDooolyService.class);
		String tokenKey = String.format(channel + ":" + TOKEN_KEY, userId);
		String tokenValue = redisService.opsForValue().get(tokenKey);
		// 若Token为空，验证失败
		if (StringUtils.isBlank(tokenValue) || !userToken.equals(tokenValue)) {
			return false;
		} else {
			// 验证token成功
			asyncDooolyService.setUserTokeExpire(channel, userId);
			log.info("验证token成功,userId={}",userId);
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
		StringRedisTemplate redisService = (StringRedisTemplate) SpringContextUtils.getBeanById("redisTemplate");
		// 1.使用token:userId 标识用户token对应的key
		String userToken = redisService.opsForValue().get(String.format(channel + ":" + TOKEN_KEY, userId));
		if (StringUtils.isBlank(userToken)) {
			// 2.生成token,userId:salt=会员Id+盐
			userToken = DigestUtils.md5Hex(String.format("%s %s", userId, TOKEN_SALT));
			// 3.放入缓存channel:userId:token
			redisService.opsForValue().set(String.format(channel + ":" + TOKEN_KEY, userId), userToken, TOKEN_EXPIRE,
					TimeUnit.DAYS);
			// 4.放入缓存token:userId
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
		StringRedisTemplate redisService = (StringRedisTemplate) SpringContextUtils.getBeanById("redisTemplate");
		// 时间戳
		String time_str = sdf.format(new Date());
		TOKEN_SALT += time_str;
		// 1.生成token,userId:salt=会员Id+盐
		String userToken = DigestUtils.md5Hex(String.format("%s %s", userId, TOKEN_SALT));
		// 2.放入redis缓存,并设置有效期,key=channel:token:userId
		redisService.opsForValue().set(String.format(channel + ":" + TOKEN_KEY, userId), userToken, TOKEN_EXPIRE,
				TimeUnit.DAYS);
		// 3.放入缓存token:userId
//		redisService.opsForValue().set(userToken, userId);

		return userToken;
	}

	/**
	 * 注销用户时清除掉用户的token令牌
	 * 
	 * @author hutao
	 * @date 创建时间：2018年9月25日 下午5:24:41
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	public static void cancelUserToken(String userId) {
		long start = System.currentTimeMillis();
		StringRedisTemplate redisService = (StringRedisTemplate) SpringContextUtils.getBeanById("redisTemplate");
		// 1.初始化需要删除的token key值
		Set<String> tokenKeys = new HashSet<>();
		tokenKeys.add(String.format("token:%s", userId));
		tokenKeys.add(String.format("h5:token:%s", userId));
		tokenKeys.add(String.format("app:token:%s", userId));
		tokenKeys.add(String.format("wechat:token:%s", userId));
		tokenKeys.add(String.format("wiscoapp:token:%s", userId));
		tokenKeys.add(String.format("wiscowechat:token:%s", userId));

		// 2.查询key对应的value集合
		List<String> valueList = redisService.opsForValue().multiGet(tokenKeys);
		// 3.删除该用户的所有token key
		for (String tt : tokenKeys) {
			String vv = redisService.opsForValue().get(tt);
			if (vv != null) {
				redisService.delete(vv);
			}
		}
		redisService.delete(tokenKeys);

		// redisService.delete(valueList);

		log.info("注销用户时清空该用户token，cost={}ms, token key list={},{}", System.currentTimeMillis() - start, tokenKeys,
				valueList);
	}
}
