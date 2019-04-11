package com.doooly.business.pay.processor.productprocessor;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.service.OrderService.ProductType;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.touristCard.datacontract.response.AccountRechargeResponse;
import com.doooly.business.touristCard.service.TouristCardService;
import com.doooly.common.constants.ThirdPartySMSConstatns;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.dao.reachad.TouristCardDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserBusinessExpansion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 旅游卡充值
 */
@Component
public class TouristCardRechargeProcessor implements ProductProcessor {

	protected Logger logger = LoggerFactory.getLogger(TouristCardRechargeProcessor.class);

	@Autowired
	private TouristCardService touristCardService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private TouristCardDao touristCardDao;
	@Autowired
	private AdUserServiceI adUserServiceI;

	@Override
	public int getProcessCode() {
		return ProductType.TOURIST_CARD_RECHARGE.getCode();
	}

	@Override
	public PayMsg process(OrderVo order) {
		if (order != null && order.getItems() != null) {
			//订单充值
			OrderItemVo oldItem = order.getItems().get(0);
			if ( !TouristCardService.SUCCESS_CODE.equals(oldItem.getRetState()) ) {
				AccountRechargeResponse accountRechargeResponse = touristCardService.accountRecharge(order);
				logger.info("sctcd accountRecharge order = {}", JSONObject.toJSONString(order));

				boolean rechargeSuccess = false;
				if (accountRechargeResponse == null) {
					logger.error("manual_intervention : accountRechargeResponse = {}", JSONObject.toJSONString(accountRechargeResponse));
					rechargeSuccess = true;
				} else{
					if ("200".equals(accountRechargeResponse.getCode())) {
						logger.info("sctcd accountRechargeResponse = {}", JSONObject.toJSONString(accountRechargeResponse));
						if ("00".equals(accountRechargeResponse.getOrderResult()) && "01".equals(accountRechargeResponse.getPayResult())) {
							rechargeSuccess = true;
							//更新第三方返回的充值结果，状态码、描述等信息
							updateOrderItemSuccesss(oldItem, accountRechargeResponse);
							// 如果旅游卡扩展表ad_user_business_expansion记录不存在，就插入一条记录
							addSctcdAccount(order);
						} else {
							//充值失败调用退款接口
							rechargeSuccess = false;
							//更新第三方返回的充值结果，状态码、描述等信息
							updateOrderItemSuccesss(oldItem, accountRechargeResponse);
						}
					} else {
						logger.error("manual_intervention : accountRechargeResponse = {}", JSONObject.toJSONString(accountRechargeResponse));
						rechargeSuccess = true;
					}
				}

				if( !rechargeSuccess ){
					//退款
					PayMsg payMsg = refundService.autoRefund(order.getUserId(), order.getOrderNumber(),null, null);
					// 如果退款失败发送以下短信
					//【兜礼】尊敬的用户，您本次的话费充值/流量充值/都市旅游卡充值失败，积分会在两个工作日内退回，微信支付的退款事宜请联系兜礼客服热线4001582212咨询！
					if(!PayMsg.success_code.equals(payMsg.getCode())){
						AdUser user = adUserServiceI.getById(order.getUserId().intValue());
						String mobiles = user.getTelephone();
						String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.recharge_fail_template_code;
						JSONObject paramSMSJSON = new JSONObject();
						paramSMSJSON.put("productType", OrderService.ProductType.getProductTypeName(order.getProductType()));
						paramSMSJSON.put("phone", "4001582212");
						int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
						logger.info("发送退款审批短信. i = {}", i);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 如果是在其他平台绑定的旅游卡，在充值成功后。
	 * 在ad_user_business_expansion插入一条记录，视为在兜礼平台绑卡成功。
	 */
	private void addSctcdAccount(OrderVo order) {
		AdUserBusinessExpansion adUserBusinessExpansion = new AdUserBusinessExpansion();
		adUserBusinessExpansion.setBusinessType("sctcd-account");
		adUserBusinessExpansion.setUserId(String.valueOf(order.getUserId()));
		adUserBusinessExpansion.setBusinessId(String.valueOf(order.getBussinessId()));
		adUserBusinessExpansion.setF1(order.getRemarks());
		adUserBusinessExpansion.setDelFlag("0");
		touristCardDao.addSctcdAccount(adUserBusinessExpansion);
	}

	/***
	 *  保存充值结果
	 */
	private int updateOrderItemSuccesss(OrderItemVo oldItem, AccountRechargeResponse accountRechargeResponse) {
		try {
			OrderItemVo newItem = new OrderItemVo();
			newItem.setId(oldItem.getId());
			newItem.setRetCode(accountRechargeResponse.getOrderResult());           //旅游卡充值平台-返回的业务结果(业务处理成功，不代表账户充值成功)
			newItem.setRetMsg(accountRechargeResponse.getResultDesc());             //旅游卡充值平台-返回的结果描述
			newItem.setRetState(accountRechargeResponse.getPayResult());            //旅游卡充值平台-返回的充值结果
			newItem.setCardOid(accountRechargeResponse.getSctcdRechargeOrderNum()); //旅游卡充值平台-返回的交易号
			newItem.setUpdateBy("TouristCardRechargeProcessor");
			newItem.setUpdateDate(new Date());
			return orderService.updateOrderItem(newItem);
		} catch (Exception e) {
			logger.error("updateOrderItem e = {}", e);
		}
		return 0;
	}

}
