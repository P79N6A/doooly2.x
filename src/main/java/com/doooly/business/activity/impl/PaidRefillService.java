package com.doooly.business.activity.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.PaidRefillServiceI;
import com.doooly.business.common.service.ActivityCodeImageServiceI;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.product.entity.AdSelfProduct;
import com.doooly.business.product.service.ProductService;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.WechatUtil;
import com.doooly.common.constants.ConstantsV2.ActivityCode;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdRechargeRecordDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.OrderMsg;
import com.doooly.entity.reachad.AdRechargeRecord;
import com.doooly.entity.reachad.AdUser;


@Service
public class PaidRefillService implements PaidRefillServiceI {
	
	private static Logger logger = Logger.getLogger(PaidRefillService.class);
	private static final String PRODUCT_ID  = PropertiesConstants.dooolyBundle.getString("paid_refill_product_id");
	private static final String SKU_ID  = PropertiesConstants.dooolyBundle.getString("paid_refill_sku_id");
	private static final String WUGANG_PRODUCT_ID  = PropertiesConstants.dooolyBundle.getString("paid_refill_wugang_product_id");
	private static final String WUGANG_SKU_ID  = PropertiesConstants.dooolyBundle.getString("paid_refill_wugang_sku_id");
	@Autowired
	private OrderService orderService;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdRechargeRecordDao adRechargeRecordDao;
	@Autowired
	private ProductService productService;
	@Autowired
	private ActivityCodeImageServiceI activityCodeImageServiceI;
	@Override
	public MessageDataBean getPaidRefill(JSONObject json) {
		// TODO Auto-generated method stub
		String userId = json.getString("userId");//子userid
        String openId = json.getString("openId");//子Openid
        String sourceUserId = json.getString("sourceUserId");//父userid
        String sourceOpenId = json.getString("sourceOpenId");//父Openid
        String channel = json.getString("channel");//channel
        String activityParam = json.getString("activityParam");//activityParam
		//生成一条record记录,并且生成订单
		//创建订单
		AdRechargeRecord rechargeRecord = adRechargeRecordDao.getLastRecord(userId);
		OrderMsg createOrder = new OrderMsg();
		if (rechargeRecord == null ||rechargeRecord.getDelFlag().equals("1") ) {
			AdUser adUser = adUserDao.getById(Integer.valueOf(userId));
			JSONObject order = new JSONObject();
			order.put("userId", userId);
			order.put("groupId", adUser.getGroupNum());
			order.put("consigneeName", null);
			order.put("consigneeMobile", adUser.getTelephone());
			order.put("consigneeAddr", null);
			order.put("productType", 7);
			order.put("supportPayType", 2);
			
			List<JSONObject> productSkus = new ArrayList<JSONObject>();
			JSONObject productSku = new JSONObject();
			AdSelfProduct product = null;
			if (channel.equals("wugang")) {
				productSku.put("productId", WUGANG_PRODUCT_ID);
				productSku.put("skuId", WUGANG_SKU_ID);
				product = productService.getProduct(Integer.valueOf(WUGANG_PRODUCT_ID));
			} else {
				productSku.put("productId", PRODUCT_ID);
				productSku.put("skuId", SKU_ID);
				product = productService.getProduct(Integer.valueOf(PRODUCT_ID));
			}
			productSku.put("buyNum", Integer.valueOf(1));
			productSkus.add(productSku);
			List<JSONObject> merchantProducts = new ArrayList<JSONObject>();
			JSONObject merchantProduct = new JSONObject();
			merchantProduct.put("merchantId", product.getBusinessId());
			merchantProduct.put("remarks", "话费充值");
			merchantProduct.put("productSku", productSkus);
			merchantProducts.add(merchantProduct);
			order.put("merchantProduct", merchantProducts);
			createOrder = orderService.createOrder(order);
			if (createOrder.getCode().equals(OrderMsg.success_code)) {
				//生成一个记录
				AdRechargeRecord adRechargeRecord = new AdRechargeRecord(userId,
						createOrder.getData().get("orderNum").toString(),
						0,openId,sourceOpenId,sourceUserId,channel,activityParam);
				adRechargeRecordDao.insert(adRechargeRecord);
				logger.error("话费充值活动立即参与,记录已生成,用户id为"+userId);
			}
		}else {
			//如有,返回标示,请用户确定是否取消订单,此时是否需要配置数据以供取消订单使用
			logger.error("话费充值活动立即参与,已参与过,用户id为"+userId);
			createOrder.setCode(ActivityCode.HAD_ALREADY.getCode()+"");
			createOrder.setMess(ActivityCode.HAD_ALREADY.getMsg());
		}
		return createOrder;
	}

	@Override
	public MessageDataBean getIsHadDone(String userId) {
		// TODO Auto-generated method stub
		//根据userId判断ad_recharge_record表中是否有数据,取最新的一条即可
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		AdRechargeRecord adRechargeRecord = adRechargeRecordDao.getLastRecord(userId);
		if (adRechargeRecord == null||adRechargeRecord.getDelFlag().equals("1") ) {
			//若无,返回成功
			messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
			messageDataBean.setMess(SystemCode.SUCCESS.getMsg());
		}else {
			//如有,返回标示,请用户确定是否取消订单,此时是否需要配置数据以供取消订单使用
			if (adRechargeRecord.getState().equals("0")) {
				data.put("orderNumber", adRechargeRecord.getOrderNumber());
				data.put("userId", adRechargeRecord.getUserId());
				messageDataBean.setCode(ActivityCode.HAD_ALREADY.getCode()+"");
				messageDataBean.setMess(ActivityCode.HAD_ALREADY.getMsg());
				messageDataBean.setData(data);
			}else if (adRechargeRecord.getState().equals("1")) {
				messageDataBean.setCode("11005");
				messageDataBean.setMess("已经下过订单");
			}
		}
		logger.error("话费充值活动判断是否参与过,返回结果"+messageDataBean.toJsonString());
		return messageDataBean;
	}

	@Override
	public MessageDataBean getQRCode(String openId,String channel,String activityId) {
		// TODO Auto-generated method stub
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		JSONObject token = WechatUtil.getAccessTokenTicketRedisByChannel(channel);
		String accessToken =token.getString("accessToken");
		String qrCodeUrl = activityCodeImageServiceI.getQRCodeUrl(accessToken, openId,channel,activityId);
		if (StringUtils.isNoneBlank(qrCodeUrl)) {
			data.put("url", qrCodeUrl);
			messageDataBean.setData(data);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
		}else {
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode()+"");
		}
		logger.error("话费充值活动二维码落地页获取二维码参数,返回结果"+messageDataBean.toJsonString());
		return messageDataBean;
	}
	
	

}
