package com.doooly.business.pay.processor.productprocessor;

import java.util.Map;

import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.common.constants.ThirdPartySMSConstatns;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.ofpay.service.OfPayService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.service.OrderService.ProductType;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.utils.AESTool;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.dto.common.PayMsg;

/***
 * 卡密充值
 * 
 * @author 2017-10-11 17:34:21 WANG
 *
 */
@Component
public class CardPswProcessor implements ProductProcessor{
	
	protected Logger logger = LoggerFactory.getLogger(CardPswProcessor.class);
	private final String CP_AES_KEY = PropertiesConstants.dooolyBundle.getString("cp_aes_key");  
	@Autowired
	private OfPayService ofPayService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private AdUserServiceI adUserServiceI;

	@Override
	public int getProcessCode() {
		return ProductType.VIRTUAL_CARD.getCode();
	}



	@Override
	public PayMsg process(OrderVo order) {
		if(order != null && order.getItems() !=null){
			//订单充值
			OrderItemVo item = order.getItems().get(0);
			if (!OfPayService.OF_SUCCESS_CODE.equals(item.getRetState())) {
				Map<String, String> retMap = ofPayService.cardPswRecharge(order);
				//logger.error("process retMap = {},order = {}", retMap, order);
				boolean bool = true;//充值结果
				if (retMap != null && retMap.size() > 0) {
					// 保存充值结果
					int rows = updateOrderItemSuccesss(item, retMap);
					logger.info("updateOrderItem  rows = {}",  rows);
					// =================发送短信==================
					String cardpws = retMap.get("cardpws");
					String cardno = retMap.get("cardno");
					String retcode = retMap.get("retcode");
					if (OfPayService.OF_SUCCESS_CODE.equals(retcode)) {
						logger.info("updateOrderItem success. rows = {}",  rows);
						// ================发送卡号和密码短信息==================
						String orderDetail = item.getGoods() + item.getSku();
						if(!StringUtils.isEmpty(cardno) && !StringUtils.isEmpty(cardpws)){
							String mobiles = order.getConsigneeMobile();
							String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.send_card_template_code;
							JSONObject paramSMSJSON = new JSONObject();
							paramSMSJSON.put("product", orderDetail);
							//paramSMSJSON.put("cardNo", cardno);
							//paramSMSJSON.put("cardPsw", cardpws);
							int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
							logger.info("发送卡密短信. i = {}", i);
						}else{
							logger.info("发送短信失败.未获得卡密.");
						}
						// =================微信消息推送==================
						JSONObject data = new JSONObject();
						data.put("userId", order.getUserId());
						data.put("orderId", order.getId());
						data.put("goods", item.getGoods());
						data.put("sku", item.getSku());
						data.put("cardNo", cardno);
						data.put("cardPsw", cardpws);
						redisTemplate.convertAndSend("CARDPSW_CHANNEL", data.toString());
					}else{
						logger.error("mobileRecharge failed.retcode  = {}", retcode);
						bool = false;
					}
				} else {
					logger.error("mobileRecharge failed.retMap  = {}", retMap);
					bool = false;
				}
				// 如果卡密获取失败发送以下短信
				//【兜礼】尊敬的用户，您本次的话费充值/流量充值/都市旅游卡充值失败，积分会在两个工作日内退回，微信支付的退款事宜请联系兜礼客服热线4001582212咨询！
				if(!bool){
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
		}else{
			logger.error("FlowRechargeProcessor failed. order = {}",order);
		}
		return null;
	}
	
	/***
	 *  保存充值结果
	 * @param order
	 * @param retMap
	 * @return
	 */
	private int updateOrderItemSuccesss(OrderItemVo oldItem, Map<String, String> retMap) {
		try {
			String retcode = retMap.get("retcode");
			String game_state = retMap.get("game_state");
			String msg = "";
			if(OF_SUCCESS_CODE.equals(retcode)){
				msg = "卡密获取成功.";
				game_state = OF_SUCCESS_CODE;
			}else{
				game_state = "-1";
				msg = retMap.get("err_msg");
			}
			OrderItemVo newItem = new OrderItemVo();
			newItem.setId(oldItem.getId());
			newItem.setRetCode(retMap.get("retcode"));
			newItem.setRetMsg(msg);
			newItem.setRetState(game_state);
			newItem.setCardOid(retMap.get("cardid"));
			String cardpws = retMap.get("cardpws");
			if(null != cardpws){
				//卡密需要加密
				newItem.setCardPass(AESTool.Encrypt(cardpws, CP_AES_KEY));
			}
			newItem.setCardCode(retMap.get("cardno"));
			logger.info("item = {}",newItem);
			return orderService.updateOrderItem(newItem);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("updateOrderItemSuccesss e = {}", e);
		}
		return 0;
	}

}
