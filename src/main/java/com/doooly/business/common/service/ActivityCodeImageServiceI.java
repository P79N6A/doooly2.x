package com.doooly.business.common.service;

import java.awt.image.BufferedImage;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachlife.LifeWechatBinding;

/**
 * @version 1.0
 */
public interface ActivityCodeImageServiceI {

	public BufferedImage overlapImage(BufferedImage big, BufferedImage small,int height);
	
	public String sendText(String accessToken,String openId,JSONObject linkUrl,String sourceOpenId);

	public String sendImage(String accessToken,String openId,String mediaId);

	public String sendActivityText(String accessToken, String openId,String sourceOpenId);

	public String sendActivityImage(String accessToken, String openId, String mediaId);
	
	public String getQRCodeUrl(String accessToken, String openId, String channel, String activityId);

	public MessageDataBean pushImageAndText(String openId, String sourceOpenId, String channel, String activityId) throws Exception;

	public JSONObject getPhoneFee();

	public void deleteTextInRedis();
	
	public LifeWechatBinding getWechatData(String openId);

}
