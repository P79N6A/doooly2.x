package com.doooly.common.util;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

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

	private static StringRedisTemplate redisTemplate = (StringRedisTemplate) SpringContextHolder
			.getBean("redisTemplate");

	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);

	/** 兜礼公众号配置 */
	private static String APPID = PropertiesHolder.getProperty("doooly.appid");
	private static String APPSECRET = PropertiesHolder.getProperty("doooly.appsecret");
	public static String API_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info";
	/** 获取公众号配置 */
	private static String getPropertiesValue(String channel, String key) {
		return PropertiesHolder.getProperty(channel + "." + key);
	}

	/** accessToken名称(兜礼公众号缓存key) */
	public static String ACCESS_TOKEN = "accessToken";
	/** accessToken名称(武钢公众号缓存key) */
	public static String ACCESS_TOKEN_WUGANG = "accessTokenWugang";
	/** 缓存中ticket-key */
	public static String TICKET = "ticket";
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
	public static Map<String, Object> getWechatConfig(String channel, String url) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			JSONObject redisAccessTokenTicketJson = getAccessTokenTicketRedisByChannel(channel);
			String accessToken = redisAccessTokenTicketJson.getString(ACCESS_TOKEN);
			String jsApiTicket = redisAccessTokenTicketJson.getString(TICKET);

			if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(jsApiTicket)) {
				log.info("accessToken = {},jsApiTicket = {}", accessToken, jsApiTicket);
				return null;
			}
			if("undefined".equals(channel) || StringUtils.isEmpty(channel) || "doooly".equals(channel)) {
				channel = "doooly";
			}
			String nonce_str = create_nonce_str();
			String timestamp = create_timestamp();
			ret.put("url", url);
			ret.put("jsapi_ticket", jsApiTicket);
			ret.put("nonceStr", nonce_str);
			ret.put("timestamp", timestamp);
			ret.put("appid", getPropertiesValue(channel, "appid"));
			ret.put("accessToken", accessToken);
			String signature = "";
			// 注意这里参数名必须全部小写，且必须有序
			String acess_url = "jsapi_ticket=" + jsApiTicket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
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
	public static String getTokenAndTicket() {
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
	public static String getCacheTicket() {
		String tokenAndTicket = getTokenAndTicket();
		return tokenAndTicket == null ? null : JSONObject.parseObject(tokenAndTicket).getString("ticket");
	}

	/**
	 * 从缓存获得token
	 * 
	 * @return
	 */
	public static String getCacheAccessToken() {
		String tokenAndTicket = getTokenAndTicket();
		return tokenAndTicket == null ? null : JSONObject.parseObject(tokenAndTicket).getString("accessToken");
	}

	/**
	 * 请求最新的token
	 * 
	 * @return
	 */
	public static String getAccessToken() {
		String accessToken = null;
		String requestUrl = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		try {
			String jsonString = HttpClientUtil.httpsGet(requestUrl);
			log.info("jsonString = {}", jsonString);
			if (null != jsonString) {
				accessToken = JSON.parseObject(jsonString).getString("access_token");
			}
		} catch (Exception e) {
			log.info("获取微信accessToken失败 = {},requestUrl = {}", e.getMessage(), requestUrl);
		}
		return accessToken;
	}

	/**
	 * 接口请求最新的token,并放入缓存
	 * 
	 * @return json key(accessToken,ticket)
	 */
	public static JSONObject getNewAccessTokenTicketByChannel(String channel) {
		log.info("====【getNewAccessTokenTicketByChannel】渠道：【" + channel + "】接口获取token,ticket");
		// 接口请求地址
		String requestUrl = "";
		// token/ticket
		String accessTokenValue = "";
		String jsApiTicketValue = "";
		// jsApiTicket缓存key(【兜礼】-jsApiTicket/【武钢】-wugangjsApiTicket)
		String jsApiTicketKey = "ticket";
		// accessToken缓存key(【兜礼】-accessToken/【武钢】-wugangaccessToken)
		String redisAccessTokenKey = ACCESS_TOKEN;
		// 缓存json对应ticket-key
		String accessTokenKey = "access_token";
		// 返回accessToken,ticket json对象
		JSONObject redisAccessTokenTicketJson = new JSONObject();

		// 渠道区分,兜礼公众号使用默认key,不拼接渠道
		if ("undefined".equals(channel) || StringUtils.isEmpty(channel) || "doooly".equals(channel)) {
			channel = "doooly";
		} else {
			redisAccessTokenKey = channel + redisAccessTokenKey;
		}
		requestUrl = ACCESS_TOKEN_URL.replace("APPID", getPropertiesValue(channel, "appid")).replace("APPSECRET",
				getPropertiesValue(channel, "appsecret"));

		try {
			log.info("====获取AccessToken的路径为:" + requestUrl);
			String jsonString = HttpClientUtil.httpsGet(requestUrl);
			log.info("====jsonString = {}", jsonString);
			if (StringUtils.isNotBlank(jsonString)) {
				accessTokenValue = JSON.parseObject(jsonString).getString(accessTokenKey);
				// accessToken为空则再次获取
				if (StringUtils.isBlank(accessTokenValue)) {
					log.info("====获取的AccessToken为空,重新获取一次");
					String jsonString1 = HttpClientUtil.httpsGet(requestUrl);
					accessTokenValue = JSON.parseObject(jsonString1).getString(accessTokenKey);
					log.info("====再次获取的accessToken为:" + accessTokenValue);
				}
				String jsApiTicketUrl = JS_API_TICKET_URL.replaceAll("ACCESS_TOKEN", accessTokenValue);
				log.info("====获取jsApiTicket的路径为:" + jsApiTicketUrl);
				String jsApiTicketStr = HttpClientUtil.httpsGet(jsApiTicketUrl);// "GET"
				log.info("====获取jsApiTicket的返回值为:" + jsApiTicketStr);
				if (StringUtils.isNotBlank(jsApiTicketStr)) {
					jsApiTicketValue = JSON.parseObject(jsApiTicketStr).getString(jsApiTicketKey);
					if (StringUtils.isBlank(jsApiTicketValue)) {
						log.info("====以accessToken为:" + accessTokenValue + "生存获取的jsApiTicket为空,重新获取一次");
						String jsApiTicketStr1 = HttpClientUtil.httpsGet(jsApiTicketUrl);// "GET"
						jsApiTicketValue = JSON.parseObject(jsApiTicketStr1).getString("ticket");
						log.info("====再次获取的jsApiTicket为:" + jsApiTicketValue);
					}
					// 缓存token ticket json格式数据
					redisAccessTokenTicketJson.put(ACCESS_TOKEN, accessTokenValue);
					// ticket
					redisAccessTokenTicketJson.put(jsApiTicketKey, jsApiTicketValue);
					redisTemplate.opsForValue().set(redisAccessTokenKey, redisAccessTokenTicketJson.toJSONString(),
							EXPIRE_IN, TimeUnit.SECONDS);
				}
			}
		} catch (Exception e) {
			log.info(
					"====【getNewAccessTokenTicketByChannel】接口获取渠道【" + channel + "】微信jsApiTicket失败 = {},requestUrl = {}",
					e.getMessage(), requestUrl);
		}
		log.info("====【getNewAccessTokenTicketByChannel】接口获取渠道【" + channel + "】微信accessToken/ticket："
				+ redisAccessTokenTicketJson.toJSONString());

		return redisAccessTokenTicketJson;
	}

	/**
	 * 缓存获取jsApiTicket
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return json key(accessToken,ticket)
	 */
	public static JSONObject getAccessTokenTicketRedisByChannel(String channel) {
		log.info("====【getAccessTokenTicketRedisByChannel】-获取accessTokenTicket渠道：" + channel);
		// accessToken缓存key(【兜礼】-accessToken/【武钢】-wugangaccessToken)
		String redisAccessTokenKey = ACCESS_TOKEN;
		// 返回accessToken,ticket json对象
		JSONObject redisAccessTokenTicketJson = new JSONObject();

		// 渠道区分,兜礼公众号使用默认key,不拼接渠道
		if ("undefined".equals(channel) || StringUtils.isEmpty(channel) || "doooly".equals(channel)) {
			channel = "doooly";
		} else {
			redisAccessTokenKey = channel + redisAccessTokenKey;
		}

		String redisAccessTokenTicketStr = redisTemplate.opsForValue().get(redisAccessTokenKey);
		log.info("====【getAccessTokenTicketRedisByChannel】缓存中渠道【" + channel + "】accessToken为:"
				+ redisAccessTokenTicketStr);
		if (StringUtils.isNotBlank(redisAccessTokenTicketStr)) {
			redisAccessTokenTicketJson = JSONObject.parseObject(redisAccessTokenTicketStr);
			return redisAccessTokenTicketJson;
		}

		// 接口获取渠道token/ticket
		redisAccessTokenTicketJson = getNewAccessTokenTicketByChannel(channel);

		return redisAccessTokenTicketJson;
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
			log.info("jsonObject = {}", jsonString);
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
	private static String resetTokenAntTicket() {
		JSONObject wehcatJson = new JSONObject();
		String token = getAccessToken();
		if (token == null) {
			return null;
		}
		String ticket = getTicket(token);
		if (ticket == null) {
			return null;
		}
		wehcatJson.put("accessToken", token);
		wehcatJson.put("ticket", ticket);
		log.info("resetTokenAntTicket() token = {},ticket = {}", token, ticket);
		redisTemplate.opsForValue().set("accessToken", wehcatJson.toJSONString(), EXPIRE_IN, TimeUnit.SECONDS);
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
	/**
	 * 获取指定会员微信账号头像与昵称
	 * @param request
	 * @param wechatOpenid 微信openId
	 * @return
	 */
	public static Map<String, String> getWechatUserByOpenId(String wechatOpenid,String channel){
		Map<String, String> map = new HashMap<>();
		try {
			String URL = API_USER_INFO + "?access_token=" + getNewAccessTokenTicketByChannel(channel).getString(ACCESS_TOKEN) + "&openid=" + wechatOpenid;
			String response = HttpClientUtil.httpsGet(URL,  null);
			JSONObject jsonObject = JSON.parseObject(response);
			String headimgurl = jsonObject.get("headimgurl") == null ? null : jsonObject.getString("headimgurl");
			String nickname = jsonObject.get("nickname") == null ? null : jsonObject.getString("nickname");
			map.put("nickname", nickname);
			map.put("headimgurl", headimgurl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
}
}
