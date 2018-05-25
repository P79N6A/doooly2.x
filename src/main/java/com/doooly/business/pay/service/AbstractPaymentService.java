package com.doooly.business.pay.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.common.context.SpringContextHolder;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdRechargeRecordDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.OrderMsg;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdRechargeRecord;
import com.doooly.entity.reachad.AdUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.service.OrderService.OrderStatus;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.processor.afterpayprocessor.AfterPayProcessor;
import com.doooly.business.pay.processor.afterpayprocessor.AfterPayProcessorFactory;
import com.doooly.business.pay.processor.productprocessor.ProductProcessor;
import com.doooly.business.pay.processor.productprocessor.ProductProcessorFactory;
import com.doooly.dto.common.PayMsg;
import org.springframework.util.StringUtils;

/**
 * 支付抽象类,完成主要支付工作<br>
 * 
 * @author 2017-10-21 16:19:23 WANG
 */
public abstract class AbstractPaymentService  implements PaymentService{
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();


	
	@Autowired
	protected OrderService orderService;
	@Autowired
	protected PayFlowService payFlowService;
	@Autowired
	protected AdUserDao adUserDao;
	@Autowired
	protected AdGroupDao adGroupDao;
	@Autowired
	protected AdRechargeRecordDao adRechargeRecordDao;

	protected abstract PayMsg buildPayParams(List<OrderVo> orders,PayFlow flow,JSONObject json) ;
	protected abstract Map<String,Object> resolveAndVerifyResult(String retStr, String payType,String channel) ;
	protected abstract PayMsg queryPayResult(PayFlow flow) ;

	/***
	 * 1.支付前的状态校验<br>
	 * 2.调用子类buildPayParams()返回支付参数
	 */
	public PayMsg prePay(JSONObject json) {
		String payType = this.getPayType();
		logger.info("prePay()  payType={},json = {}", payType, json);
		String orderNum = json.getString("orderNum");
		String userId = json.getString("userId");
		String channel = json.getString("channel");
		// 校验订单
		List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
		if (CollectionUtils.isEmpty(orders)) {
			return new PayMsg(PayMsg.failure_code, "没有找到订单");
		}
		// 订单状态
		OrderVo order = orders.get(0);
		Long oid = checkOrderStatus(order);
		if (oid != null) {
			return new PayMsg(PayMsg.failure_code, "无效的订单状态");
		}
		// 是否重复支付
		PayFlow paidRecord = payFlowService.getByOrderNum(orderNum, payType,null);
		if (paidRecord != null && PayFlowService.PAYMENT_SUCCESS.equals(paidRecord.getPayStatus())) {
			return new PayMsg(PayMsg.failure_code, "请勿重复支付");
		}
		//校验是否可以支付
		PayMsg msg = canPay(order);
		if(msg != null){
			return msg;
		}
		// 保存支付记录
		PayFlow flow = savePayFlow(orderNum, userId, payType,paidRecord,order,channel);
		if (flow == null) {
			return new PayMsg(PayMsg.failure_code, "保存支付记录失败.");
		}
		// 调用子类返回支付参数
		return buildPayParams(orders, flow, json);
	}



	/**
	 * 处理支付平台回调通知
	 * 
	 * 1.调用子类resolveAndVerifyResult()解析校验检查支付结果<br>
	 * 2.修改支付和订单状态<br>
	 * 3.调用处理器,充值,卡密,订单同步等操作.<br>
	 */
	public PayMsg handlePayResult(String retStr, String channel) {
		String payType = this.getPayType();
		logger.info("handlePayResult() payType = {},retStr = {},channel = {}", payType, retStr, channel);
		Map<String, Object> resultMap = resolveAndVerifyResult(retStr, payType, channel);
		try {
			if (resultMap != null) {
				logger.info("handlePayResult() 支付结果验证通过.payType = {}", payType);
				String payFlowId = (String) resultMap.get("payFlowId");
				String orderNum = (String) resultMap.get("orderNum");
				String transNo = (String) resultMap.get("transNo");
				List<OrderVo> orders = (List<OrderVo>) resultMap.get("orders");
				PayFlow payFlow = payFlowService.getById(payFlowId);
				long s = System.currentTimeMillis();
				logger.info("handlePayResult() s = {},payFlow = {}",s,payFlow);
				// 这里需要加分布式锁控制和事务处理
				// 校验是否已经处理过通知结果
				if (!PayFlowService.PAYMENT_SUCCESS.equals(payFlow.getPayStatus())) {
					logger.info("handlePayResult() 开始修改状态. payFlowId = {},orderNum={}", payFlowId, orderNum);
					//修改支付状态
					long s1 = System.currentTimeMillis();
					logger.info("updatePaySuccess ===>start = {}",s1);
					int payStatus = payFlowService.updatePaySuccess(payFlowId, transNo);
					if(payStatus == 0){
						return  new PayMsg(PayMsg.failure_code, "修改支付记录失败.");
					}
					logger.info("updatePaySuccess end.cost = {},payStatus = {}", System.currentTimeMillis() - s1, payStatus);
					// 修改订单状态
					long s2 = System.currentTimeMillis();
					logger.info("updateOrderSuccess ===>start = {}",s2);
					int orderStaus = orderService.updateOrderSuccess(orders, payType);
					if (orderStaus == 0) {
						logger.info("handlePayResult() orderStaus = {}", orderStaus);
						return new PayMsg(PayMsg.failure_code, "修改订单状态失败.");
					}
					logger.info("updateOrderSuccess end.cost = {},orderStaus = {}", System.currentTimeMillis() - s2, orderStaus);
					//这里要重新获取修改后的数据
					long s3 = System.currentTimeMillis();
					logger.info("doProcessor ===>start = {}",s3);
					PayFlow flow = payFlowService.getById(payFlowId);
					List<OrderVo> ods = orderService.getByOrdersNum(orderNum);
					doProcessor(ods, flow);
					logger.info("doProcessor end.cost = {}", System.currentTimeMillis() - s3);

				}
				logger.info("handlePayResult() cost = {},payFlow = {}", System.currentTimeMillis() - s,payFlow);
				return new PayMsg(PayMsg.success_code, PayMsg.success_mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("handlePayResult() e = {} ,retStr = {}", e, retStr);
		}
		return new PayMsg(PayMsg.failure_code, PayMsg.failure_mess);
	}

	/**
	 * 1.先从支付记录表中查询结果.<br>
	 * 如果支付记录不是成功状态, 调用支付平台的查询API查询结果.
	 */
	public PayMsg getPayResult(String orderNum) {
		String payType = this.getPayType();
		logger.info("getPayResult() orderNum = {},payType={}", orderNum, payType);
		PayFlow flow = payFlowService.getByOrderNum(orderNum, payType,null);
		if(flow == null){
			return new PayMsg(PayMsg.failure_code, "未找到支付记录.");
		}
		PayMsg payMsg = null;
		if (PayFlowService.PAYMENT_SUCCESS.equals(flow.getPayStatus())) {
			//得到支付平台通知并已经处理过支付结果, 直接返回结果
			payMsg = new PayMsg(PayMsg.success_code, PayMsg.success_code);
		}else{
			//调用支付平台查询API,查询结果并处理支付记录
			payMsg = queryPayResult(flow);
		}
		// 跳转支付结果页面需要数据
		if (payMsg != null && PayMsg.success_code.equals(payMsg.getCode())) {
			List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
			PayFlow afterQueryFlow = payFlowService.getByOrderNum(orderNum, payType,null);
			if (!CollectionUtils.isEmpty(orders)) {
				OrderVo order = orders.get(0);
				payMsg.data = new HashMap<String,Object>();
				payMsg.data.put("orderNum", order.getOrderNumber());
				payMsg.data.put("orderId", order.getOrderId());
				payMsg.data.put("oid", order.getId());
				payMsg.data.put("totalAmount", order.getTotalMount());
				//最终支付结果code
				if(PayFlowService.PAYTYPE_DOOOLY.equals(flow.getPayType())){
					payMsg.data.put("code", PayFlowService.PAYMENT_SUCCESS.equals(flow.getPayStatus()) ? PayMsg.success_code : PayMsg.failure_code);
				}else{
					payMsg.data.put("code", payMsg.getCode());
				}
				//手续费
				if(PayFlowService.PAYTYPE_DOOOLY.equals(flow.getPayType()) && order.getServiceCharge() != null){
					payMsg.data.put("serviceCharge", order.getServiceCharge());

				}
				//话费优惠活动- 分享需要的参数
				if(OrderService.ProductType.MOBILE_RECHARGE_PREFERENCE.getCode() == order.getProductType()){
					AdRechargeRecord record = adRechargeRecordDao.getRecordByOrderNumber(order.getOrderNumber());
					payMsg.data.put("openId", record.getOpenId());
					payMsg.data.put("activityParam", record.getActivityParam());
				}
			}
		}
		return payMsg;
	}

	/**
	 * 校验订单是否为待支付状态
	 * 
	 * @param orders
	 * @return
	 */
	private Long checkOrderStatus(OrderVo order) {
		if (order.getType() != OrderStatus.NEED_TO_PAY.getCode()) {
			return order.getOrderId();
		}
		return null;
	}
	/**
	 * 保存支付记录, 如果支付记录存在,支付次数+1
	 * 
	 * @param order
	 * @return
	 */
	private PayFlow savePayFlow(String orderNum, String userId,String payType,PayFlow payFlow,OrderVo order,String channel) {
		try {
			if (payFlow == null) {
				//支付金额
				BigDecimal payAmount = order.getTotalMount();
				//话费充值使用积分支付计算手续费s
				BigDecimal serviceCharge = order.getServiceCharge();
				if(PayFlowService.PAYTYPE_DOOOLY.equals(payType) && serviceCharge != null && OrderService.ProductType.MOBILE_RECHARGE.getCode() == order.getProductType()){
					payAmount = payAmount.add(order.getServiceCharge());
				}
				// 新增支付记录
				PayFlow flow = new PayFlow();
				flow.setOrderNumber(orderNum);
				flow.setPayType(payType);
				flow.setUserId(userId);
				flow.setAmount(payAmount);
				flow.setPayStatus(PayFlowService.TRADING);
				flow.setPaySumbitTime(new Date());
				flow.setPayCount(1);
				flow.setChannel(channel);
				logger.info("flow = {}", flow);
				int rows = payFlowService.insert(flow);
				return rows == 0 ? null : flow;
			} else {
				// 修改支付次数
				PayFlow flow = new PayFlow();
				flow.setPayCount(payFlow.getPayCount() + 1);
				flow.setId(payFlow.getId());
				int rows = payFlowService.update(flow);
				return rows > 0 ? payFlowService.getById(String.valueOf(payFlow.getId())) : null;
			}
		} catch (Exception e) {
			logger.error("savePayFlow e = {}", e);
		}
		return null;
	}
	
	
	/**
	 * 商品详情,多个商品详情以;分割
	 * 
	 * @param order
	 * @return
	 */
	protected String getGoodsDesc(List<OrderVo> orders) {
		StringBuffer sbf = new StringBuffer();
		for (OrderVo orderVo : orders) {
			List<OrderItemVo> items = orderVo.getItems();
			for (int i = 0; i < items.size(); i++) {
				OrderItemVo item = items.get(i);
				if (i > 0) {
					sbf.append(";");
				}
				sbf.append(item.getGoods() + "-" + item.getSku());
			}
		}
		return sbf.toString();
	}
	
	/**
	 * 订单金额
	 * 
	 * @param order
	 * @return
	 */
	protected BigDecimal getTotalAmount(List<OrderVo> orders) {
		return orderService.getPayAmount(orders);
	}
	
	/**
	 * 执行处理器<br>
	 * 1.针对某种商品类型的处理器, 如:流量,话费..<br>
	 * 2.所有订单必须执行处理器, 如:订单同步,交易记录同步..<br>
	 * @param orders
	 */
	private List<Long> doProcessor(List<OrderVo> orders,final  PayFlow payFlow) {
		try {
			for (final OrderVo order : orders) {
				//针对某种商品类型的订单处理器
				final ProductProcessor productProcessor = ProductProcessorFactory.getProcessor(order.getProductType());
				if (productProcessor != null) {
					cachedThreadPool.submit(new Runnable() {
						@Override
						public void run() {
							logger.info("执行 orderNumber = {},productProcessor = {}, class= {}",order.getOrderNumber(),productProcessor.getProcessCode(),productProcessor);
							productProcessor.process(order);
						}
					});
				}
				//所有订单都需要执行的处理器
				List<AfterPayProcessor> afterPayProcessors = AfterPayProcessorFactory.getAllProcessors();
				logger.info("afterPayProcessors.size = {}", afterPayProcessors.size());
				for (final  AfterPayProcessor afterPayProcessor : afterPayProcessors) {
					cachedThreadPool.submit(new Runnable() {
						@Override
						public void run() {
							logger.info("执行 orderNumber = {},afterPayProcessors class= {}",order.getOrderNumber(),afterPayProcessor);
							afterPayProcessor.process(order, payFlow);
						}
					});
				}
			}
		} catch (Exception e) {
			logger.info("doProcessor e = {},orders = {}", e, orders);
		}
		return null;
	}

	protected PayMsg canPay(OrderVo order){
		logger.info("productType={}", order.getProductType());
		AdUser user = adUserDao.getById(String.valueOf(order.getUserId()));
		if(order.getProductType() == OrderService.ProductType.FLOW_RECHARGE.getCode() || order.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode()){
			//手机话费充值和流量充值限制额度
			//微信支付--> 都不收手续费
			//积分支付--> 话费充值收手续费,流量充值不收手续费
			AdGroup adGroup = adGroupDao.findGroupByID(String.valueOf(user.getGroupNum()));
			BigDecimal consumptionAmount = orderService.getConsumptionAmount(user.getId());
			BigDecimal limit = adGroup.getDailyLimit();
			logger.info("consumptionAmount={},order.getTotalMount()={},limit={}", consumptionAmount,order.getTotalMount(),limit);
			if (consumptionAmount != null && limit != null) {
				if (consumptionAmount.add(order.getTotalMount()).compareTo(limit) > 0) {
					return new PayMsg(OrderMsg.purchase_limit_code, String.format("您已超出每日限额", limit));
				}
			}
		}else {
			if (!StringUtils.isEmpty(order.getActType())) {
				//是否普通订单
				if (!OrderService.ActivityType.COMMON_ORDER.getActType().equals(order.getActType())) {
					//若果活动结束或者下架,未完成的订单不能支付.
					OrderItemVo item = order.getItems().get(0);
					long userId = order.getUserId();
					String str[] = item.getProductSkuId().split("-");
					int skuId = Integer.valueOf(str[1]);
					ActivityInfo actInfo = orderService.getActivityInfo(String.valueOf(user.getGroupNum()), skuId);
					logger.info("canPay() actInfo = {},actType = {}", actInfo, order.getActType());
					if (actInfo != null) {
						if (actInfo.getActivityName().equals(order.getActType())) {
							//用户限购数量
							String activityName = actInfo.getActivityName();
							Integer actLimitNum = actInfo.getBuyNumberLimit();
							int buyQuantity = item.getNumber().intValue();
							int byNum = orderService.getBuyNum(userId, skuId, activityName);
							logger.info("canPay() actLimitNum = {},byNum + buyQuantity = {}", actLimitNum, byNum + buyQuantity);
							if (actLimitNum != null && actLimitNum < byNum + buyQuantity) {
								return new PayMsg(OrderMsg.purchase_limit_code, String.format("此商品每个账号仅限购买%s次", actLimitNum));
							}
						} else {
							//找到的活动与订单活动不匹配
							logger.info("活动与订单活动不匹配");
							return new PayMsg(OrderMsg.end_activity_code, OrderMsg.end_activity_mess);
						}
					} else {
						//未找到活动
						return new PayMsg(OrderMsg.end_activity_code, OrderMsg.end_activity_mess);
					}
				}
			}
		}
		return null;
	}
}
