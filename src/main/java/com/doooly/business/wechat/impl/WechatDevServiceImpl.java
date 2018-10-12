package com.doooly.business.wechat.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.business.common.constant.ConnectorConstants.WechatConstants;
import com.doooly.business.common.service.ActivityCodeImageServiceI;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.report.WechatEventPushService;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.wechat.WechatDevCallbackServiceI;
import com.doooly.common.constants.ActivityConstants;
import com.doooly.common.util.WechatUtil;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.report.WechatEventPush;
import com.wechat.ThirdPartyWechatUtil;
import com.wechat.vo.UserInfo;

/**
 * 
 * @ClassName: WechatDevServiceImpl
 * @Description: 处理微信回调业务
 * @author hutao
 * @date 2018年4月12日
 *
 */
@Service
public class WechatDevServiceImpl implements WechatDevCallbackServiceI {
	private static Logger log = LoggerFactory.getLogger(WechatDevServiceImpl.class);
	// 开发者模式-自行定义Token
	public static final String token = "doooly";
	public static final String WECHAT_MSG = "WECHAT_MSG";
	// 活动分隔符
	private static final String ACTIVITY_SPLIT = "~";
	// 开发者模式-活动标记名
	public static final String RECHARGE_ACTIVITY = "recharge_activity";
	public static final String WUGANG_SCAN_ACTIVITY = "scan_activity";
	public static final String BRING_COLLNESS_ACTIVITY = "bring_coolness_activity";
	public static final String MU_RECHARGE_ACTIVITY = "mu_coupon_activity";

	/** 微信推送事件Service */
	@Autowired
	private WechatEventPushService wechatEventPushService;

	/** 微信扫码推送推文/图片 */
	@Autowired
	private ActivityCodeImageServiceI activityCodeImageServiceI;
	//
	// @Autowired
	// private AdConfigDictDao configDictDao;

	@Autowired
	private ConfigDictServiceI configService;

	@Autowired
	private StringRedisTemplate stringRedis;

	/**
	 * 方法名：checkSignature</br>
	 * 详述：验证签名</br>
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public boolean checkSignature(String signature, String timestamp, String nonce) {
		// 1.将token、timestamp、nonce三个参数进行字典序排序
		String[] arr = new String[] { token, timestamp, nonce };
		Arrays.sort(arr);

		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		String tmpStr = DigestUtils.sha1Hex(content.toString());
		log.info("微信开发者模式签名验证：" + signature.toUpperCase() + "==" + tmpStr);
		// 3.将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature) : false;
	}

	/**
	 * 微信回调处理（如扫描带参二维码、关注、取消关注等事件）
	 */
	@Override
	public List<String> dealCallback(String reqStr, String channel) {
		try {
			JSONObject json = XML.toJSONObject(reqStr).getJSONObject("xml");
			log.info("微信回调事件-请求参数xml转json：" + json.toString());
			// 消息类型，event
			String msgType = json.getString("MsgType");
			// 开发者微信号
			// String toUserName = json.getString("ToUserName");
			// 发送方帐号（一个OpenID）
			String fromUserName = json.getString("FromUserName");
			// 二维码参数
			String eventKey = "";
			// 放入渠道
			json.put("channel", channel);
			// 处理消息集合
			List<String> messageList = new ArrayList<String>();
			// 回复文本消息
			if (WechatConstants.MESSAGE_TYPE_TEXT.equals(msgType)) {
				String conent = json.getString("Content");
				// 添加回复文本消息内容
				messageList.add(createMessageReqJson(channel, fromUserName, conent));
				// 推送事件
			} else {
				String event = json.getString("Event");
				switch (event) {
				// 用户已关注时的事件推送
				case WechatConstants.EVENT_TYPE_SCAN:
					// 存储微信推送信息
					Long count = this.saveWechatEventPush(json);
					if (count == 0) {
						return null;
					}
					eventKey = json.getString("EventKey").replace("qrscene_", "");
					// 话费充值活动
					if (eventKey.contains(RECHARGE_ACTIVITY)) {
						handleIsPush(channel, fromUserName, eventKey);
					} else if (eventKey.contains(WUGANG_SCAN_ACTIVITY)) {
						handleIsPushNews(channel, fromUserName, WUGANG_SCAN_ACTIVITY);
					} else if (eventKey.contains(BRING_COLLNESS_ACTIVITY)) {
						handleIsPushNews(channel, fromUserName, BRING_COLLNESS_ACTIVITY);
					} else if (eventKey.contains(MU_RECHARGE_ACTIVITY)) {
						handleIsPushNews(channel, fromUserName, MU_RECHARGE_ACTIVITY);
						// 兜礼裂变v1活动
					} else if (eventKey.contains(ActivityConstants.FISSION_V1_ACTIVITY)) {
						// 通过活动标签识别
						handleIsPush(channel, fromUserName, eventKey);
					} else if (eventKey.contains(ACTIVITY_SPLIT)) {
						// 统一组装微信带参二维码客服回复消息列表
						messageList.addAll(createQrcodeWithParamMsgList(eventKey));
					}

					break;
				// 用户关注时的事件推送
				case WechatConstants.EVENT_TYPE_SUBSCRIBE:
					// 存储微信推送信息
					count = this.saveWechatEventPush(json);
					// 若为0则插入失败，不再执行后续业务
					if (count == 0) {
						String msg = createMessageReqJson(channel, fromUserName, WechatConstants.EVENT_TYPE_SUBSCRIBE);
						messageList.add(msg);
						return messageList;
					}
					eventKey = json.getString("EventKey").replace("qrscene_", "");
					// 话费充值活动
					if (eventKey.contains(RECHARGE_ACTIVITY)) {
						// 二维码参数集合,格式：[渠道,活动标记,分享人openId]
						// String[] paramArr = eventKey.replace("qrscene_",
						// "").split("~");
						handleIsPush(channel, fromUserName, eventKey);
					}
					// 兜礼裂变v1活动
					else if (eventKey.contains(ActivityConstants.FISSION_V1_ACTIVITY)) {
						fissionActivityCallback(channel, fromUserName, eventKey);
					} else if (eventKey.contains(WUGANG_SCAN_ACTIVITY)) {
						handleIsPushNews(channel, fromUserName, WUGANG_SCAN_ACTIVITY);
					} else if (eventKey.contains(BRING_COLLNESS_ACTIVITY)) {
						handleIsPushNews(channel, fromUserName, BRING_COLLNESS_ACTIVITY);
					} else if (eventKey.contains(MU_RECHARGE_ACTIVITY)) {
						handleIsPushNews(channel, fromUserName, MU_RECHARGE_ACTIVITY);
					} else if (eventKey.contains(ACTIVITY_SPLIT)) {
						// 统一组装微信带参二维码客服回复消息列表
						messageList.addAll(createQrcodeWithParamMsgList(eventKey));
					} else {
						// 微信公众号关注回复信息
						List<String> msgJsonList = createMessageReqJsonList(channel, fromUserName,
								WechatConstants.EVENT_TYPE_SUBSCRIBE);
						messageList.addAll(msgJsonList);
						log.info("====【dealCallback】关注微信公众号后回复客服消息" + msgJsonList);
					}

					break;
				// 用户点击事件推送
				case WechatConstants.EVENT_TYPE_CLICK:
					eventKey = json.getString("EventKey");
					String textMsg = createMessageReqJson(channel, fromUserName, eventKey);
					messageList.add(textMsg);
					log.info("====【dealCallback】用户点击菜单事件回复文本消息" + textMsg);
					// 存储微信推送信息
					this.saveWechatEventPush(json);
					break;
				default:
					break;
				}

			}

			return messageList;
		} catch (Exception e) {
			log.error("====【dealCallback】处理微信回调事件出错，error=" + e.getMessage(), e);
		}
		return null;
	}

	private void handleIsPush(String channel, String fromUserName, String eventKey) throws Exception {
		// 二维码参数集合,格式：[渠道,活动标记,分享人openId]
		String[] paramArr = eventKey.replace("qrscene_", "").split("~");
		// 活动标记
		String activityMark = paramArr[1];
		// 获取分享人openId
		String shareOpendId = null;
		if (paramArr.length == 3) {
			shareOpendId = paramArr[2];
		}
		log.info(
				"====【dealCallback】活动标识：" + activityMark + ",分享人openId：" + shareOpendId + ",扫描人openId:" + fromUserName);

		if (StringUtils.isBlank(shareOpendId) || !shareOpendId.equals(fromUserName)) {
			MessageDataBean messageDataBean = activityCodeImageServiceI.pushImageAndText(fromUserName, shareOpendId,
					channel, activityMark);
			log.info("====【dealCallback】推送结果：" + messageDataBean.toJsonString());
		} else {
			log.info("====【dealCallback】扫自己二维码,不推送信息====");
		}
	}

	private void handleIsPushNews(String channel, String fromUserName, String activityKey) throws Exception {
		MessageDataBean messageDataBean = activityCodeImageServiceI.pushNews(fromUserName, channel, activityKey);
		log.info("====【dealCallback】推送结果：" + messageDataBean.toJsonString());
	}

	/**
	 * 微信推送事件信息存储
	 * 
	 * @param json
	 */
	private Long saveWechatEventPush(JSONObject jsonObject) throws Exception {
		log.info("====【saveWechatEventPush】执行微信推送信息存储");
		WechatEventPush wechatEventPush = new WechatEventPush();

		wechatEventPush.setToUserName(jsonObject.getString("ToUserName"));
		wechatEventPush.setFromUserName(jsonObject.getString("FromUserName"));
		wechatEventPush.setMsgType(jsonObject.getString("MsgType"));
		wechatEventPush.setEvent(jsonObject.getString("Event"));
		if (jsonObject.has("EventKey")) {
			wechatEventPush.setEventKey(jsonObject.getString("EventKey").replace("qrscene_", ""));
		}
		wechatEventPush.setChannel(jsonObject.getString("channel"));
		Long CreateTime = jsonObject.getLong("CreateTime");
		wechatEventPush.setCreateTime(new Date(CreateTime * 1000));
		Long count = wechatEventPushService.insert(wechatEventPush);
		log.info("====【saveWechatEventPush】生成的记录行数:" + count);
		return count;

	}

	/**
	 * 创建客服消息（文本/图片/图文等消息）
	 * 
	 * @author hutao
	 * @date 创建时间：2018年9月27日 上午9:32:25
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return 客服消息（Json格式）
	 */
	private String createMessageReqJson(String channel, String toUserName, String switchType) {
		String dictKey = channel + "_" + switchType;
		String msgJson = configService.getValueByTypeAndKey(WECHAT_MSG, dictKey.toUpperCase());
		msgJson = msgJson.replace("OPENID", toUserName);

		return msgJson;
	}

	/**
	 * 创建客服消息（文本/图片/图文等消息）
	 * 
	 * @author hutao
	 * @date 创建时间：2018年9月27日 上午9:32:25
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return 客服消息（Json格式）
	 */
	private List<String> createMessageReqJsonList(String channel, String toUserName, String switchType) {
		String dictKey = channel + "_" + switchType;
		List<String> msgJsonList = new ArrayList<>();
		List<String> msgJson = configService.getValueListByTypeAndKey(WECHAT_MSG, dictKey.toUpperCase());
		for (String msg : msgJson) {
			msgJsonList.add(msg.replace("OPENID", toUserName));
		}
		return msgJsonList;
	}

	/**
	 * 统一组装微信带参二维码客服回复消息（列表）
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月12日 下午12:56:31
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	private List<String> createQrcodeWithParamMsgList(String eventKey) throws Exception {
		// 二维码参数集合,格式：[渠道,活动标记,分享人openId]
		String[] paramArr = eventKey.replace("qrscene_", "").split("~");
		// 活动标记
		String activityMark = paramArr[1];
		// 获取分享人openId
		String openId = paramArr[2];
		String dictKey = (paramArr[0] + "_" + activityMark).toUpperCase();
		List<String> msgJsonList = new ArrayList<>();
		List<String> msgJson = configService.getValueListByTypeAndKey(WECHAT_MSG, dictKey.toUpperCase());
		for (String msg : msgJson) {
			msgJsonList.add(msg.replace("OPENID", openId));
		}
		return msgJsonList;
	}

	/**
	 * 组装文本消息
	 * 
	 * @author hutao
	 * @date 创建时间：2018年4月12日 下午6:04:54
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	private String createTextMessage(String channel, String toUserName, String switchType) {
		JSONObject textMsg = new JSONObject();
		textMsg.put("touser", toUserName);
		textMsg.put("msgtype", WechatConstants.MESSAGE_TYPE_TEXT);
		JSONObject contentJson = new JSONObject();
		String dictKey = channel + "_" + switchType;
		String content = configService.getValueByTypeAndKey(WECHAT_MSG, dictKey.toUpperCase());
		// 若为空则默认返回客服中心统一消息
		if (StringUtils.isBlank(content)) {
			content = configService.getValueByTypeAndKey(WECHAT_MSG, (channel + "_SERVICE").toUpperCase());
		}
		contentJson.put("content", content);
		textMsg.put("text", contentJson);
		String result = textMsg.toString();
		result = result.replaceAll("%s", "\n");
		log.info("微信回调事件-被动回复文本消息=" + result);
		return result;
	}

	/**
	 * 
	 * @author hutao
	 * @date 创建时间：2018年9月16日 下午3:59:09
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	private void fissionActivityCallback(String channel, String fromUserName, String eventKey) {
		// 1.若是自己扫码关注自己则不进行处理
		if (eventKey.contains(fromUserName))
			return;
		// 2.通过异步线程处理
		new Thread(new java.lang.Runnable() {
			@Override
			public void run() {
				// 以~结尾标识活动带参二维码，非以~结尾标识个人带参二维码
				if (!eventKey.endsWith("~")) {
					// Long addCount = stringRedis.opsForSet().add(eventKey,
					// fromUserName);
					// //默认set 30天有效
					// stringRedis.expire(eventKey, 30, TimeUnit.DAYS);
					// log.info("兜礼裂变活动，添加redis到set，addCount={}",addCount);
					String toUserName = eventKey.substring(eventKey.lastIndexOf("~") + 1);
					com.alibaba.fastjson.JSONObject token = WechatUtil.getAccessTokenTicketRedisByChannel(channel);
					String accessToken = token.getString("accessToken");
					Integer invitationCount = wechatEventPushService.selectCountByEventKey(eventKey);
					// 只有参与人数大于0才推送消息给分享人
					if (invitationCount > 0) {
						Integer maxInvitationCount = Integer
								.valueOf(configService.getValueByTypeAndKey(ActivityConstants.ACTIVITY_TYPE,
										ActivityConstants.DOOOLY_FISSION_V1_ACTIVITY_INVITATION_NUMBER));
						String customMsg = null;
						if (maxInvitationCount >= invitationCount) {
							customMsg = configService.getValueByTypeAndKey(ActivityConstants.ACTIVITY_TYPE,
									ActivityConstants.DOOOLY_FISSION_V1_ACTIVITY_TEMPLATE_NOTICE);
							UserInfo userinfo = ThirdPartyWechatUtil.getUserInfo(accessToken, fromUserName);
							String nickName = userinfo.getNickname();
							customMsg = customMsg.replace("KEYWORD1", nickName)
									.replace("KEYWORD2", DateUtils.getDate(DateUtils.parsePatterns[1]))
									.replace("NUMBER", String.valueOf(invitationCount)).replace("TOUSER", toUserName);
							ThirdPartyWechatUtil.sendTemplateMsg(customMsg, accessToken);

							if (maxInvitationCount == invitationCount) {
								customMsg = configService.getValueByTypeAndKey(ActivityConstants.ACTIVITY_TYPE,
										ActivityConstants.DOOOLY_FISSION_V1_ACTIVITY_TEMPLATE_COMPLETION);
								customMsg = customMsg.replace("KEYWORD3", DateUtils.getDate(DateUtils.parsePatterns[1]))
										.replace("NUMBER", String.valueOf(invitationCount))
										.replace("TOUSER", toUserName);
								ThirdPartyWechatUtil.sendTemplateMsg(customMsg, accessToken);
							}
							log.info("兜礼裂变v1活动，发送模板消息结果={}", customMsg);
						} else {
							log.info("兜礼裂变v1活动分享人任务已完成，不再推送模板消息，eventKey={}", eventKey);
						}
					}
				}
				try {
					handleIsPush(channel, fromUserName, eventKey);
				} catch (Exception e) {
					log.error("兜礼裂变v1活动带参二维码回调异常", e);
				}
				log.info("兜礼裂变v1活动带参二维码回调成功，channel={},fromUserName={},eventKey={}={}", channel, fromUserName,
						eventKey);
			}
		}

		).start();
	}
}
