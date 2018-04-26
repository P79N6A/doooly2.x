package com.doooly.publish.rest.wechat;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.wechat.WechatDevCallbackServiceI;
import com.doooly.common.util.WechatUtil;
import com.wechat.ThirdPartyWechatUtil;
import com.wechat.vo.Menu;
import com.wechat.vo.MenuRes;
import com.wechat.vo.QRCode;
import com.wechat.vo.QRCodeRes;
import com.wechat.vo.WechatRes;

@Component
@Path("/wechats")
public class WechatDevRestService {
	private static Logger log = LoggerFactory.getLogger(WechatDevRestService.class);

	@Autowired
	private WechatDevCallbackServiceI wechatCallbackService;


	/**
	 * 创建微信菜单
	 * 
	* @author  hutao 
	* @date 创建时间：2018年4月13日 下午5:24:01 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/menu/create/{channel}")
	@Produces(MediaType.APPLICATION_JSON)
	public String createMenu(@RequestBody String json, @PathParam("channel") String weixinChannel){
		String token = WechatUtil.getAccessTokenTicketRedisByChannel(weixinChannel).getString(WechatUtil.ACCESS_TOKEN);
		log.info("请求参数json=" + json);
		Menu menu = (Menu)JSONObject.parseObject(json, Menu.class);
		WechatRes res = ThirdPartyWechatUtil.createMenu(menu, token);
		return JSONObject.toJSONString(res);
	}
	
	/**
	 * 查询微信菜单
	 * 
	* @author  hutao 
	* @date 创建时间：2018年4月13日 下午5:25:12 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/menu/query/{channel}")
	@Produces(MediaType.APPLICATION_JSON)
	public String queryMenu(@RequestBody String json, @PathParam("channel") String weixinChannel){
		String token = WechatUtil.getAccessTokenTicketRedisByChannel(weixinChannel).getString(WechatUtil.ACCESS_TOKEN);
		log.info("请求参数json=" + json);
		MenuRes res = ThirdPartyWechatUtil.queryMenu(token);
		return JSONObject.toJSONString(res);
	}
	
	/**
	 * 删除微信菜单
	 * 
	* @author  hutao 
	* @date 创建时间：2018年4月13日 下午5:25:29 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/menu/delete/{channel}")
	public String deleteMenu(@RequestBody JSONObject json, @PathParam("channel") String weixinChannel){
		String token = WechatUtil.getAccessTokenTicketRedisByChannel(weixinChannel).getString(WechatUtil.ACCESS_TOKEN);
		log.info("请求参数json=" + json);
		WechatRes res = ThirdPartyWechatUtil.deleteMenu(token);
		return JSONObject.toJSONString(res);
	}
	
	/**
	 * 生成微信带参二维码
	 * 
	* @author  hutao 
	* @date 创建时间：2018年4月16日 下午3:59:19 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/qrcode/create/{channel}")
	public String createWechatQrcode(@RequestBody JSONObject json, @PathParam("channel") String weixinChannel){
		String token = WechatUtil.getAccessTokenTicketRedisByChannel(weixinChannel).getString(WechatUtil.ACCESS_TOKEN);
		log.info("请求参数json=" + json);
		QRCode qrCode = (QRCode)JSONObject.parseObject(json.toJSONString(), QRCode.class);
		QRCodeRes res = ThirdPartyWechatUtil.createQRCode(qrCode, token);
		return JSONObject.toJSONString(res);
	}
	
}
