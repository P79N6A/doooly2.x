
package com.doooly.business.pay.processor.productprocessor;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.ofpay.OfUtil;
import com.doooly.business.ofpay.service.OfPayService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.service.OrderService.ProductType;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.service.RefundService;
import com.doooly.common.constants.ThirdPartySMSConstatns;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 话费充值活动
 *
 * @author 2017-10-11 17:25:09 WANG
 *
 */
@Component
public class MobileRechargeActivityProcessor implements ProductProcessor {

	protected Logger logger = LoggerFactory.getLogger(MobileRechargeActivityProcessor.class);

	@Autowired
	private OfPayService ofPayService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private AdUserServiceI adUserServiceI;

	@Override
	public int getProcessCode() {
		return ProductType.NEXUS_RECHARGE_ACTIVITY.getCode();	}

	@Override
	public PayMsg process(OrderVo order) {
		if (order != null && order.getItems() != null) {
			//订单充值
			OrderItemVo oldItem = order.getItems().get(0);
			if (!OfPayService.OF_SUCCESS_CODE.equals(oldItem.getRetState())) {
				Map<String, String> retMap = ofPayService.mobileRecharge(order);
				logger.error("process retMap = {},order = {}", retMap, order);
				//保存充值结果
				updateOrderItemSuccesss(oldItem, retMap);
				//充值失败调用退款接口
				boolean bool = false;
				if (retMap == null) {
					bool = true;
				}else{
					String retcode = retMap.get("retcode");
					bool = OfUtil.mobileCanRefund(retcode);
				}
				if(bool){
					//退款
					PayMsg payMsg = refundService.autoRefund(order.getUserId(), order.getOrderNumber());
					// 如果退款失败发送以下短信
					//【兜礼】尊敬的用户，您本次的话费充值/流量充值/都市旅游卡充值失败，积分会在两个工作日内退回，微信支付的退款事宜请联系兜礼客服热线4001582212咨询！
					if(!PayMsg.success_code.equals(payMsg.getCode())){
						AdUser user = adUserServiceI.getById(order.getUserId().intValue());
						String mobiles = user.getTelephone();
						String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.recharge_fail_template_code;
						JSONObject paramSMSJSON = new JSONObject();
						paramSMSJSON.put("productType", ProductType.getProductTypeName(order.getProductType()));
						paramSMSJSON.put("phone", "4001582212");
						int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
						logger.info("发送退款审批短信. i = {}", i);
					}
				}
			}
		}
		return null;
	}

	/***
	 *  保存充值结果
	 * @param retMap
	 * @return
	 */
	private int updateOrderItemSuccesss(OrderItemVo oldItem, Map<String, String> retMap) {
		try {
			String retcode = "-1";
			String game_state = "-1";
			String msg = "失败.";
			String cardid = "";
			if (retMap != null) {
				//充值平台有返回结果
				retcode = retMap.get("retcode");
				game_state = retMap.get("game_state");
				msg = retMap.get("err_msg");
				cardid = retMap.get("cardid");
				if (OF_SUCCESS_CODE.equals(retcode)) {
					if (RECHARGE_STATE_0.equals(game_state)) {
						msg = "充值中.";
					} else if (RECHARGE_STATE_1.equals(game_state)) {
						msg = "充值成功.";
					} else if (RECHARGE_STATE_9.equals(game_state)) {
						msg = "充值失败.";
					}
				}
			}
			OrderItemVo newItem = new OrderItemVo();
			newItem.setId(oldItem.getId());
			newItem.setRetCode(retcode);
			newItem.setRetMsg(msg);
			newItem.setRetState(game_state);
			newItem.setCardOid(cardid);
			newItem.setUpdateBy("MobileRechargeProcessor");
			newItem.setUpdateDate(new Date());
			return orderService.updateOrderItem(newItem);
		} catch (Exception e) {
			logger.error("updateOrderItem e = {}", e);
		}
		return 0;
	}

}
