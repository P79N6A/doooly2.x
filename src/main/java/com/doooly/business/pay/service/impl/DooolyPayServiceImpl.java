package com.doooly.business.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.service.AbstractPaymentService;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 兜礼积分支付
 * 
 * @author 2017-10-21 18:30:11 WANG
 */
@Service
public class DooolyPayServiceImpl extends AbstractPaymentService {


	@Autowired
	private MallBusinessService mallBusinessService;

	@Override
	public String getPayType() {
		return PayFlowService.PAYTYPE_DOOOLY;
	}

	@Override
	protected PayMsg buildPayParams(List<OrderVo> orders, PayFlow flow, JSONObject json) {
		OrderVo order = orders.get(0);
		PayMsg msg = new PayMsg(MessageDataBean.success_code, MessageDataBean.success_mess,
				new HashMap<String, Object>());
		//积分充值话费收手续费
		BigDecimal payAmount = order.getTotalMount();
		if(order.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode() && order.getServiceCharge() != null){
			payAmount = payAmount.add(order.getServiceCharge());
		}
		msg.data.put("orderNumber", order.getOrderNumber());
		msg.data.put("productType", order.getProductType());
		msg.data.put("totalMount", payAmount.toString());
		msg.data.put("consigneeMobile", order.getConsigneeMobile());
		msg.data.put("payFlowId", flow.getId());
		AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
		msg.data.put("businessId", business.getBusinessId());
		msg.data.put("storeId", OrderService.STORESID);
		return msg;
	}

	@Override
	protected Map<String, Object> resolveAndVerifyResult(String retStr, String payType, String channel) {
		try {
			JSONObject json = JSONObject.parseObject(retStr);
			String code = json.getString("code");
			// 积分扣除成功
			if (PayMsg.success_code.equals(code)) {
				String orderNum = json.getString("orderNum");
				String payFlowId = json.getString("payFlowId");
				//修改订单金额加上手续费
				OrderVo order = orderService.getByOrderNum(orderNum);
				BigDecimal serviceCharge = order.getServiceCharge();
				if(OrderService.ProductType.MOBILE_RECHARGE.getCode() == order.getProductType() && serviceCharge != null){
					OrderVo o = new OrderVo();
					o.setOrderNumber(order.getOrderNumber());
					o.setTotalMount(order.getTotalMount().add(order.getServiceCharge()));
					o.setUpdateBy("resolveAndVerifyResult");
					int i = orderService.updateByNum(o);
					logger.info("updateByNum() i = {}",i);
				}
				// 返回解析结果数据
				List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderNum", orderNum);
				map.put("payFlowId", payFlowId);
				map.put("orders", orders);
				// 积分支付没有交易流水号
				map.put("transNo", "0");
                map.put("realPayType", "0");//兜礼积分支付方式
				return map;
			}
		}catch (Exception e){
			logger.error("e = {}",e);
		}
		return null;
	}

	@Override
	protected PayMsg queryPayResult(PayFlow flow) {
		// 积分没有查询接口,直接返回调用接口成功
		return new PayMsg(PayMsg.success_code, PayMsg.success_mess);
	}
}
