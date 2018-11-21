package com.doooly.publish.rest.life.impl;

import java.util.Date;
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
import com.doooly.business.product.entity.AdGroupSelfProductPrice;
import com.doooly.business.product.service.ProductService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.SelfProductRestServiceI;

/**
 * 自营商品 REST Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年9月25日
 */
@Component
@Path("/selfProduct")
public class SelfProductRestService implements SelfProductRestServiceI {

	private static Logger logger = Logger.getLogger(SelfProductRestService.class);

	@Autowired
	private ProductService productService;

	@POST
	@Path(value = "/list")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getSelfProductList(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			int currentPage = obj.getInteger("currentPage");// 当前页
			int pageSize = obj.getInteger("pageSize");// 每页显示条数
			String userId = obj.getString("userId");// 会员id
			HashMap<String, Object> map = productService.getSelfProductList(currentPage, pageSize, userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取首页 “卡券购买”列表异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/detail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getSelfProductDetail(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String productId = obj.getString("productId");
			String userId = obj.getString("userId");
			HashMap<String, Object> map = productService.getSelfProductDetail(productId, userId);
			messageDataBean.setCode((String)map.get("code"));
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取卡券商品详情页信息异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	@POST
	@Path(value = "/getSelfProductByName")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getSelfProductByName(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String activityName = obj.getString("activityName");
			AdGroupSelfProductPrice adGroupSelfProductPrice = productService.getSelfProductSkuListByName(activityName);
			Date now = new Date();
			if(now.compareTo(adGroupSelfProductPrice.getSpecialStartDate()) < 0) {
				adGroupSelfProductPrice.setIsStart("1");
			} else if(now.compareTo(adGroupSelfProductPrice.getSpecialStartDate()) >= 0 && 
					now.compareTo(adGroupSelfProductPrice.getSpecialEndDate()) <= 0) {
				adGroupSelfProductPrice.setIsStart("2");
			} else if(now.compareTo(adGroupSelfProductPrice.getSpecialEndDate()) > 0){
				adGroupSelfProductPrice.setIsStart("3");
			}
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("adGroupSelfProductPrice", adGroupSelfProductPrice);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取卡券商品详情页信息异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
