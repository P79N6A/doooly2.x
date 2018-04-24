package com.doooly.publish.rest.life.impl;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.Constants;
import com.doooly.common.util.AppShareUtil;
import com.doooly.common.util.WechatUtil;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.ShareRetMsg;
import com.doooly.publish.rest.life.ShareWithFriendsServiceI;

@Component
@Path("/share")
public class ShareWithFriendsService implements ShareWithFriendsServiceI {

	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);
	
	@GET
	@Path(value = "/commonShareJSONPConfig")
	@Produces("application/json")  
	public String commonShareJSONPConfig(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		setAccessControl(request, response);
		return "jsonpCallback("+  commonShareConfig(request,response)+ ");";
	}
	
	@GET
	@Path(value = "/commonShareConfig")
	@Produces("application/json")  
	public String commonShareConfig(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		String client = request.getParameter("client");
		String prefix = request.getParameter("prefix");
		String[] params = request.getParameterValues("params");
		if(params!= null && params.length > 0){
			log.info("params = {}", Arrays.asList(params));
		}

		if (Constants.WECHAT.equals(client)) {
			//微信分享配置
			String url = request.getParameter("url");
			Map<String,Object> config = WechatUtil.getWechatConfig(url);
			if(config != null){
				Map<String,Object> shareConfig = AppShareUtil.getShareInfo(client,prefix,params);
				config.put("shareConfig", shareConfig);
				log.info("config = {}",config);
				return new ShareRetMsg(ShareRetMsg.WECHAT_APP_CODE, ShareRetMsg.WECHAT_APP_MSG,config).toJsonString();
			}
			return new ShareRetMsg(ShareRetMsg.failure_code, MessageDataBean.failure_mess,null).toJsonString();
		} else if (Constants.DOOOLY.equals(client)) {
			//兜礼分享配置
			JSONObject jsonObject = new JSONObject();
			Map<String,Object> shareConfig = AppShareUtil.getShareInfo(client,prefix,params);
			jsonObject.put("shareConfig", shareConfig);
			log.info("shareConfig = {}",jsonObject);
			return  new ShareRetMsg(ShareRetMsg.DOOOLY_APP_CODE, ShareRetMsg.DOOOLY_APP_MSG,jsonObject).toJsonString();
		}
		return new ShareRetMsg(MessageDataBean.failure_code, MessageDataBean.failure_mess,null).toJsonString();
	}

	/**
	 * 允许跨域访问
	 * 
	 * @param request
	 * @param response
	 */
	private void setAccessControl(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Max-Age", "3600");
		// 如果IE浏览器则设置头信息如下
		if ("IE".equals(request.getParameter("type"))) {
			response.addHeader("XDomainRequestAllowed", "1");
		}
	}

}
