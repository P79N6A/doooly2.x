package com.doooly.business.common.service;

import java.awt.image.BufferedImage;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachlife.LifeWechatBinding;

/**
 * @version 1.0
 */
public interface ActivityCodeImageServiceI {
	//画布合并图片
	public BufferedImage overlapImage(BufferedImage big, BufferedImage small,int height);
	//推送文字消息
	public String sendLinkText(String accessToken,String openId,JSONObject linkUrl,String sourceOpenId);
	//推送图片
	public String sendImage(String accessToken,String openId,String mediaId);
	//推送活动消息
	public String sendActivityText(String accessToken, String openId,String sourceOpenId);
	
	public String sendActivityImage(String accessToken, String openId, String mediaId);
	//生成微信带参二维码
	public String getQRCodeUrl(String accessToken, String openId, String channel, String activityId);

	public MessageDataBean pushImageAndText(String openId, String sourceOpenId, String channel, String activityId) throws Exception;
	//根据活动分别推送图文信息
	public MessageDataBean pushNews(String openId, String channel, String activityKey) throws Exception;

	public JSONObject getPhoneFee(String key);
	//删除redis 的key
	public void deleteTextInRedis();
	//获取微信昵称和头像
	public LifeWechatBinding getWechatData(String openId);

	String sendNews(String accessToken, String openId,List<JSONObject>articles);

}
