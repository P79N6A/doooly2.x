package com.doooly.publish.rest.life.impl;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.scanDiscount.ScanDiscountServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.ScanDiscountRestServiceI;

@Component
@Path("/scanBusiness")
public class ScanDiscountRestService implements ScanDiscountRestServiceI{

	private static Logger logger = Logger.getLogger(ScanDiscountRestService.class);
	
	@Autowired
	private ScanDiscountServiceI scanDiscountServiceI;
	
	@POST
	@Path(value = "/getList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getBusinessList(JSONObject data) {

		logger.info("请求参数：" + data.toString());
		HashMap<String, Object> res = new HashMap<>();
		MessageDataBean messageDataBean = new MessageDataBean();
		
		try {
			long start = System.currentTimeMillis();
			res = scanDiscountServiceI.getBusinessList(data);
			logger.info("返回数据" + res.toString());
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(res);
			logger.info("原生扫码优惠列表获取时间"+ (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取商户列表错误" + e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	
	
	}
	/*{
		code: "0"
		dimensionCode: "688991159010739785"
		msgCode: "387506"
		info: "短信验证获取成功！"
		}*/
	@POST
	@Path(value = "/getScanDiscount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getScanDiscount(JSONObject data) {
		logger.info("请求参数：" + data.toString());
		MessageDataBean messageDataBean = new MessageDataBean();
		long start = System.currentTimeMillis();
		try {
			messageDataBean = scanDiscountServiceI.getScanDiscount(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取商户列表错误" + e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info("扫码优惠详情数据获取时间"+ (System.currentTimeMillis() - start) + " ms");
		return messageDataBean.toJsonString();
	}

}
