package com.doooly.business.pay.processor.afterpayprocessor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.service.OrderService.OrderStatus;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.business.pay.service.PayFlowService.PayType;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.Order;
import com.doooly.entity.reachad.OrderDetail;
import org.springframework.util.StringUtils;

/***
 * 同步订单到_order
 * 
 * @author 2017-10-25 17:05:50 WANG
 */
@Component
public class SynToOrderProcessor implements AfterPayProcessor{
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private MallBusinessService mallBusinessService;


	
	@Override
	public PayMsg process(OrderVo order,PayFlow payFlow) {
		try {
			logger.info("同步订单到_order开始. orderNum = {}", order.getOrderNumber());
			AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
			Order o = new Order();
			o.setId(order.getId());
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
			o.setPayType(PayType.getCodeByName(payFlow.getPayType()));
			o.setOrderNumber(order.getOrderNumber());
			o.setSerialNumber(order.getOrderNumber());
			o.setOrderDate(order.getOrderDate());
			//o.setOriginOrderNumber(null);
			o.setState(OrderStatus.HAD_FINISHED_ORDER.getCode());
			o.setOrderType(1);
			o.setType(OrderStatus.HAD_FINISHED_ORDER.getCode());
			o.setSource(order.getIsSource());
			o.setIsPayPassword(0);
			o.setOrderDetail(null);
			o.setIsRebate(0);
			//旅游卡计算返佣返利
			if (order.getProductType() == OrderService.ProductType.TOURIST_CARD_RECHARGE.getCode()) {
				BigDecimal businessRebateAmount = null;
				if (!StringUtils.isEmpty(business.getBussinessRebate())) {
					BigDecimal businessRebate = new BigDecimal(business.getBussinessRebate()).divide(new BigDecimal("100"));
					businessRebateAmount = order.getTotalMount().multiply(businessRebate);
					o.setBusinessRebate(businessRebateAmount);
				}
				logger.info(" businessRebateAmount = {}", businessRebateAmount);
			} else {
				o.setBusinessRebate(order.getBusinessRebateAmount());
			}
			o.setUserRebate(order.getUserReturnAmount());

			o.setCreateDateTime(new Date());
			o.setCheckState(0);
			int rows = orderDao.updateById(o);
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
				}
			}
		} catch (Exception e) {
			logger.error("同步订单到_order出现异常. order = {},e = {}", order, e);
		}
		return null;
	}
	
	
	
}
