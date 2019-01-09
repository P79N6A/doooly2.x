package com.doooly.publish.rest.wechat;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.wechat.WechatDevCallbackServiceI;
import com.doooly.common.util.WechatUtil;
import com.wechat.ThirdPartyWechatUtil;

@Component
@Path("/wechat")
public class WechatDevCallback {
	private static Logger log = LoggerFactory.getLogger(WechatDevCallback.class);

	@Autowired
	private WechatDevCallbackServiceI wechatCallbackService;

	/**
	 * 确认请求来自微信服务器
	 */
	@GET
	@Path(value = "/callback/{channel}")
	public String callback(@Context HttpServletRequest request) throws ServletException, IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		String result = wechatCallbackService.checkSignature(signature, timestamp, nonce) ? echostr : "";
		if (StringUtils.isNotBlank(result)) {
			log.info("微信开发者模式签名校验成功");
		} else {
			log.warn("微信开发者模式签名校验失败");
		}
		return result;
	}

	/**
	 * 处理微信服务器发来的消息(兜礼公众号)
	 * 
	 * @param channel
	 *            - 公众号渠道(渠道区分,获取对应公众号配置)
	 */
	@POST
	@Path(value = "/callback/{channel}")
	public String callback(@RequestBody String xmlStr, @PathParam("channel") String channel)
			throws ServletException, IOException {
		//针对回复多条消息出现提示（该公众号出现的服务出现故障，请稍后重试），优化为异步线程处理
		new Thread(new Runnable() {
			@Override
			public void run() {
				log.info("微信回调事件-请求参数xml=" + xmlStr);
				JSONObject accessTokenTicket = WechatUtil.getAccessTokenTicketRedisByChannel(channel);
				String token = accessTokenTicket.getString(WechatUtil.ACCESS_TOKEN);

				List<String> messageList = wechatCallbackService.dealCallback(xmlStr, channel);
				if (CollectionUtils.isNotEmpty(messageList)) {
					try {
						for (String message : messageList) {
							ThirdPartyWechatUtil.sendCustomMessage(message, token);
						}
					} catch (Exception e) {
						log.error("微信回调事件处理失败，error=" + e.getMessage(), e);
					}
				}
			}
		}).start();

		return "";
	}
	/**
	 * 用于测试-
	 * 处理微信服务器发来的消息(兜礼公众号)
	 * 
	 * @param channel
	 *            - 公众号渠道(渠道区分,获取对应公众号配置)
	 */
	@POST
	@Path(value = "/callback2/{channel}")
	public String callbackv2(@RequestBody String xmlStr, @PathParam("channel") String channel, @Context HttpServletRequest req, @Context HttpServletResponse res)
			throws ServletException, IOException {
			log.info("微信回调事件-请求参数xml=" + xmlStr);
			JSONObject accessTokenTicket = WechatUtil.getAccessTokenTicketRedisByChannel(channel);
			String token = accessTokenTicket.getString(WechatUtil.ACCESS_TOKEN);
			String resXml = "<xml><ToUserName><![CDATA[$toUserName]]></ToUserName><FromUserName><![CDATA[$fromUserName]]></FromUserName><CreateTime>$createTime</CreateTime><MsgType><![CDATA[news]]></MsgType><ArticleCount>2</ArticleCount><Articles><item><Title><![CDATA[搜索引擎]]></Title><Description><![CDATA[感谢关注]]></Description><PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz_jpg/nWY3YXezOzWbBOyZJMz6icMUfERSXds1w1pPaFAWeC76icNL4ZEiccAPLGsibuyP38zuknbuv7CJaFg1ILSqDkUHAg/0]]></PicUrl><Url><![CDATA[http://imooc.com]]></Url></item><item><Title><![CDATA[搜索引擎]]></Title><Description><![CDATA[感谢关注]]></Description><PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz_jpg/nWY3YXezOzWbBOyZJMz6icMUfERSXds1w1pPaFAWeC76icNL4ZEiccAPLGsibuyP38zuknbuv7CJaFg1ILSqDkUHAg/0]]></PicUrl><Url><![CDATA[http://imooc.com]]></Url></item></Articles></xml>";
			org.json.JSONObject json = XML.toJSONObject(xmlStr).getJSONObject("xml");
			String fromUserName = json.getString("FromUserName");
			String toUserName = json.getString("ToUserName");
			resXml = resXml.replace("$toUserName", fromUserName);
			resXml = resXml.replace("$createTime", System.currentTimeMillis()+"");
			resXml = resXml.replace("$fromUserName", toUserName);
			log.info("微信回调事件-返回xml=" + resXml);
			res.setContentType("text/xml;charset=UTF-8");
			ServletOutputStream outputStream = res.getOutputStream();
			outputStream.write(resXml.getBytes("utf-8"));
			outputStream.flush();
			outputStream.close();
		return "";
	}
}
