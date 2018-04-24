package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.product.entity.AdSelfProduct;
import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.business.product.service.ProductService;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.entity.reachad.AdGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 欧飞接口
 * 
 * 2017-11-03 15:21:18 WANG
 *
 */
@Component
@Path("/productService")
public class ProductServicePublish {
	protected Logger logger = LoggerFactory.getLogger(ProductServicePublish.class);
	
	private static final String MOBILE_ID  = PropertiesConstants.dooolyBundle.getString("mobile_product_id");
	private static final String FLOW_ID  = PropertiesConstants.dooolyBundle.getString("flow_product_id");
	private static final String MERCHANT_ID  = PropertiesConstants.dooolyBundle.getString("merchant_id");

	private static final String SCTCD_MERCHANT_ID = PropertiesConstants.sctcdBundle.getString("SCTCD_MERCHANT_ID");
	private static final String SCTCD_PRODUCT_ID = PropertiesConstants.sctcdBundle.getString("SCTCD_PRODUCT_ID");

	@Autowired
	private ProductService productService;
	@Autowired
	private AdGroupDao adGroupDao;


	/***
	 * 虚拟产品信息
	 * @param request
	 * @return
	 */
	@POST
	@Path(value = "/virProducts")
	@Produces(MediaType.APPLICATION_JSON)
	public String virProducts(JSONObject params) {
		logger.info("virProducts start.MOBILE_ID = {},FLOW_ID={},MERCHANT_ID={}", MOBILE_ID, FLOW_ID, MERCHANT_ID);
		JSONObject retJson = new JSONObject(true);

		// ==================== 话费商品信息 ====================
		AdSelfProduct  mobilePro = productService.getProductSku(Integer.valueOf(MERCHANT_ID), Integer.valueOf(MOBILE_ID), null);
		retJson.put("mobile_merchant_id", MERCHANT_ID);
		retJson.put("mobile_product_id", mobilePro.getId());
		List<AdSelfProductSku> mobileList = mobilePro.getProductSku();
		String default_sku_id = "";
		JSONArray array = new JSONArray();
		for (AdSelfProductSku adSelfProductSku : mobileList) {
			JSONObject sku = new JSONObject();
			sku.put("id", adSelfProductSku.getId());
			sku.put("price", adSelfProductSku.getSpecification());
			sku.put("specification", adSelfProductSku.getSpecification());
			//默认值
			if(adSelfProductSku.getSpecification().equals("50元")){
				default_sku_id = adSelfProductSku.getId();
			}
			array.add(sku);
		}
		retJson.put("mobile_list", array);
		retJson.put("mobile_default_product_id", mobilePro.getId());
		retJson.put("mobile_default_sku_id", default_sku_id);

		// ==================== 流量商品信息 ====================
		AdSelfProduct  flowPro = productService.getProductSku(Integer.valueOf(MERCHANT_ID), Integer.valueOf(FLOW_ID), null);
		retJson.put("flow_merchant_id", MERCHANT_ID);
		retJson.put("flow_product_id", flowPro.getId());
		List<AdSelfProductSku> flowList = flowPro.getProductSku();
		array = new JSONArray();
		for (AdSelfProductSku adSelfProductSku : flowList) {
			String[] str = adSelfProductSku.getSpecification().split("/");
			JSONObject sku = new JSONObject();
			sku.put("id", adSelfProductSku.getId());
			sku.put("price", str[0]);
			sku.put("specification", str[1]);
			//默认值
			if(str[1].equals("30M")){
				default_sku_id = adSelfProductSku.getId();
			}
			array.add(sku);
		}
		retJson.put("flow_list", array);
		retJson.put("flow_default_product_id", flowPro.getId());
		retJson.put("flow_default_sku_id", default_sku_id);

		// ==================== 旅游卡商品信息 ====================
		AdSelfProduct  sctcdPro = productService.getProductSku(Integer.valueOf(SCTCD_MERCHANT_ID), Integer.valueOf(SCTCD_PRODUCT_ID), null);
		retJson.put("sctcd_merchant_id", SCTCD_MERCHANT_ID);
		retJson.put("sctcd_product_id", sctcdPro.getId());
		List<AdSelfProductSku> sctcdList = sctcdPro.getProductSku();
		array = new JSONArray();
		for (AdSelfProductSku adSelfProductSku : sctcdList) {
			JSONObject sku = new JSONObject();
			sku.put("id", adSelfProductSku.getId());
			sku.put("price", adSelfProductSku.getSpecification());
			sku.put("specification", adSelfProductSku.getSpecification());
			//默认值
			if(adSelfProductSku.getSpecification().equals("50元")){
				default_sku_id = adSelfProductSku.getId();
			}
			array.add(sku);
		}
		retJson.put("sctcd_list", array);
		retJson.put("sctcd_default_product_id", flowPro.getId());
		retJson.put("sctcd_default_sku_id", default_sku_id);

		//限额和手续费百分比
		String groupId  = params.getString("groupId");
		AdGroup adGroup = adGroupDao.findGroupByID(groupId);
		logger.info("getDailyLimit={},getCharges={}",adGroup.getDailyLimit(),adGroup.getCharges());
		retJson.put("daily_limit", adGroup.getDailyLimit());
		retJson.put("charges", adGroup.getCharges());
		return retJson.toJSONString();
	}
	
}
