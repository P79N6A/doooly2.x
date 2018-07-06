
package com.doooly.business.ofpay.service.impl;

import com.doooly.business.ofpay.OfUtil;
import com.doooly.business.ofpay.service.OfPayService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.common.util.MD5Util;
import com.doooly.dto.common.PayMsg;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OfPayServiceImpl implements OfPayService {
	
	protected Logger logger = LoggerFactory.getLogger(OfPayServiceImpl.class);
	
	/**
	 * 话费充值
	 */
	@Override
	public Map<String,String> mobileRecharge(OrderVo order) {
		OrderItemVo item = order.getItems().get(0);
		String sporder_id = order.getOrderNumber();
		int cardnum = item.getPrice().intValue();
		String game_userid = order.getConsigneeMobile();
		String sporder_time = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
		String md5userpws = MD5Util.digest(OfUtil.USERPWS, "gbk");
		String md5param = OfUtil.USERID + md5userpws + OfUtil.CARDID + cardnum + sporder_id +sporder_time+ game_userid + OfUtil.KEYSTR;
		String sign = MD5Util.digest(md5param, "gbk").toUpperCase();
		logger.info("md5param = {} , sign = {}", md5param, sign);
		String param = "userid=" + OfUtil.USERID 
					 + "&userpws=" + md5userpws 
					 + "&cardid=" + OfUtil.CARDID 
					 + "&cardnum="+ cardnum 
					 +"&mctype=&sporder_id=" + sporder_id 
					 + "&sporder_time="+ sporder_time 
					 + "&game_userid=" + game_userid 
					 + "&md5_str=" + sign 
					 + "&ret_url=" + OfUtil.MOBILE_NOTIFYURL 
					 + "&version=6.0";
		logger.info("mobileRecharge() url = {}, param = {}", OfUtil.MOBILE_URL, param);
		String resultXml = HttpClientUtil.httpsGet(OfUtil.MOBILE_URL + "?" + param);
		logger.info("mobileRecharge() url = {}, result = {}", OfUtil.MOBILE_URL, resultXml);
		return OfUtil.parseOfcardResponse(resultXml);
	}

	
	/**
	 * 流量充值
	 */
	@Override
	public Map<String,String> flowRecharge(OrderVo order) {
		String sporderId = order.getOrderNumber();
		OrderItemVo item = order.getItems().get(0);
		int perValue = item.getPrice().intValue();
		String flowValue = item.getSku().split("/")[1];
		String phoneno = order.getConsigneeMobile();
		String md5userpws = MD5Util.digest(OfUtil.USERPWS, "gbk");
		String md5param = OfUtil.USERID + md5userpws + phoneno + perValue + 
				flowValue + OfUtil.RANGE + OfUtil.EFFECTSTARTTIME + OfUtil.EFFECTTIME + OfUtil.NETTYPE + sporderId + OfUtil.KEYSTR;
		String sign = MD5Util.digest(md5param, "gbk").toUpperCase();
		logger.info("md5param = {} , sign = {}", md5param, sign);
		String param = "userid=" + OfUtil.USERID  
					+ "&userpws=" + md5userpws  
					+ "&phoneno=" + phoneno 
					+ "&perValue="+ perValue 
					+ "&flowValue=" + flowValue 
					+ "&range="+ OfUtil.RANGE 
					+ "&effectStartTime="+ OfUtil.EFFECTSTARTTIME 
					+ "&effectTime="+ OfUtil.EFFECTTIME
					+ "&netType="+ OfUtil.NETTYPE
					+ "&sporderId="+ sporderId
					+ "&md5Str=" + sign 
					+ "&retUrl=" + OfUtil.FLOW_NOTIFYURL 
					+ "&version=6.0";
		logger.info("flowRecharge() url = {}, param = {}", OfUtil.FLOW_URL, param);
		String resultXml = HttpClientUtil.httpsGet(OfUtil.FLOW_URL + "?" + param);
		logger.info("flowRecharge() url = {}, resultXml = {}", OfUtil.FLOW_URL, resultXml);
		return OfUtil.parseOfcardResponse(resultXml);
	}

	@Override
	public Map<String, String> cardPswRecharge(OrderVo order) {
		OrderItemVo item = order.getItems().get(0);
		String cardid = item.getExternalNumber();
		int cardnum = item.getNumber().intValue();
		String sporder_id = order.getOrderNumber();
		String phone = order.getConsigneeMobile();
		String email = "";
		String sporder_time = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
		String md5userpws = MD5Util.digest(OfUtil.USERPWS, "gbk");
		String md5param = OfUtil.USERID + md5userpws + cardid + 
				cardnum + sporder_id 
				+ sporder_time 
				+ OfUtil.KEYSTR;
		String sign = MD5Util.digest(md5param, "gbk").toUpperCase();
		logger.info("md5param = {} , sign = {}", md5param, sign);
		String param = "userid=" + OfUtil.USERID + 
				"&userpws=" + md5userpws + 
				"&cardid=" + cardid + 
				"&cardnum=" + cardnum+ 
				"&sporder_id=" + sporder_id + 
				"&sporder_time=" + sporder_time + 
				"&md5_str=" + sign + 
				"&phone=" + phone + 
				"&email=" + email + 
				"&version=6.0";
		logger.info("cardPswRecharge() url = {}, param = {}", OfUtil.CARDPSW_URL, param);
		String resultXml = HttpClientUtil.httpsGet(OfUtil.CARDPSW_URL + "?" + param);
		//logger.info("cardPswRecharge() url = {}, resultXml = {}", OfUtil.CARDPSW_URL, resultXml);
		return OfUtil.parseOfcardResponse(resultXml);
	}
	
	/**
	 * 手机号验证是否可以充值
	 */
	@Override
	public PayMsg telCheck(String phoneNo,String price) {
		try {
			String param = "userid=" + OfUtil.USERID + "&phoneno=" + phoneNo + "&price=" + price;
			String result = HttpClientUtil.httpsGet(OfUtil.TELCHECK_URL + "?" + param);
			logger.info("telCheck() result = {}", result);
			if(StringUtils.isNoneBlank(result)){
				//1#成功#0000&江苏南京
				String str[] = result.split("#");
				if(!OF_SUCCESS_CODE.equals(str[0])){
					return new PayMsg(PayMsg.failure_code, str[1]);
				}
				return new PayMsg(PayMsg.success_code,PayMsg.success_mess);
			}
		} catch (Exception e) {
			logger.info("telCheck() e = {}, phoneNo = {},price = {}", e, phoneNo, price);
		}
		return new PayMsg(PayMsg.success_code,PayMsg.success_mess);
	}
	
	
	/**
	 * 手机号验证是否可以充值流量
	 */
	@Override
	public PayMsg flowCheck(String phoneNo,String price) {
		try {
			String flowValue = OfUtil.PER_VALUES.get(Integer.valueOf(price));
			String md5userpws = MD5Util.digest(OfUtil.USERPWS, "gbk");
			String param = "userid=" + OfUtil.USERID  
						+ "&userpws=" + md5userpws  
						+ "&phoneno=" + phoneNo 
						+ "&perValue="+ price 
						+ "&flowValue=" + flowValue 
						+ "&range="+ OfUtil.RANGE 
						+ "&effectStartTime="+ OfUtil.EFFECTSTARTTIME 
						+ "&effectTime="+ OfUtil.EFFECTTIME
						+ "&netType="+ OfUtil.NETTYPE
						+ "&version=6.0";
			logger.info("flowCheck() url = {}, param = {}", OfUtil.FLOWCHECK_URL, param);
			String resultXml = HttpClientUtil.httpsGet(OfUtil.FLOWCHECK_URL + "?" + param);
			logger.info("flowCheck() url = {}, resultXml = {}", OfUtil.FLOWCHECK_URL, resultXml);
			Map<String,String> res =  OfUtil.parseOfcardResponse(resultXml);
			if(OF_SUCCESS_CODE.equals(res.get("retcode"))){
				return new PayMsg(PayMsg.success_code, PayMsg.success_mess);
			}else{
				return new PayMsg(PayMsg.failure_code, res.get("err_msg"));
			}
		} catch (Exception e) {
			logger.info("flowCheck() e = {}, phoneNo = {},price = {}", e, phoneNo, price);
		}
		return new PayMsg(PayMsg.success_code,PayMsg.success_mess);
	}

	@Override
	public PayMsg queryLeftcardNum(String cardid) {
		if(StringUtils.isEmpty(cardid)){
			return new PayMsg(PayMsg.failure_code,"无效的外部编号");
		}
		String userid=OfUtil.USERID;
		String userpws=OfUtil.USERPWS;
		String  flowurl= OfUtil.QUERYLEFTCARDNUM_URL;
		String md5userpws = MD5Util.digest(userpws, "gbk");

		String param = "userid=" + userid
				+ "&userpws=" + md5userpws
				+ "&cardid=" + cardid
				+ "&version=6.0";
		String resultXml = HttpClientUtil.httpsGet(flowurl + "?" + param);
		System.out.println(resultXml);
		logger.info("queryLeftcardNum() url = {}, resultXml = {}", OfUtil.QUERYLEFTCARDNUM_URL, resultXml);
		Map<String,String> map  = OfUtil.parseOfcardResponse(resultXml);
		if(map != null){
            String innum = map.get("innum");
			if(innum != null){
				PayMsg payMsg = new PayMsg(PayMsg.success_code,PayMsg.success_mess);
				payMsg.data = new HashMap();
				payMsg.data.put("innum",innum);
				return  payMsg;
			}else{
				String err_msg = map.get("err_msg");
				String retcode = map.get("retcode");
				return new PayMsg(PayMsg.failure_code, err_msg );
			}

		}
		return new PayMsg(PayMsg.failure_code,PayMsg.failure_mess);
	}

	public static void main(String[] args) {
		//测试流量充值
		String cardid = "12404131";
		String userid="A08566";
		String userpws="of111111";
		String  flowurl="http://apitest.ofpay.com/queryleftcardnum.do";
		String md5userpws = MD5Util.digest(userpws, "gbk");

		String param = "userid=" + userid
				+ "&userpws=" + md5userpws
				+ "&cardid=" + cardid
				+ "&version=6.0";
		String resultXml = HttpClientUtil.httpsGet(flowurl + "?" + param);
		System.out.println(resultXml);

	}



	public static Map<String, String> parseOfcardResponse(String xmlString) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			StringReader stringReader = new StringReader(xmlString);
			InputSource inputSource = new InputSource(stringReader);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputSource);
			document.getDocumentElement().normalize();
			Node node = document.getFirstChild();
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node nodeItem = list.item(i);
				// 返回信息
				if (nodeItem.getNodeName().equals("err_msg")) {
					if (nodeItem.hasChildNodes()) {
						String err_msg = nodeItem.getFirstChild().getNodeValue();
						result.put("err_msg", err_msg);
					}
				}
				if (nodeItem.getNodeName().equals("retcode")) {
					if (nodeItem.hasChildNodes()) {
						String retcode = nodeItem.getFirstChild().getNodeValue();
						result.put("retcode", retcode);
					}
				}
				// 欧飞订单号
				if (nodeItem.getNodeName().equals("cardid")) {
					if (nodeItem.hasChildNodes()) {
						String cardid = nodeItem.getFirstChild().getNodeValue();
						result.put("cardid", cardid);
					}
				}
				// 处理状态
				if (nodeItem.getNodeName().equals("game_state")) {
					if (nodeItem.hasChildNodes()) {
						String game_state = nodeItem.getFirstChild().getNodeValue();
						result.put("game_state", game_state);
					}
				}
				//卡密
				if (nodeItem.getNodeName().equals("cards")) {
					if (nodeItem.hasChildNodes()) {
						NodeList cardList = nodeItem.getChildNodes();
						for (int j = 0; j < cardList.getLength(); j++) {
							Node cardItem = cardList.item(j);
							if (cardItem.getNodeName().equals("card")) {
								if (cardItem.hasChildNodes()) {
									NodeList cardPassList = cardItem.getChildNodes();
									for (int k = 0; k < cardPassList.getLength(); k++) {
										Node cardPassItem = cardPassList.item(k);
										if (cardPassItem.getNodeName().equals("cardno")) {
											String cardno = cardPassItem.getFirstChild().getNodeValue();
											result.put("cardno", cardno);
										}
										if (cardPassItem.getNodeName().equals("cardpws")) {
											String cardpws = cardPassItem.getFirstChild().getNodeValue();
											result.put("cardpws", cardpws);
										}
										if (cardPassItem.getNodeName().equals("expiretime")) {
											String expiretime = cardPassItem.getFirstChild().getNodeValue();
											result.put("expiretime", expiretime);
										}
									}
								}
							}
						}
					}
				}

				//卡密库存
				if (nodeItem.getNodeName().equals("ret_cardinfos")) {
					if (nodeItem.hasChildNodes()) {
						NodeList cardList = nodeItem.getChildNodes();
						for (int j = 0; j < cardList.getLength(); j++) {
							Node cardItem = cardList.item(j);
							if (cardItem.getNodeName().equals("card")) {
								if (cardItem.hasChildNodes()) {
									NodeList cardPassList = cardItem.getChildNodes();
									for (int k = 0; k < cardPassList.getLength(); k++) {
										Node cardPassItem = cardPassList.item(k);
										if (cardPassItem.getNodeName().equals("innum")) {
											String cardno = cardPassItem.getFirstChild().getNodeValue();
											result.put("innum", cardno);
										}
									}
								}
							}
						}
					}
				}

			}
			//logger.info("parseOfcardResponse result = {}", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
