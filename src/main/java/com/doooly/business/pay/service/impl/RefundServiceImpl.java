package com.doooly.business.pay.service.impl;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.entity.reachad.AdBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.bean.WxRefundParams;
import com.doooly.business.pay.service.AbstractRefundService;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.business.pay.utils.WxUtil;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdUser;

/**
 * 退款接口
 *
 * @author 2017-11-09 11:05:50 WANG
 *
 */
@Service
public class RefundServiceImpl extends AbstractRefundService {

	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private MallBusinessService mallBusinessService;

	@Override
	public PayMsg doRefund(OrderVo order, PayFlow payFlow, String refundFlowId) {
		logger.info("doRefund start . orderNumber = {}", order.getOrderNumber());
		try {
			if (PayFlowService.PAYTYPE_DOOOLY.equals(payFlow.getPayType())) {
				// 积分退款
				return dooolyRefund(order, payFlow);
			}else{
				return new PayMsg(PayMsg.failure_code, PayMsg.failure_mess);
			}
		} catch (Exception e) {
			logger.info("doRefund() e = {}", e);
		}
		return new PayMsg(PayMsg.failure_code, PayMsg.failure_mess);
	}

	private PayMsg wxAppRefund(OrderVo order, PayFlow payFlow) {
		WxRefundParams params = new WxRefundParams();
		params.setAppid(WxJsapiPayServiceImpl.WXJS_APPID);
		params.setMch_id(WxJsapiPayServiceImpl.WXJS_MCH_ID);
		params.setNonce_str(WxUtil.getNonceStr());
		// params.setSign(sign);
		params.setTransaction_id(payFlow.getTransNo());
		params.setOut_refund_no("");
		// params.setTotal_fee(total_fee);
		// params.setRefund_fee(refund_fee);
		// params.set
		return null;
	}

	/***
	 * 积分退款
	 *
	 * @param order
	 * @param payFlow
	 * @return
	 */
	public PayMsg dooolyRefund(OrderVo order, PayFlow payFlow) {

//		// 积分退款
//		AdUser adUser = adUserDao.getById(String.valueOf(order.getUserId()));
//		JSONObject params = new JSONObject();
//		params.put("orderNumber", order.getOrderNumber());
//		params.put("serialNumber", order.getOrderNumber());
//		params.put("cardNumber", adUser.getCardNumber());
//		params.put("integral", order.getTotalMount().toString());
//		params.put("type", "5");
//		params.put("businessId", WebService.BUSINESSID);
//		params.put("storesId", WebService.STOREID);
//		//订单子项
//		OrderItemVo item = order.getItems().get(0);
//		JSONObject jsonDetail = new JSONObject();
//		jsonDetail.put("code", item.getId());
//		jsonDetail.put("goods", item.getGoods() + item.getSku());
//		jsonDetail.put("number", item.getNumber());
//		jsonDetail.put("price", item.getPrice().toString());
//		jsonDetail.put("category", item.getCategoryId());
//		params.put("orderDetail", jsonDetail);
//		JSONObject ret = WebService.checkAddIntegralAuthorizationPort(params);
//		logger.info("ret = {}", ret);
//		if (ret != null) {
//			if (ret.getInteger("code") == 0) {
//				try {
//					// 积分退成功发送短信
//					OrderItemVo orderItem = order.getItems().get(0);
//					String mobiles = adUser.getTelephone();
//					String alidayuSmsCode = "SMS_109475271";
//					JSONObject paramSMSJSON = new JSONObject();
//					paramSMSJSON.put("product", orderItem.getGoods() + "-" + orderItem.getSku());
//					paramSMSJSON.put("integral", payFlow.getAmount().toString());
//					int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
//					logger.info("sendMsg orderNum = {},i = {}", order.getOrderNumber(), i);
//				} catch (Exception e) {
//					logger.error("sendMsg has an error. e = {}", e);
//				}
//				PayMsg payMsg = new PayMsg(PayMsg.success_code, "积分退款成功!", new HashMap<String, Object>());
//				payMsg.data.put("refund_id", "0");
//				return payMsg;
//			} else {
//				return new PayMsg(PayMsg.failure_code, ret.getString("info"));
//			}
//		} else {
//			return new PayMsg(PayMsg.failure_code, "退款出现异常!");
//		}

		String url = WebService.ADDINTEGRALAUTHORIZATION;
		// 积分退款
		AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
		AdUser adUser = adUserDao.getById(String.valueOf(order.getUserId()));
		JSONObject params = new JSONObject();
		params.put("orderNumber", order.getOrderNumber());
		params.put("serialNumber", order.getOrderNumber());
		params.put("cardNumber", adUser.getCardNumber());
		params.put("integral", order.getTotalMount().toString());
		params.put("orderType", "5");
		params.put("businessId", WebService.BUSINESSID);
		params.put("storesId", WebService.STOREID);
		params.put("username", WebService.USERNAME);
		params.put("password", WebService.PASSWORD);
		//订单子项
		OrderItemVo item = order.getItems().get(0);
		JSONObject jsonDetail = new JSONObject();
		jsonDetail.put("code", item.getId());
		jsonDetail.put("goods", item.getGoods() + item.getSku());
		jsonDetail.put("number", item.getNumber());
		jsonDetail.put("price", item.getPrice().toString());
		jsonDetail.put("category", item.getCategoryId());
		//jsonDetail.put("orderNumber", order.getOrderNumber());
		//jsonDetail.put("serialNumber", order.getOrderNumber());
		params.put("orderDetail", jsonDetail);

		String ret = HTTPSClientUtils.sendPostNew(params.toJSONString(), url);
		logger.info("result = {}",ret);
		if (ret != null) {
			JSONObject json = JSON.parseObject(ret);
			if (json.getInteger("code") == 0) {
				try {
					// 积分退成功发送短信
					OrderItemVo orderItem = order.getItems().get(0);
					String mobiles = adUser.getTelephone();
					String alidayuSmsCode = "SMS_109475271";
					JSONObject paramSMSJSON = new JSONObject();
					paramSMSJSON.put("product", orderItem.getGoods() + "-" + orderItem.getSku());
					paramSMSJSON.put("integral", payFlow.getAmount().toString());
					int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
					logger.info("sendMsg orderNum = {},i = {}", order.getOrderNumber(), i);
				} catch (Exception e) {
					logger.error("sendMsg has an error. e = {}", e);
				}
				PayMsg payMsg = new PayMsg(PayMsg.success_code, "积分退款成功!", new HashMap<String, Object>());
				payMsg.data.put("refund_id", "0");
				return payMsg;
			} else {
				return new PayMsg(PayMsg.failure_code, json.getString("info"));
			}
		} else {
			return new PayMsg(PayMsg.failure_code, "退款出现异常!");
		}

	}
}
