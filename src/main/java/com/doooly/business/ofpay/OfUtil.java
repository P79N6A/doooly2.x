package com.doooly.business.ofpay;

import com.doooly.common.constants.PropertiesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class OfUtil {
	private final static Logger logger = LoggerFactory.getLogger(OfUtil.class);
	private static boolean validating = false;
	//欧飞商家参数
	public static final String KEYSTR = PropertiesHolder.getProperty("keystr");
	public static final String USERID = PropertiesHolder.getProperty("userid");
	public static final String USERPWS = PropertiesHolder.getProperty("userpws");
	// 话费参数
	public static final String CARDID = PropertiesHolder.getProperty("mobile.cardid");
	public static final String MOBILE_NOTIFYURL = PropertiesHolder.getProperty("mobile.notifyurl");
	public static final String MOBILE_URL = PropertiesHolder.getProperty("mobile.url");
	// 流量参数
	public static final String FLOW_NOTIFYURL = PropertiesHolder.getProperty("flow.notifyurl");
	public static final String FLOW_URL = PropertiesHolder.getProperty("flow.url");
	public static final String RANGE = PropertiesHolder.getProperty("flow.range");
	public static final String EFFECTSTARTTIME = PropertiesHolder.getProperty("flow.effectstarttime");
	public static final String EFFECTTIME = PropertiesHolder.getProperty("flow.effecttime");
	public static final String NETTYPE = PropertiesHolder.getProperty("flow.nettype");
	// 卡密参数
	public static final String CARDPSW_URL = PropertiesHolder.getProperty("cardpsw.url");
	// 查询手机号当时是否可以充值话费
	//public static final String TELCHECK_URL = PropertiesHolder.getProperty("telcheck.url");
	public static final String TELCHECK_URL = PropertiesHolder.getProperty("telcheck.url");
	// 查询手机号是否可以充值流量
	public static final String FLOWCHECK_URL = PropertiesHolder.getProperty("flowcheck.url");
	// 查询卡密库存
	public static final String QUERYLEFTCARDNUM_URL = PropertiesHolder.getProperty("queryLeftcardNum.url");
	
//	private static final String KEYSTR = "33082cb5044f48ea";
//	private static final String USERID = "A1406813";
//	private static final String USERPWS = "gym12345678";
//	//话费参数
//	private static final String CARDID = "140101";
//	private static final String MOBILE_NOTIFYURL = "http://admin.doooly.com/dooolytest/jersey/payService/mobileCallback";
//	private static final String MOBILE_URL = "http://api2.ofpay.com/onlineorder.do";
//	//流量参数
//	private static final String FLOW_NOTIFYURL = "http://www.doooly.com/api/flowCallback";
//	private static final String FLOW_URL = "http://api2.ofpay.com/flowOrder.do";
//	private static final String RANGE = "2";
//	private static final String EFFECTSTARTTIME = "1";
//	private static final String EFFECTTIME = "1";
//	private static final String NETTYPE = "";
//	//卡密参数
//	private static final String CARDPSW_URL = "http://api2.ofpay.com/order.do";
	
	//面值对应流量
	public static Map<Integer, String> PER_VALUES = new HashMap<Integer, String>();
	static {
		PER_VALUES.put(5, "30M");
		PER_VALUES.put(10, "100M");
		PER_VALUES.put(20, "300M");
		PER_VALUES.put(30, "500M");
		PER_VALUES.put(50, "1G");
		PER_VALUES.put(70, "2G");
		PER_VALUES.put(90, "2G");
	}
	
	/**
	 * 手机话费充值返回错误码
	 */
	private static Map<String,String> MOBILE_ERR_MAP = new HashMap<String,String>();
	static {
		MOBILE_ERR_MAP.put("9998","参数格式错误");
		//errMap.put("1043","支付超时，订单处理失败(不能作失败处理，需要人工核实)");
		//errMap.put("334","订单生成超时(不能作为失败处理，需要人工核实)");
		MOBILE_ERR_MAP.put("331","订单生成失败");
		MOBILE_ERR_MAP.put("321","暂时不支持此类手机号的充值");
		MOBILE_ERR_MAP.put("319","充值的手机号不正确");
		//errMap.put("105","请求失败(不能作失败处理，需要人工核实)");
		//errMap.put("9999","未知错误(不能作失败处理，需要人工核实)");
		MOBILE_ERR_MAP.put("1008","缺少必需参数");
		MOBILE_ERR_MAP.put("1006","充值金额超出系统限制");
		MOBILE_ERR_MAP.put("1007","账户金额不足");
		MOBILE_ERR_MAP.put("1005","购买的商品数量超出系统要求");
		MOBILE_ERR_MAP.put("1003","MD5串验证错误");
		MOBILE_ERR_MAP.put("1002","商户IP验证错误");
		MOBILE_ERR_MAP.put("11","运营商地区维护，暂不能充值");
		MOBILE_ERR_MAP.put("1001","商户名验证错误");
	}
	public static boolean mobileCanRefund(String retCode) {
		return MOBILE_ERR_MAP.containsKey(retCode);
	}
	
	
	private static Map<String,String> FLOW_ERR_MAP = new HashMap<String,String>();
	static {
		FLOW_ERR_MAP.put("9998","参数格式错误");
//		FLOW_ERR_MAP.put("1043","支付超时，订单处理失败(不能作失败处理，需要人工核实)");
//		FLOW_ERR_MAP.put("334","订单生成超时(不能作为失败处理，需要人工核实)");
		FLOW_ERR_MAP.put("331","订单生成失败");
		FLOW_ERR_MAP.put("321","暂时不支持此类手机号的充值");
		FLOW_ERR_MAP.put("319","充值的手机号不正确");
		FLOW_ERR_MAP.put("305","充值失败未知错误");
//		FLOW_ERR_MAP.put("105","请求失败(不能作失败处理，需要人工核实)");
//		FLOW_ERR_MAP.put("9999","未知错误(不能作失败处理，需要人工核实)");
		FLOW_ERR_MAP.put("1008","缺少必需参数");
		FLOW_ERR_MAP.put("1006","充值金额超出系统限制");
		FLOW_ERR_MAP.put("1007","账户金额不足");
		FLOW_ERR_MAP.put("1005","购买的商品数量超出系统要求");
		FLOW_ERR_MAP.put("1003","MD5串验证错误");
		FLOW_ERR_MAP.put("1002","商户IP验证错误");
		FLOW_ERR_MAP.put("11","运营商地区维护，暂不能充值");
		FLOW_ERR_MAP.put("1001","商户名验证错误");
	}
	public static boolean flowCanRefund(String retCode) {
		return FLOW_ERR_MAP.containsKey(retCode);
	}
	

	public static Map<String, String> parseOfcardResponse(String xmlString) {
		try {
			Map<String, String> result = new HashMap<String, String>();
			StringReader stringReader = new StringReader(xmlString);
			InputSource inputSource = new InputSource(stringReader);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(validating);
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
			return result;
			//logger.info("parseOfcardResponse result = {}", result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e = {}, xmlString = {}", e, xmlString);
		}
		return null;
	}

}
