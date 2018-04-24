package com.doooly.dto.common;

import java.util.Map;

/**
 * 分享返回结果封装bean
 * 
 * @author wang
 *
 */
public class ShareRetMsg extends MessageDataBean{
	
	public void ShareRetMsg() {
	}
	
	public ShareRetMsg(String success_code, String success_mess, Map<String, Object> shareConfig) {
		super(success_code, success_mess, shareConfig);
	}
	//微信返回结果
	public static final String WECHAT_APP_CODE = "4001";
	public static final String WECHAT_APP_MSG = "成功获得微信APP分享配置";
	//兜礼返回结果
	public static final String DOOOLY_APP_CODE = "4002";
	public static final String DOOOLY_APP_MSG = "成功获得兜礼APP分享配置";
}
