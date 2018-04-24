package com.doooly.common.webservice;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.PropertiesConstants;
import com.reachlife.pay.client.CheckAddIntegralAuthorizationDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class WebService {

	protected static Logger logger = LoggerFactory.getLogger(WebService.class);
//	//本地测试
//	public static String WEBURL = "https://pay.reach-life.com:8448/api/";
//	public static String businessid = "TEST_0d4e9c9ae4f81ee0838797d849c69c361";
//	public static String businessid = "TEST_4sf16a17faad3189c3fa85zhongxinlvyou";
//	public static String storesId = "2";
//	public static String cardNumber = "18616006011";
//	public static String USERNAME="reachlife558558";
//	public static String PASSWORD="reachlifeOwerFQ";
//	public static String SSL_TRUST_STORE = "/Users/john/Documents/workspace/Doooly-master/src/main/resources/SSL/client.truststore";
//	public static String SSL_KEY_STORE =  "/Users/john/Documents/workspace/Doooly-master/src/main/resources/SSL/liketry.p12";

	public final static String ADDINTEGRALAUTHORIZATION = PropertiesConstants.dooolyBundle.getString("ws.addIntegralAuthorization");

	/***************pay接口配置***************/
	public final static String WEBURL = PropertiesConstants.dooolyBundle.getString("ws.url");
	public final static String BUSINESSID = PropertiesConstants.dooolyBundle.getString("ws.businessid");
	public final static String STOREID = PropertiesConstants.dooolyBundle.getString("ws.storeid");
	public final static String USERNAME = PropertiesConstants.dooolyBundle.getString("ws.username");
	public final static String PASSWORD = PropertiesConstants.dooolyBundle.getString("ws.password");
    public final static String PAY_POINT_URL = PropertiesConstants.dooolyBundle.getString("pay.point.url");
    public final static String PAY_CODE_URL = PropertiesConstants.dooolyBundle.getString("pay.code.url");



    public static String SSL_TRUST_STORE = getRootPath() + "/SSL/client.truststore";
	public static String SSL_KEY_STORE =  getRootPath() + "/SSL/liketry.p12";

	public static String getRootPath() {
		String classPath = WebService.class.getClassLoader().getResource("/").getPath();
		String rootPath = "";
		//windows下
		if("\\".equals(File.separator)){
			rootPath = classPath.substring(1,classPath.indexOf("/WEB-INF"));
			rootPath = rootPath.replace("%20", " ");
		}
		//linux下
		if("/".equals(File.separator)){
			rootPath = classPath.substring(0,classPath.indexOf("/WEB-INF"));
		}
		return rootPath;
	}

	/***
	 * 在com.doooly.common.context.SpringContextLoaderListener中初始.
	 */
	public static void initSSL(){
//		try {
//			logger.info("==============SSLService init starting==============================");
//			logger.info("trustStore = {} ,keyStore = {}", System.getProperty("javax.net.ssl.trustStore"), System.getProperty("javax.net.ssl.keyStore"));
//			//System.setProperty("javax.net.ssl.trustStore", SSL_TRUST_STORE);
//			System.setProperty("javax.net.ssl.trustStorePassword", "qscesz123456");
//			System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
//			//System.setProperty("javax.net.ssl.keyStore", SSL_KEY_STORE);
//			System.setProperty("javax.net.ssl.keyStorePassword", "qweasdzxc123456");
//			logger.info("==============SSLService init ended==============================");
//		} catch (Exception e) {
//			logger.error("initSSL e = {}", e);
//		}
	}

	public static Service init(String serviceUrl,String serviceName){
		try {
			URL url = new URL(serviceUrl);
			QName qname = new QName("http://pay.reachlife.com/", serviceName);
			return Service.create(url, qname);
		} catch (Exception e) {
			logger.info("==============SSLService==============================");
			logger.error("1 = {}",System.getProperty("javax.net.ssl.trustStore"));
			logger.error("2 = {}",System.getProperty("javax.net.ssl.trustStorePassword"));
			logger.error("3 = {}",System.getProperty("javax.net.ssl.keyStoreType"));
			logger.error("4 = {}",System.getProperty("javax.net.ssl.keyStore"));
			logger.error("5 = {}",System.getProperty("javax.net.ssl.keyStorePassword"));
			logger.info("==============SSLService==============================");

			initSSL();
			logger.error("init e = {}", e);
		}
		return null;
	}

	/***
	 * 退款接口
	 * @param params
	 * @throws MalformedURLException
	 */
	public static JSONObject checkAddIntegralAuthorizationPort(JSONObject params)
	{
		try {
			String businessId = params.getString("businessId");
			String storesId = params.getString("storesId");
			String integral = params.getString("integral");
			JSONArray array = createRefundPointDetail(params.getJSONObject("orderDetail"));
			JSONObject orderDetailJson = new JSONObject();
			orderDetailJson.put("orderNumber", params.getString("orderNumber"));
			orderDetailJson.put("serialNumber", params.getString("serialNumber"));
			orderDetailJson.put("orderDetail", array);
			String S_URL = WEBURL + "CheckAddIntegralAuthorizationPort";
			Service service = init(S_URL, "CheckAddIntegralAuthorizationService");
			CheckAddIntegralAuthorizationDelegate port = service.getPort(CheckAddIntegralAuthorizationDelegate.class);
			String mPointResult = port.checkAddIntegralAuthorization(businessId, storesId,
					integral, params.getString("cardNumber"), orderDetailJson.toJSONString(),
					params.getIntValue("type"), USERNAME, PASSWORD);
			return JSONObject.parseObject(mPointResult);
		} catch (Exception e) {
			logger.info("checkAddIntegralAuthorizationPort = {}", e);
		}
		return null;
	}

	public static JSONArray createRefundPointDetail(JSONObject params) {
		JSONArray orderDetails = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("code", params.getString("code"));
		json.put("goods", params.getString("goods"));
		json.put("number", params.getString("number"));
		json.put("price", params.getString("price"));
		json.put("category", params.getString("category"));
		orderDetails.add(json);
		return orderDetails;
	}

	public static void main(String[] args) throws MalformedURLException {
		JSONObject params = new JSONObject();
		params.put("orderNumber", "150891939430513273N");
		params.put("serialNumber", "150891939430513273N");
		params.put("cardNumber", "774770000014");
		params.put("integral", "0.01");
		params.put("type", "5");
		params.put("businessId", "TEST_0d4e9c9ae4f81ee0838797d849c69c361");
		params.put("storesId", "2");

		JSONObject json = new JSONObject();
		json.put("code", "12195058");
		json.put("goods", "12195058");
		json.put("number", "1");
		json.put("price", "50");
		params.put("orderDetail", json);
		JSONObject ret = checkAddIntegralAuthorizationPort(params) ;
		System.out.println(ret);
	}



}
