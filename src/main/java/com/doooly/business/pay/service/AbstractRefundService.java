package com.doooly.business.pay.service;

import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.service.OrderService.OrderStatus;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessor;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessorFactory;
import com.doooly.business.pay.service.PayFlowService.PayType;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.dao.reachad.AdRefundFlowDao;
import com.doooly.dao.reachad.AdReturnFlowDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdRefundFlow;
import com.doooly.entity.reachad.AdReturnDetail;
import com.doooly.entity.reachad.AdReturnFlow;
import com.doooly.entity.reachad.Order;
import com.doooly.entity.reachad.OrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRefundService implements RefundService {
	

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PayFlowService payFlowService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AdRefundFlowDao adRefundFlowDao;
	@Autowired
	private AdReturnFlowDao adReturnFlowDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private MallBusinessService mallBusinessService;
    @Autowired
    private ReturnFlowService returnFlowService;

	public abstract PayMsg doRefund(OrderVo order, PayFlow payFlow);

    public abstract ResultModel dooolyApplyRefund(OrderVo order);

    public abstract ResultModel dooolyPayRefund(OrderVo order,String merchantRefundNo,String refundType);

	public PayMsg autoRefund(long userId,String orderNum){
		try {
			PayFlow payFlow = payFlowService.getByOrderNum(orderNum, null, PayFlowService.PAYMENT_SUCCESS);
			//兜礼自动退款并自动审核退货单
            OrderVo order = checkOrderStatus(userId, orderNum);
            if(order == null){
                //表示订单未完成支付，直接返回
                return new PayMsg(PayMsg.success_code, PayMsg.success_mess);
            }
            if( payFlow!= null && PayFlowService.PAYTYPE_DOOOLY.equals(payFlow.getPayType())){
				//生成退货单
				long id = saveReturnFlow(order, payFlow);
				logger.info("saveReturnFlow. id = {}", id);
				if (id > 0) {
					// 兜礼自动退款并自动审核退货单
					PayMsg msg = refund(order, payFlow);
					if (PayMsg.success_code.equals(msg.getCode())) {
						AdReturnFlow adReturnFlow = new AdReturnFlow();
						adReturnFlow.setId(id);
						adReturnFlow.setType("1");
						returnFlowService.updateByPrimaryKeySelective(adReturnFlow);
						return new PayMsg(PayMsg.success_code, PayMsg.success_mess);
					}
				}
			}
			else if(payFlow == null || PayFlowService.PAYTYPE_CASHIER_DESK.equals(payFlow.getPayType())){
                //兜礼收银台退款
                ResultModel resultModel = dooolyCashDeskRefund(userId, orderNum, orderNum, null);
                if(resultModel.getCode()==GlobalResultStatusEnum.SUCCESS.getCode()){
                    //退款成功
                    AdReturnFlow adReturnFlow = new AdReturnFlow();
                    adReturnFlow.setOrderReportId(order.getId());
                    adReturnFlow.setType("1");
                    returnFlowService.updateByPrimaryKeySelective(adReturnFlow);
                    return new PayMsg(PayMsg.success_code, PayMsg.success_mess);
                }
            }
		} catch (Exception e) {
			logger.info("autoRefund error! orderNum = {}", orderNum);
		}
		return new PayMsg(PayMsg.failure_code, PayMsg.failure_mess);
	}


	public ResultModel dooolyCashDeskRefund(long userId, String orderNum, String returnFlowNumber, String payType){
		try {
            ResultModel resultModel;
            String merchantRefundNo;
            OrderVo order = checkOrderStatus(userId, orderNum);
            if(order == null){
                //表示订单未完成支付，直接返回
                return new ResultModel(GlobalResultStatusEnum.REFUND_STATUS_SUCCESS);
            }
            AdReturnFlow adReturnFlow = returnFlowService.getByOrderId(order.getId(),returnFlowNumber,payType);
            if(adReturnFlow != null && adReturnFlow.getType().equals("1")){
                //表示已经退款
                return new ResultModel(GlobalResultStatusEnum.REFUND_STATUS_SUCCESS);
            }else if(adReturnFlow != null) {
                //说明已经申请过退款
                merchantRefundNo = String.valueOf(adReturnFlow.getReturnFlowNumber());
                resultModel = dooolyPayRefund(order, merchantRefundNo,payType);
            }else {
                //说明未申请退
                resultModel = applyRefund(userId, orderNum);
                if(resultModel.getCode()==GlobalResultStatusEnum.SUCCESS.getCode()){
                    //说明申请成功
                    Map<String,Object> map = (Map<String, Object>) resultModel.getData();
                    merchantRefundNo = (String) map.get("merchantRefundNo");
                    //查询待退款流水
                    List<AdReturnFlow> listByOrderId = adReturnFlowDao.getListByOrderId(order.getId(), returnFlowNumber, null);
                    for (AdReturnFlow returnFlow : listByOrderId) {
                        resultModel = dooolyPayRefund(order, merchantRefundNo,returnFlow.getType());
                    }
                }else {
                    return resultModel;
                }
            }
            return resultModel;
        } catch (Exception e) {
			logger.info("refund error! orderNum = {},异常原因", orderNum,e);
            return new ResultModel(GlobalResultStatusEnum.REFUND_STATUS_FAIL);
        }
	}

    private int saveOneOrder(OrderVo order,int payType,String amount,String price) {
        try {
            int rows = 0;
            logger.info("同步订单到_order开始. order ={} ===> payType = {}", order, payType);
            AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
            Order o = new Order();
            o.setUserid(order.getUserId());
            o.setOrderUserId(order.getUserId());
            o.setBussinessId(business.getBusinessId());
            o.setStoresId(OrderService.STORESID);
            o.setPayPassword(null);
            o.setVerificationCode(null);
            o.setAmount(new BigDecimal(amount));
            o.setTotalAmount(order.getTotalMount());
            o.setPrice(new BigDecimal(price));
            o.setTotalPrice(order.getTotalPrice());
            //积分是0其他是2现金
            o.setPayType(payType);
            o.setOrderNumber(order.getOrderNumber());
            o.setSerialNumber(order.getOrderNumber());
            o.setOrderDate(order.getOrderDate());
            //o.setOriginOrderNumber(null);
            o.setState(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
            o.setOrderType(1);
            o.setType(OrderService.OrderStatus.RETURN_ORDER.getCode());
            o.setSource(order.getIsSource());
            o.setIsPayPassword(0);
            o.setOrderDetail(null);
            o.setIsRebate(0);
            o.setBusinessRebate(new BigDecimal("0"));
            o.setUserRebate(new BigDecimal("0"));
            o.setCreateDateTime(new Date());
            o.setCheckState(0);
            rows = orderDao.insert(o);
            logger.info("同步订单到_order结束. rows = {}", rows);
            //同步detail
            if(o.getId() != null){
                List<OrderItemVo> items = order.getItems();
                for (int i = 0; i < items.size(); i++) {
                    OrderItemVo itVo = items.get(i);
                    OrderDetail d = new OrderDetail();
                    d.setOrderid(o.getId().intValue());
                    d.setCode(itVo.getCode());
                    d.setGoods(itVo.getGoods() + itVo.getSku());
                    d.setAmount(itVo.getAmount());
                    d.setPrice(itVo.getPrice());
                    d.setNumber(itVo.getNumber());
                    d.setTax(new BigDecimal("0"));
                    d.setCategory(itVo.getCategoryId());
                    d.setFirstCategory(null);
                    d.setSecondCategory(null);
                    d.setBrandName(null);
                    d.setCreatedatetime(new Date());
                    int r = orderDao.insertDetail(d);
                    if (r > 0) {
                        logger.info("同步订单到_orderDetail结束. index ={},rows = {}", i, rows);
                    }
                    rows += r;
                }
            }
            return rows;
        } catch (Exception e) {
            logger.error("同步订单到_order出现异常. order = {},e = {}", order, e);
        }
        return 0;
    }
	
	public PayMsg refund(long userId,String orderNum) {
		logger.info("refund start. userId = {},orderNum = {}", userId, orderNum);
		// 校验支付记录
		PayFlow payFlow = payFlowService.getByOrderNum(orderNum, null, PayFlowService.PAYMENT_SUCCESS);
		if (payFlow == null) {
			logger.info("payFlow not found. orderNum = {}", orderNum);
			return new PayMsg(PayMsg.failure_code, "payFlow not found. orderNum=" + orderNum);
		}
		// 校验订单及状态
		OrderVo order = checkOrderStatus(userId, orderNum);
		if (order == null) {
			logger.info("order not found. userId= {},orderNum = {},type,state.", userId, orderNum);
			return new PayMsg(PayMsg.failure_code, "order not found. orderNum=" + orderNum);
		}
		return refund(order, payFlow);
	}
	
	public PayMsg refund(OrderVo order, PayFlow payFlow) {
		PayMsg refundMsg = this.doRefund(order, payFlow);
		logger.info("refundMsg = {}", refundMsg);
        if(GlobalResultStatusEnum.REFUND_STATUS_SUCCESS.toString().equals(refundMsg.getCode())){
            // 修改订单状态-已退款
            int u2 = orderService.updateOrderRefund(order, String.valueOf(order.getUserId()));

            if(!PayFlowService.PAYTYPE_DOOOLY.equals(payFlow.getPayType())){
                // 非积分退款需要同步退款流水到_order
                saveOneOrder(order, payFlow);
            }else{
                //积分退款要修改businessId一致
                updateBusinessId(order);
            }
            if(u2 >0){
                afterRefundProcess(order);
                return new PayMsg(PayMsg.success_code, PayMsg.success_mess);
            }else{
                logger.error("u2 = {}", u2);
                return new PayMsg(PayMsg.failure_code, PayMsg.failure_mess);
            }
        }
        return refundMsg;
	}

	private int updateBusinessId(OrderVo order) {
		try {
			AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
			int u4 = orderDao.updateBussinessIdByNum(order.getOrderNumber(), business.getBusinessId());
			logger.info("updateBussinessIdByNum u4 = {}", u4);
			return u4;
		} catch (Exception e) {
			logger.error("updateBusinessId() error = {},orderNum = {}",e, order.getOrderNumber());
		}
		return 0;
	}

    @Override
    public ResultModel applyRefund(long userId, String orderNum) {
        OrderVo order = checkOrderStatus(userId, orderNum);
        if(order == null){
            //表示订单未完成支付，直接返回
            return new ResultModel(GlobalResultStatusEnum.FAIL,"订单未完成支付，申请退款失败");
        }
        return dooolyApplyRefund(order);
    }

    private OrderVo checkOrderStatus(long userId,String orderNum) {
		try {
			OrderVo o = new OrderVo();
			o.setOrderNumber(orderNum);
			o.setUserId(userId);
            //o.setType(OrderStatus.HAD_FINISHED_ORDER.getCode());
            o.setState(OrderService.PayState.PAID.getCode());
			return orderService.getOrder(o).get(0);
		} catch (Exception e) {
			logger.error("checkOrderStatus() error = {},orderNum = {}",e, orderNum);
		}
		return null;
	}
	
	private int updateRefundFlow(String refundFlowId,String refundId,String refundStatus,String errCode, String errReason) {
		try {
			AdRefundFlow flow = new AdRefundFlow();
			flow.setId(Long.valueOf(refundFlowId));
			flow.setRefundStatus(refundStatus);
			flow.setRefundId(refundId);
			flow.setErrorCode(errCode);
			flow.setErrorReason(errReason);
			return adRefundFlowDao.updateByPrimaryKeySelective(flow);
		} catch (Exception e) {
			logger.error("updateRefundFlow error,e = {}", e);
		}
		return 0;
	}
	
	private long saveReturnFlow(OrderVo order,PayFlow payFlow){
		try {
            AdReturnFlow returnFlow = returnFlowService.getByOrderId(order.getId(), order.getOrderNumber(), "1");
            if(returnFlow != null){
                //说明已经生成过流水
                return returnFlow.getId();
            }
            // 退货流水记录
			AdReturnFlow adReturnFlow = new AdReturnFlow();
			adReturnFlow.setOrderReportId(order.getId());
			adReturnFlow.setReturnFlowNumber(order.getOrderNumber());
			adReturnFlow.setPayType((short) PayType.getCodeByName(payFlow.getPayType()));
			adReturnFlow.setAmount(order.getTotalMount());
			adReturnFlow.setType("0");// 0:未审核 1：已审核
			adReturnFlow.setUserRebate(new BigDecimal(order.getUserRebate()));
			adReturnFlow.setUserReturnAmount(order.getUserReturnAmount());
			adReturnFlow.setBusinessRebateAmount(order.getBusinessRebateAmount());
			adReturnFlow.setBillingState(String.valueOf(order.getBillingState()));
			adReturnFlow.setCreateBy(String.valueOf(order.getUserId()));
			adReturnFlow.setUpdateBy(String.valueOf(order.getUserId()));
			adReturnFlow.setIsFirst(0);
			adReturnFlow.setCreateDate(new Date());
			adReturnFlow.setRemarks(order.getRemarks());
			adReturnFlow.setIsSource(3);
			adReturnFlow.setDelFlag("0");
			// 退货流水记录-子表
			List<OrderItemVo> items = order.getItems();
			List<AdReturnDetail> details = new ArrayList<AdReturnDetail>();
			for (OrderItemVo item : items) {
				AdReturnDetail detail = new AdReturnDetail();
				detail.setReturnFlowId(order.getId());
				detail.setCategoryId(item.getCategoryId());
				detail.setCode(item.getCode());
				detail.setGoods(item.getGoods() + item.getSku());
				detail.setAmount(item.getAmount());
				detail.setNumber(item.getNumber());
				detail.setCreateBy(String.valueOf(order.getUserId()));
				detail.setUpdateBy(String.valueOf(order.getUserId()));
				detail.setCreateDate(new Date());
				detail.setDelFlag("0");
				details.add(detail);
			}
			adReturnFlow.setDetails(details);
			return returnFlowService.insert(adReturnFlow);
		} catch (Exception e) {
			logger.error("saveReturnFlow error,e = {}", e);
		}
		return 0;
	}

	private int saveOneOrder(OrderVo order, PayFlow payFlow) {
		try {
			int rows = 0;
			logger.info("同步订单到_order开始. order ={} ===> payFlow = {}", order, payFlow);
			AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
			Order o = new Order();
			o.setUserid(order.getUserId());
			o.setOrderUserId(order.getUserId());
			o.setBussinessId(business.getBusinessId());
			o.setStoresId(OrderService.STORESID);
			o.setPayPassword(null);
			o.setVerificationCode(null);
			o.setAmount(order.getTotalMount());
			o.setTotalAmount(order.getTotalMount());
			o.setPrice(order.getTotalPrice());
			o.setTotalPrice(order.getTotalPrice());
			//积分是0其他是2现金
			o.setPayType(PayType.getCodeByName(payFlow.getPayType())); 
			o.setOrderNumber(order.getOrderNumber());
			o.setSerialNumber(order.getOrderNumber());
			o.setOrderDate(order.getOrderDate());
			//o.setOriginOrderNumber(null);
			o.setState(OrderStatus.HAD_FINISHED_ORDER.getCode());
			o.setOrderType(1);
			o.setType(OrderStatus.RETURN_ORDER.getCode());
			o.setSource(order.getIsSource());
			o.setIsPayPassword(0);
			o.setOrderDetail(null);
			o.setIsRebate(0);
			o.setBusinessRebate(new BigDecimal("0"));
			o.setUserRebate(new BigDecimal("0"));
			o.setCreateDateTime(new Date());
			o.setCheckState(0);
			rows = orderDao.insert(o);
			logger.info("同步订单到_order结束. rows = {}", rows);
			//同步detail
			if(o.getId() != null){
				List<OrderItemVo> items = order.getItems();
				for (int i = 0; i < items.size(); i++) {
					OrderItemVo itVo = items.get(i);
					OrderDetail d = new OrderDetail();
					d.setOrderid(o.getId().intValue());
					d.setCode(itVo.getCode());
					d.setGoods(itVo.getGoods() + itVo.getSku());
					d.setAmount(itVo.getAmount());
					d.setPrice(itVo.getPrice());
					d.setNumber(itVo.getNumber());
					d.setTax(new BigDecimal("0"));
					d.setCategory(itVo.getCategoryId());
					d.setFirstCategory(null);
					d.setSecondCategory(null);
					d.setBrandName(null);
					d.setCreatedatetime(new Date());
					int r = orderDao.insertDetail(d);
					if (r > 0) {
						logger.info("同步订单到_orderDetail结束. index ={},rows = {}", i, rows);
					}
					rows += r;
				}
			}
			return rows;
		} catch (Exception e) {
			logger.error("同步订单到_order出现异常. order = {},e = {}", order, e);
		}
		return 0;
	}

	/**
	 * 保存退款记录
	 * @param order
	 * @param payFlow
	 * @return
	 */
	private PayMsg saveRefundFlow(OrderVo order, PayFlow payFlow) {
		try {
            AdRefundFlow byOrderNumber = adRefundFlowDao.findByOrderNumber(order.getOrderNumber());
            if(byOrderNumber != null){
                if(byOrderNumber.getRefundStatus().equals(REFUND_STATUS_S)){
                    //说明已经退款
                   return new PayMsg(PayMsg.failure_code, "改订单已经退款，请勿重复退款");
                }
                PayMsg msg = new PayMsg(PayMsg.success_code, PayMsg.success_mess,new HashMap<String, Object>());
                msg.data.put("out_refund_id", byOrderNumber.getId().toString());
                return msg;
            }
            AdRefundFlow flow = new AdRefundFlow();
			flow.setOrderNumber(order.getOrderNumber());
			flow.setPayType(payFlow.getPayType());
			flow.setUserId(payFlow.getUserId());
			flow.setOldTransNo(payFlow.getTransNo());
			flow.setAmount(payFlow.getAmount());
			flow.setApplyAmount(payFlow.getAmount());
			flow.setSubmitTime(new Date());
			flow.setRefundStatus(REFUND_STATUS_I);
			int rows = adRefundFlowDao.insert(flow);
			if(rows > 0){
				PayMsg msg = new PayMsg(PayMsg.success_code, PayMsg.success_mess,new HashMap<String, Object>());
				msg.data.put("out_refund_id", flow.getId().toString());
				return msg;
			}
		} catch (Exception e) {
			logger.error("saveRefundFlow() err = {}", e);
		}
		return new PayMsg(PayMsg.failure_code, "saveRefundFlow.insert err.");
	}

	/**
	 * 退款成功后执行处理器
	 *
	 * @param order
	 */
	private void afterRefundProcess(OrderVo order){
		List<AfterRefundProcessor> afterPayProcessors = AfterRefundProcessorFactory.getAllProcessors();
		for (AfterRefundProcessor afterRefundProcessor : afterPayProcessors) {
			logger.info("afterRefundProcess() afterRefundProcessor = {}", afterRefundProcessor);
			new Thread(new Runnable() {
				@Override
				public void run() {
					logger.error("执行afterRefundProcess class = {}",afterRefundProcessor);
					afterRefundProcessor.process(order, new Order());
				}
			}).start();
		}
	}
	
}
