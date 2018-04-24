package com.doooly.common.util;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.context.SpringContextHolder;

/**
 * 
 * @author WANG
 *
 */
public class WechatUtil {
	
	private static StringRedisTemplate redisTemplate = (StringRedisTemplate)SpringContextHolder.getBean("redisTemplate");
	
	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);
	
	private static String APPID = PropertiesHolder.getProperty("doooly.appid");
	private static String APPSECRET = PropertiesHolder.getProperty("doooly.appsecret");

	
	/** accessToken名称 */
	public static String ACCESS_TOKEN = "accessToken";
	/** jsApiTicket名称 */
	public static String JS_API_TICKET = "jsApiTicket";
	/** tokenExpiresTime名称 */
	public static String TOKEN_EXPIRES_TIME = "tokenExpiresTime";
	/** jsTicketExpiresTime名称 */
	public static String JS_API_TICKET_EXPIRES_TIME = "jsTicketExpiresTime";
	/** accessToken接口连接 */
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	/** jsApiTicket接口连接 */
	public static String JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	// 过期时间
	public static Long EXPIRE_IN = 5400L;

	/**
	 * 获得签名信息
	 * 
	 * @param url
	 * @param prefix
	 * @return
	 */
	public static Map<String, Object> getWechatConfig(String url){
		String accessToken = getCacheAccessToken();
		String jsApiTicket = getCacheTicket();
		if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(jsApiTicket)) {
			log.info("accessToken = {},jsApiTicket = {}", accessToken, jsApiTicket);
			return null;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			String nonce_str = create_nonce_str();
			String timestamp = create_timestamp();
			ret.put("url", url);
			ret.put("jsapi_ticket", jsApiTicket);
			ret.put("nonceStr", nonce_str);
			ret.put("timestamp", timestamp);
			ret.put("appid",APPID);
			ret.put("accessToken", accessToken);
			String signature = "";
			// 注意这里参数名必须全部小写，且必须有序
			String acess_url = "jsapi_ticket=" + jsApiTicket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp	 + "&url=" + url;
			ret.put("acess_url", acess_url);
			log.info("accessToken = {},jsApiTicket = {}", accessToken, jsApiTicket);
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(acess_url.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest()).toUpperCase();
			ret.put("signature", signature);
			log.info("signature = {}", signature);
		} catch (Exception e) {
			log.info("e = {}, ret = {}", e, ret);
		}
		return ret;
	}
	
	/**
	 * 从缓存获得token和ticket
	 * 
	 * @return
	 */
	public static String getTokenAndTicket(){
		String accessToken = redisTemplate.opsForValue().get("accessToken");
		if (StringUtils.isBlank(accessToken)) {
			return resetTokenAntTicket();
		} else {
			return accessToken;
		}
	}
	
	/**
	 * 从缓存获得ticket
	 * 
	 * @return
	 */
	public static String getCacheTicket(){
		String tokenAndTicket = getTokenAndTicket();
		return tokenAndTicket == null ? null :  JSONObject.parseObject(tokenAndTicket).getString("ticket");
	}
	
	/**
	 * 从缓存获得token
	 * 
	 * @return
	 */
	public static String getCacheAccessToken(){
		String tokenAndTicket = getTokenAndTicket();
		return tokenAndTicket == null ? null :JSONObject.parseObject(tokenAndTicket).getString("accessToken"); 
	}
	
	/**
	 * 请求最新的token 
	 * 
	 * @return
	 */
	public static String getAccessToken() {
		String accessToken = null;
		String requestUrl = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET",APPSECRET);
		try {
			String jsonString = HttpClientUtil.httpsGet(requestUrl);
			log.info("jsonString = {}" ,jsonString);
			if (null != jsonString) {
				accessToken = JSON.parseObject(jsonString).getString("access_token");
			}
		} catch (Exception e) {
			log.info("获取微信accessToken失败 = {},requestUrl = {}" , e.getMessage(), requestUrl);
		}
		return accessToken;
	}
	
	/**
	 * 请求最新的ticket 
	 * 
	 * @param accessToken
	 * @return
	 */
	public static String getTicket(String accessToken) {
		String jsTicket = "";
		String ticketUrl = JS_API_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
		try {
			String jsonString = HttpClientUtil.httpsGet(ticketUrl);
			log.info("jsonObject = {}",jsonString);
			if (null != jsonString) {
				jsTicket = JSON.parseObject(jsonString).getString("ticket");
			}
		} catch (Exception e) {
			log.error("获取微信jsTicket失败 = {},ticketUrl ={}", e.getMessage(), ticketUrl);
		}
		return jsTicket;
	}
	
	/**
	 * 获得token和ticket,缓存到redis
	 * 
	 * @return JSON
	 */
	private static String resetTokenAntTicket(){
		JSONObject wehcatJson = new JSONObject();
		String token = getAccessToken();
		if(token == null){
			return null;
		}
		String ticket = getTicket(token);
		if(ticket == null){
			return null;
		}
		wehcatJson.put("accessToken", token);
		wehcatJson.put("ticket",ticket);
		log.info("resetTokenAntTicket() token = {},ticket = {}", token, ticket);
		redisTemplate.opsForValue().set("accessToken", wehcatJson.toJSONString(), EXPIRE_IN,TimeUnit.SECONDS);
		return wehcatJson.toJSONString();
	}
	
	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	private static String create_nonce_str() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		if (uuid.length() >= 32) {
			uuid = uuid.substring(0, 32);
		}
		return uuid;
	}
	
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
}
