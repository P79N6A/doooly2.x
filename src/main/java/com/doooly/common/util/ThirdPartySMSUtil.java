package com.doooly.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.ThirdPartySMSConstatns;
import com.doooly.common.constants.ThirdPartySMSConstatns.AlidayuConfig.AlidayuSMSSwitch;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;


public class ThirdPartySMSUtil {
	protected  static Logger logger = LoggerFactory.getLogger(ThirdPartySMSUtil.class);
	public static ConcurrentHashMap<String, JSONObject> alidayuMap = new ConcurrentHashMap<>();

	// 短信通道
	/**
	 * 
	 * @param mobiles 手机号列表
	 * @param paramJson 短信模板可变参数
	 * @param alidayuSMSCode 阿里大鱼短信模板编号
	 * @param smsContent 短信内容（含可变参数也可不含），给其它短信平台使用
	 * @return
	 */
	public static int sendMsg(String mobiles,JSONObject paramJson,String alidayuSMSCode, String smsContent, Boolean alidayuFlag) {
		List<AlidayuSMSSwitch> alidayuSmsList = ThirdPartySMSConstatns.AlidayuConfig.alidayuSmsSwitchList;
		int result = -1;
		if(alidayuFlag){
			// 阿里大于短信通道发送
			for(AlidayuSMSSwitch smsSwitch:alidayuSmsList){
				if(StringUtils.isBlank(alidayuSMSCode)){
					alidayuSMSCode = smsSwitch.SMS_TEMPLATE_CODE;
				}
				result = alidayuSms(mobiles, alidayuSMSCode, paramJson, smsSwitch.APP_KEY, smsSwitch.APP_SECRET);
				if(result==0)
				{
					return result;
				}
			}
		}else{
			if(StringUtils.isBlank(smsContent)){
				smsContent = ThirdPartySMSConstatns.JiayiConfig.SMS_TEMPLATE;
			}
			result = jiayiSms(mobiles, smsContent);
		}
		return result;
	}
	/**
	 * 阿里大鱼短信通道
	 * 
	 * @param mobiles
	 * @param smsTemplate
	 * @param paramJson 短信模板参数值
	 * @param appKey
	 * @param appSecret
	 * @return
	 */
	private static int alidayuSms(String mobiles, String smsTemplate, JSONObject paramJson, String appKey, String appSecret) {
		TaobaoClient client = new DefaultTaobaoClient(ThirdPartySMSConstatns.AlidayuConfig.URL, appKey, appSecret);
		logger.info("url = {},appKey={},appSecret={}", ThirdPartySMSConstatns.AlidayuConfig.URL, appKey, appSecret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("extend");
		req.setSmsType("normal");
		req.setSmsFreeSignName(ThirdPartySMSConstatns.AlidayuConfig.SMS_FREE_SIGN_NAME);
		req.setSmsParamString(paramJson.toJSONString());
		req.setRecNum(mobiles);
		req.setSmsTemplateCode(smsTemplate);
		logger.info("req = {}", JSONObject.toJSONString(req));
		System.out.println(JSONObject.toJSONString(req));
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);

			if (rsp.getBody().toString().indexOf("error_response") != -1)
				return 1;
			JSONObject myjson = JSONObject.parseObject(rsp.getBody().toString());
			myjson = myjson.getJSONObject("alibaba_aliqin_fc_sms_num_send_response");
			myjson = myjson.getJSONObject("result");
			logger.info(String.format("\nalibaba=  %s", myjson.toJSONString()));
//			return Integer.parseInt(myjson.get("err_code").toString());
			return myjson.getBoolean("success")?0:1;
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return 1;
	}
	/**
	 * 嘉戈短信通道
	 * 
	 * @param mobiles
	 * @param content
	 * @return
	 */
	private static int jiayiSms(String mobiles, String content) {
		String rec_string="";
		URL url = null;
		BufferedReader rd = null;
		HttpURLConnection urlConn = null;
		OutputStream out = null;
		try {
			url = new URL(ThirdPartySMSConstatns.JiayiConfig.URL); // 根据数据的发送地址构建URL
			urlConn = (HttpURLConnection) url.openConnection(); // 打开链接
			urlConn.setConnectTimeout(30000); // 链接超时设置为30秒
			urlConn.setReadTimeout(30000); // 读取超时设置30秒
			urlConn.setRequestMethod("POST"); // 链接相应方式为post
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			out = urlConn.getOutputStream();
			String info = String
					.format("action=send&userid=%s&account=%s&password=%s&mobile=%s&content=%s",
							ThirdPartySMSConstatns.JiayiConfig.USERID, ThirdPartySMSConstatns.JiayiConfig.ACCOUNT, ThirdPartySMSConstatns.JiayiConfig.PASSWORD, mobiles, content);
			out.write(info.getBytes("UTF-8"));
			out.flush();
			out.close();
			rd = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream(), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rec_string = sb.toString().trim();
			logger.info( rec_string.replace("\n", ""));
			if(rec_string.contains("<returnstatus>Success")){
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			rec_string = "-107";
			System.out.println(e);
		} finally {
			if(rd!=null){
				try {
					rd.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
		return -1;
	}
}
