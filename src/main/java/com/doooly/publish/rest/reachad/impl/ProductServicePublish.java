package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.product.entity.AdSelfProduct;
import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.business.product.service.ProductService;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.dao.reachad.AdNexusBindDao;
import com.doooly.dao.reachad.AdRechargeConfDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdRechargeConf;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
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

	private static final String MERCHANT_ID  = PropertiesConstants.dooolyBundle.getString("merchant_id");
	private static final String CMCC_TEL_ID  = PropertiesConstants.dooolyBundle.getString("cmcc_tel_product_id");
	private static final String CUCC_TEL_ID  = PropertiesConstants.dooolyBundle.getString("cucc_tel_product_id");
	private static final String CTC_TEL_ID  = PropertiesConstants.dooolyBundle.getString("ctc_tel_product_id");

	private static final String CMCC_FLOW_ID = PropertiesConstants.dooolyBundle.getString("cmcc_flow_product_id");
	private static final String CUCC_FLOW_ID = PropertiesConstants.dooolyBundle.getString("cucc_flow_product_id");
	private static final String CTC_FLOW_ID = PropertiesConstants.dooolyBundle.getString("ctc_flow_product_id");

	private static final String SCTCD_MERCHANT_ID = PropertiesConstants.sctcdBundle.getString("SCTCD_MERCHANT_ID");
	private static final String SCTCD_PRODUCT_ID = PropertiesConstants.sctcdBundle.getString("SCTCD_PRODUCT_ID");

	private static final String MOBIKE_PRODUCT_ID = PropertiesConstants.dooolyBundle.getString("mobike_product_id");
	private static final String MOBIKE_MERCHANT_ID = PropertiesConstants.dooolyBundle.getString("mobike_merchant_id");

	private static final String NEXUS_PRODUCT_ID = PropertiesConstants.dooolyBundle.getString("nexus_product_id");
	private static final String NEXUS_MERCHANT_ID = PropertiesConstants.dooolyBundle.getString("nexus_merchant_id");

	@Autowired
	private ProductService productService;
	@Autowired
	private AdRechargeConfDao adRechargeConfDao;
	@Autowired
	protected AdUserDao adUserDao;
	@Autowired
	protected AdNexusBindDao adNexusBindDao;



	/***
	 * 查询是否绑定纳客宝
	 * @param params
	 * @return
	 */
	@POST
	@Path(value = "/nexusBindQuery")
	@Produces(MediaType.APPLICATION_JSON)
	public String nexusBindQuery(JSONObject params) {
		long userId = params.getLong("userId");
		if (userId <= 0) {
			return new MessageDataBean(MessageDataBean.failure_code, "userId is null").toJsonString();
		}
		String bindId = adNexusBindDao.getBindId(userId);
		if(StringUtils.isEmpty(bindId)){
			return new MessageDataBean(MessageDataBean.success_code, "用户id未绑定纳客宝").toJsonString();
		}else {
			return new MessageDataBean(MessageDataBean.failure_code, "用户id已绑定纳客宝").toJsonString();
		}
	}

	/***
	 * 纳客宝绑定用户
	 * @param params
	 * @return
	 */
	@POST
	@Path(value = "/nexusBind")
	@Produces(MediaType.APPLICATION_JSON)
	public String nexusBind(JSONObject params) {
		long userId = params.getLong("userId");
		if (userId <= 0) {
			return new MessageDataBean(MessageDataBean.failure_code, "userId is null").toJsonString();
		}
		String bindId = adNexusBindDao.getBindId(userId);
		if (!StringUtils.isEmpty(bindId)) {
			//用户已绑定bindId
			return new MessageDataBean(MessageDataBean.already_used_code, "用户id已绑定纳客宝").toJsonString();
		}
		String nexusId = params.getString("bindId");
		if (StringUtils.isEmpty(nexusId)) {
			return new MessageDataBean(MessageDataBean.failure_code, "bindId is null").toJsonString();
		}
		String bid = adNexusBindDao.getByBindId(nexusId);
		if (StringUtils.isEmpty(nexusId)) {
			//bindId已经其他用户被绑定
			return new MessageDataBean("1008", "纳客宝已经被绑定!").toJsonString();
		}
		int i = adNexusBindDao.insert(String.valueOf(userId), nexusId);
		if (i > 0) {
			return new MessageDataBean(MessageDataBean.success_code, "success").toJsonString();
		} else {
			return new MessageDataBean(MessageDataBean.failure_code, "failed").toJsonString();
		}
	}


	/***
	 * 纳客宝商品信息
	 * @param params
	 * @return
	 */
	@POST
	@Path(value = "/nexusProductInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public String nexusProductInfo(JSONObject params) {
		AdSelfProduct mobikePro = productService.getProductSku(Integer.valueOf(NEXUS_MERCHANT_ID), Integer.valueOf(NEXUS_PRODUCT_ID), null);
		JSONObject retJson = new JSONObject(true);
		List<AdSelfProductSku> mobileList = mobikePro.getProductSku();
		String default_sku_id = "";
		JSONArray array = new JSONArray();
		for (AdSelfProductSku adSelfProductSku : mobileList) {
			JSONObject sku = new JSONObject();
			sku.put("id", adSelfProductSku.getId());
			sku.put("price", adSelfProductSku.getSellPrice());
			sku.put("specification", adSelfProductSku.getSpecification());
			//默认值
			if (adSelfProductSku.getSpecification().equals("1000")) {
				default_sku_id = adSelfProductSku.getId();
			}
			array.add(sku);
		}
		retJson.put("merchant_id", NEXUS_MERCHANT_ID);
		retJson.put("product_id", mobikePro.getId());
		retJson.put("default_sku_id", default_sku_id);
		retJson.put("sku_list", array);
		return retJson.toJSONString();
	}


	/***
	 * 摩拜
	 * @param params
	 * @return
	 */
	@POST
	@Path(value = "/mobikeProductInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public String mobikeProductInfo(JSONObject params) {
		AdSelfProduct  mobikePro = productService.getProductSku(Integer.valueOf(MOBIKE_MERCHANT_ID), Integer.valueOf(MOBIKE_PRODUCT_ID), null);
		JSONObject retJson = new JSONObject(true);
		List<AdSelfProductSku> mobileList = mobikePro.getProductSku();
		String default_sku_id = "";
		JSONArray array = new JSONArray();
		for (AdSelfProductSku adSelfProductSku : mobileList) {
			JSONObject sku = new JSONObject();
			sku.put("id", adSelfProductSku.getId());
			sku.put("price", adSelfProductSku.getSellPrice());
			sku.put("specification", adSelfProductSku.getSpecification());
			//默认值
			if(adSelfProductSku.getSpecification().equals("1元")){
				default_sku_id = adSelfProductSku.getId();
			}
			array.add(sku);
		}
		retJson.put("mobike_merchant_id", MOBIKE_MERCHANT_ID);
		retJson.put("mobike_product_id", mobikePro.getId());
		retJson.put("mobike_default_sku_id", default_sku_id);
		retJson.put("mobike_sku_list", array);
		String userId = params.getString("userId");
		if(userId != null) {
			AdUser user = adUserDao.getById(userId);
			retJson.put("integral", user.getIntegral());
		}else{
			retJson.put("integral", "0");
		}
		return retJson.toJSONString();
	}


		/***
         * 虚拟产品信息
         * @param request
         * @return
         */
	@POST
	@Path(value = "/virProducts")
	@Produces(MediaType.APPLICATION_JSON)
	public String virProducts(JSONObject params) {

		try {
			long s = System.currentTimeMillis();
			JSONObject retJson = new JSONObject(true);
			// ==================== 话费商品信息 ====================================
			AdSelfProduct  mobilePro = productService.getProductSku(Integer.valueOf(MERCHANT_ID), Integer.valueOf(CMCC_TEL_ID), null);
			List<AdSelfProductSku> mobileList = mobilePro.getProductSku();
			String default_sku_id = "";
			JSONArray array = new JSONArray();
			for (AdSelfProductSku adSelfProductSku : mobileList) {
                JSONObject sku = new JSONObject();
                sku.put("id", adSelfProductSku.getId());
                sku.put("price", adSelfProductSku.getSellPrice());
                sku.put("specification", adSelfProductSku.getSpecification());
                //默认值
                if(adSelfProductSku.getSpecification().equals("50元")){
                    default_sku_id = adSelfProductSku.getId();
                }
                array.add(sku);
            }
			retJson.put("cmcc_tel_merchant_id", MERCHANT_ID);
			retJson.put("cmcc_tel_product_id", mobilePro.getId());
			retJson.put("cmcc_tel_default_sku_id", default_sku_id);
			retJson.put("cmcc_tel_list", array);

			mobilePro = productService.getProductSku(Integer.valueOf(MERCHANT_ID), Integer.valueOf(CUCC_TEL_ID), null);
			mobileList = mobilePro.getProductSku();
			array = new JSONArray();
			for (AdSelfProductSku adSelfProductSku : mobileList) {
                JSONObject sku = new JSONObject();
                sku.put("id", adSelfProductSku.getId());
                sku.put("price", adSelfProductSku.getSellPrice());
                sku.put("specification", adSelfProductSku.getSpecification());
                //默认值
                if(adSelfProductSku.getSpecification().equals("50元")){
                    default_sku_id = adSelfProductSku.getId();
                }
                array.add(sku);
            }
			retJson.put("cucc_tel_merchant_id", MERCHANT_ID);
			retJson.put("cucc_tel_product_id", mobilePro.getId());
			retJson.put("cucc_tel_default_sku_id", default_sku_id);
			retJson.put("cucc_tel_list", array);

			mobilePro = productService.getProductSku(Integer.valueOf(MERCHANT_ID), Integer.valueOf(CTC_TEL_ID), null);
			mobileList = mobilePro.getProductSku();
			array = new JSONArray();
			for (AdSelfProductSku adSelfProductSku : mobileList) {
                JSONObject sku = new JSONObject();
                sku.put("id", adSelfProductSku.getId());
                sku.put("price", adSelfProductSku.getSellPrice());
                sku.put("specification", adSelfProductSku.getSpecification());
                //默认值
                if(adSelfProductSku.getSpecification().equals("50元")){
                    default_sku_id = adSelfProductSku.getId();
                }
                array.add(sku);
            }
			retJson.put("ctc_tel_merchant_id", MERCHANT_ID);
			retJson.put("ctc_tel_product_id", mobilePro.getId());
			retJson.put("ctc_tel_default_sku_id", default_sku_id);
			retJson.put("ctc_tel_list", array);

			logger.info("virProducts() cost1={}", System.currentTimeMillis() - s);

			// ==================== 流量商品信息 ====================================
			//mobile移动 unicom联通 telecom电信
			AdSelfProduct flowPro = productService.getProductSku(Integer.valueOf(MERCHANT_ID), Integer.valueOf(CMCC_FLOW_ID), null);
			array = new JSONArray();
			for (AdSelfProductSku adSelfProductSku : flowPro.getProductSku()) {
                String[] str = adSelfProductSku.getSpecification().split("/");
                JSONObject sku = new JSONObject();
                sku.put("id", adSelfProductSku.getId());
                sku.put("price", adSelfProductSku.getSellPrice());
                sku.put("specification", str[1]);
                //默认值
                if (str[1].equals("30M")) {
                    default_sku_id = adSelfProductSku.getId();
                }
                array.add(sku);
            }
			retJson.put("cmcc_flow_merchant_id", MERCHANT_ID);
			retJson.put("cmcc_flow_product_id", flowPro.getId());
			retJson.put("cmcc_flow_default_sku_id", default_sku_id);
			retJson.put("cmcc_flow_list", array);

			//联通
			flowPro = productService.getProductSku(Integer.valueOf(MERCHANT_ID), Integer.valueOf(CUCC_FLOW_ID), null);
			array = new JSONArray();
			for (AdSelfProductSku adSelfProductSku : flowPro.getProductSku()) {
                String[] str = adSelfProductSku.getSpecification().split("/");
                JSONObject sku = new JSONObject();
                sku.put("id", adSelfProductSku.getId());
                sku.put("price", adSelfProductSku.getSellPrice());
                sku.put("specification", str[1]);
                //默认值
                if (str[1].equals("30M")) {
                    default_sku_id = adSelfProductSku.getId();
                }
                array.add(sku);
            }
			retJson.put("cucc_flow_merchant_id", MERCHANT_ID);
			retJson.put("cucc_flow_product_id", flowPro.getId());
			retJson.put("cucc_flow_default_sku_id", default_sku_id);
			retJson.put("cucc_flow_list", array);

			//电信
			flowPro = productService.getProductSku(Integer.valueOf(MERCHANT_ID), Integer.valueOf(CTC_FLOW_ID), null);
			array = new JSONArray();
			for (AdSelfProductSku adSelfProductSku : flowPro.getProductSku()) {
                String[] str = adSelfProductSku.getSpecification().split("/");
                JSONObject sku = new JSONObject();
                sku.put("id", adSelfProductSku.getId());
                sku.put("price", adSelfProductSku.getSellPrice());
                sku.put("specification", str[1]);
                //默认值
                if (str[1].equals("30M")) {
                    default_sku_id = adSelfProductSku.getId();
                }
                array.add(sku);
            }
			retJson.put("ctc_flow_merchant_id", MERCHANT_ID);
			retJson.put("ctc_flow_product_id", flowPro.getId());
			retJson.put("ctc_flow_default_sku_id", default_sku_id);
			retJson.put("ctc_flow_list", array);

			logger.info("virProducts() cost2={}", System.currentTimeMillis() - s);

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
			logger.info("virProducts() cost2={}", System.currentTimeMillis() - s);
			//限额和手续费百分比
			String groupId = params.getString("groupId");
			AdRechargeConf conf = adRechargeConfDao.getRechargeConf(groupId);
			logger.info("conf={}", conf);
			if (conf != null) {
                //默认配置限额和手续费
                retJson.put("daily_limit", conf.getMonthLimit()); //限额1000
                retJson.put("charges", conf.getCharges()); //折扣
                retJson.put("discounts_month_limit", conf.getDiscountsMonthLimit());//免手续费金额
                //活动时间内配置
                if(conf.getDiscountsStartDate() != null && conf.getDiscountsStartDate() != null) {
                    long t = System.currentTimeMillis();
                    if (conf.getDiscountsStartDate().getTime() <= t && t <= conf.getDiscountsEndDate().getTime()) {
                        retJson.put("cmccCharges", conf.getCmccCharges());
                        retJson.put("chuCharges", conf.getChuCharges());
                        retJson.put("chaCharges", conf.getChaCharges());
                        retJson.put("discountsStartDate", DateUtils.formatDate(conf.getDiscountsStartDate(), "yyyy.MM.dd"));
                        retJson.put("discountsEndDate", DateUtils.formatDate(conf.getDiscountsEndDate(), "yyyy.MM.dd"));
                    }
                }
            }
			logger.info("virProducts() cost3={}", System.currentTimeMillis() - s);
			return retJson.toJSONString();
		} catch (Exception e) {
			logger.info("virProducts() Exception={}", e);
			e.printStackTrace();
		}
		return null;
	}
	
}
