package com.doooly.publish.rest.reachad.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.publish.rest.life.impl.BusinessPrivilegeRestService;
import com.doooly.publish.rest.reachad.HttpsForPayService;
import com.google.gson.JsonObject;

/**
 * 
 * @author 赵清江
 * @date 2016年12月16日
 * @version 1.0
 */
@Component
@Path("/https")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HttpsForPay implements HttpsForPayService {

	 private static Logger logger = Logger.getLogger(HttpsForPay.class);
	
	@POST
	@Path(value="/forPay")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String HttpsForPayServicePublishI(JSONObject json) {
		//String iString = "{'orderNumber':'13441006','orderSerialDetail':[{'amount':'13.5','serialNumber':'34456789123we','payType':0,'orderDate':'5226-12-22 00:00:00','pointCardNumber':'18670722771'}],'storesId':'A001','businessId':'TEST_0d4e9c9a82eer97d866yanxuan','orderDetail':'[{'categoryOne':'服饰','number':'1','amount':'9.5','code':'AD00001','price':'9.5','categoryTwo':'男装','goods':'线上问题复现用商品01','tax':'0','category':'1111'},{'categoryOne':'服饰','number':'1','amount':'4','code':'A00091','price':'4.00','categoryTwo':'男装','goods':'运费','tax':'0','category':'0000'}]','orderPrice':'1.50','cardNumber':'18670722771'}";
//		JSONObject.parseObject(iString);
		logger.info("调用接口参数:+++"+json.toJSONString());
		String url = json.getString("url");
		String jsondata= json.getString("data");
		JSONObject retu = new JSONObject();
		try {
			String sendPost = HTTPSClientUtils.sendPostNew(jsondata, url);
//			return "";
			return sendPost;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retu.put("code", "4000");
			return retu.toJSONString();
		}	
	}

	
	
	

}
