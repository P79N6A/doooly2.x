package com.doooly.common.util;

import com.doooly.common.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;

/**
 * (注意: 分布式生成ID全局无序, 非线程安全)
 * @author john
 *
 */
public class IdGeneratorUtil {

	private static Logger logger = LoggerFactory.getLogger(IdGeneratorUtil.class);

	private static final String suffixOfOrderNo = "N";

	private static final String REDIS_ID_KEY = "ID:ORDER_NUMBER";

	protected static StringRedisTemplate redisTemplate = (StringRedisTemplate) SpringContextHolder.getBean(StringRedisTemplate.class);

	static{
		redisTemplate.opsForValue().setIfAbsent(REDIS_ID_KEY,"10000");
	}

	/**
	 * 时间戳 + 四位随机数 + 订单来源
	 * 
	 * 获取订单号
	 * 
	 * @return
	 */
	public static long getOrderId() {
		// 时间戳
		long l = System.currentTimeMillis();
		// 产生随机数
		int r = (int) ((Math.random() * 9 + 1) * 1000);
		return Long.valueOf(l + r);
	}
	
	/**
	 * 大订单号 
	 * 
	 * 时间戳 + 四位随机数 + 订单来源 + N
	 * 
	 * 获取订单号
	 * 
	 * @return
	 */
	public static String getOrderNumber(int source) {
		
		StringBuffer sbf = new StringBuffer();
		// 时间戳
		long l = System.currentTimeMillis();
		//自增长
		long r = 0;
		try {
			r = redisTemplate.opsForValue().increment(REDIS_ID_KEY,1);
		}catch (Exception e){
			logger.error("getOrderNumber e = {}", e);
			r = (long) ((Math.random() * 9 + 1) * 100000);
		}
		// 订单来源(默认兜礼)
		int s = source > 0 ? source : 3;
		return sbf.append(l).append(r).append(s).append(suffixOfOrderNo).toString();
	}

	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String generateSctcdOrderNumber(long userId){
		StringBuilder sb = new StringBuilder("U");
		String hexUserId = Long.toHexString(userId).toUpperCase();
		sb.append(hexUserId);
		sb.append(System.currentTimeMillis());
		String orderNumber = sb.toString();
		if (orderNumber.length() > 20) {
			orderNumber = orderNumber.substring(0, 20);
		}
		return orderNumber;
	}
	
//	public static void main(String[] args) {
//		System.out.println(getOrderNumber(0));
//		for (int i = 0; i < 20; i++) {
//			System.out.println((int) ((Math.random() * 9 + 1) * 1000));
//		}
//	}

}
