package com.doooly.business.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.exwings.ExWingsUtils;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.MerchantProdcutVo;
import com.doooly.business.order.vo.OrderExtVo;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.order.vo.ProductSkuVo;
import com.doooly.business.pay.bean.AdOrderSource;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.business.product.entity.AdSelfProduct;
import com.doooly.business.product.entity.AdSelfProductImage;
import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.business.product.service.ProductService;
import com.doooly.common.util.IdGeneratorUtil;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdOrderDeliveryDao;
import com.doooly.dao.reachad.AdOrderDetailDao;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdOrderSourceDao;
import com.doooly.dao.reachad.AdRechargeConfDao;
import com.doooly.dao.reachad.AdRechargeRecordDao;
import com.doooly.dao.reachad.AdSelfProductImageDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.OrderMsg;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdCoupon;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdRechargeConf;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.Order;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一下单接口
 *
 * @author 2017-09-21 14:57:24 WANG
 */
@Service
public class OrderServiceImpl implements OrderService {

	protected Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	@Autowired
	private AdOrderReportDao adOrderReportDao;
	@Autowired
	private AdOrderDeliveryDao adOrderDeliveryDao;
	@Autowired
	private AdOrderDetailDao adOrderDetailDao;
	@Autowired
	private ProductService productService;
	@Autowired
	private AdSelfProductImageDao adSelfProductImageDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	protected AdUserDao adUserDao;
	@Autowired
	private AdCouponCodeDao adCouponCodeDao;
	@Autowired
	AdRechargeRecordDao adRechargeRecordDao;
	@Autowired
	private AdRechargeConfDao adRechargeConfDao;
    @Autowired
    private RefundService refundService;
    @Autowired
    private AdOrderSourceDao adOrderSourceDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OrderMsg createOrder(JSONObject json) {
		long s = System.currentTimeMillis();
		logger.info("Start creating order. json = {}", json);
		OrderVo orderVo = json.toJavaObject(OrderVo.class);
		//检查订单参数
		OrderMsg checkMsg = checkOrderParams(orderVo);
		if (checkMsg != null) {
			return checkMsg;
		}
		OrderExtVo orderExtVo = orderVo.getOrderExt();
		List<MerchantProdcutVo> merchants = orderVo.getMerchantProduct();
		long userId = orderVo.getUserId();
		String orderNum = IdGeneratorUtil.getOrderNumber(orderVo.getIsSource());
		String actType = ActivityType.COMMON_ORDER.getActType();
		for (MerchantProdcutVo merchantProduct : merchants) {
			int merchantId = merchantProduct.getMerchantId();
			String remarks = merchantProduct.getRemarks();
			List<OrderItemVo> orderItems = new ArrayList<OrderItemVo>();
			BigDecimal totalMount = new BigDecimal("0");//实付金额
			BigDecimal totalPrice = new BigDecimal("0");//应付金额
			List<ProductSkuVo> prodcutSkus = merchantProduct.getProductSku();
			//商家对应的商品SKU
			for (ProductSkuVo productSkuVo : prodcutSkus) {
				int productId = productSkuVo.getProductId();
				int skuId = productSkuVo.getSkuId();
				int buyQuantity = productSkuVo.getBuyNum();

				AdSelfProduct product = productService.getProductSku(merchantId, productId, skuId);
				if (product == null) {
					return new OrderMsg(OrderMsg.failure_code, "未找到商品信息.");
				}
				List<AdSelfProductSku> skus = product.getProductSku();
				AdSelfProductSku sku = null;
				if (CollectionUtils.isEmpty(product.getProductSku()) || (sku = skus.get(0)) == null) {
					return new OrderMsg(OrderMsg.failure_code, "未找到商品规格信息.");
				}
				if (buyQuantity <= 0) {
					return new OrderMsg(OrderMsg.failure_code, "购买数量必须大于0");
				}
				BigDecimal sellPrice = new BigDecimal(sku.getSellPrice());
				if (orderVo.getProductType() == ProductType.MOBILE_RECHARGE.getCode()) {
					//话费充值计算手续费
					OrderMsg msg = getServiceChargeAndCheckLimit(orderVo, sku);
					if (OrderMsg.success_code.equals(msg.getCode())) {
						if(msg.data != null) {
							orderVo.setServiceCharge((BigDecimal) msg.data.get("serviceCharge"));
						}
					} else {
						return msg;
					}
				} else if (orderVo.getProductType() == ProductType.TOURIST_CARD_RECHARGE.getCode()) {
					// do nothing
				} else {
					//活动
					OrderMsg msg = getActInfo(orderVo, productSkuVo);
					if (OrderMsg.success_code.equals(msg.getCode())) {
						if (msg.data != null) {
							actType = (String) msg.data.get("actType");
							sellPrice = (BigDecimal) msg.data.get("actPrice");
						}

						if (orderVo.getProductType() == ProductType.NEXUS_RECHARGE_ACTIVITY.getCode()) {
						    sku.setSellPrice(sellPrice.toString());
							msg = getServiceChargeAndCheckLimit(orderVo, sku);
							if (OrderMsg.success_code.equals(msg.getCode())) {
								if(msg.data != null) {
									orderVo.setServiceCharge((BigDecimal) msg.data.get("serviceCharge"));
								}
							} else {
								return msg;
							}
						}
					} else {
						return msg;
					}
				}
				//校验库存并扣除库存
				Integer inventory = sku.getInventory();
				if (inventory != null) {
					if (inventory <= 0) {
						logger.error("product.inventory = {}", inventory);
						return new OrderMsg(OrderMsg.out_of_stock_code1, OrderMsg.out_of_stock_mess1);
					}
					int rows = productService.decInventory(skuId);
					logger.info("decInventory() skuId={},inventor={},rows={}", skuId, inventory, rows);
					if (rows == 0) {
						if (orderVo.getProductType() == ProductType.NEXUS_RECHARGE_ACTIVITY.getCode()) {
							return new OrderMsg(OrderMsg.out_of_stock_code2, OrderMsg.out_of_stock_mess2);
						} else {
							return new OrderMsg(OrderMsg.create_order_failed_code, OrderMsg.create_order_failed_mess);
						}
					}
				}
				BigDecimal marketPrice = new BigDecimal(sku.getMarketPrice());
				totalMount = totalMount.add(sellPrice.multiply(new BigDecimal(String.valueOf(buyQuantity))));
				totalPrice = totalPrice.add(marketPrice.multiply(new BigDecimal(String.valueOf(buyQuantity))));
				OrderItemVo orderItem = buildOrderItem(orderVo, userId, remarks, buyQuantity, product, sku, sellPrice, marketPrice);
				orderItems.add(orderItem);
			}
			//自营优惠券
			OrderMsg msg = getDisAmountAndSetCoupon(orderVo, totalMount);
			BigDecimal couponValue = null;
			String couponId = null;
			if (OrderMsg.success_code.equals(msg.getCode())) {
				BigDecimal discountAmount = (BigDecimal) msg.data.get("discountAmount");
				totalMount = discountAmount != null ? discountAmount : totalMount;
				couponValue = (BigDecimal) msg.data.get("couponValue");
				couponId = (String) msg.data.get("couponId");
			}else{
				return msg;
			}
			//扣券后的订单明细金额
			orderItems.get(0).setAmount(totalMount);
			//保存订单
			OrderExtVo orderExt = buildOrderExt(orderExtVo);
			OrderVo order = buildOrder(orderVo, orderExt, orderNum, merchantId, totalMount, totalPrice, userId, actType, couponValue, couponId);
			int rows = saveOrder(order, orderExt, orderItems);
			logger.info("Create order successfully. orderNumber = {}, rows = {},execution time : {} milliseconds.", orderNum, rows, System.currentTimeMillis() - s);
		}
		//下单成功返回信息
		OrderMsg msg = new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess);
		msg.getData().put("orderNum", orderNum);
		return msg;
	}

	@Transactional(rollbackFor = Exception.class)
	public int lockCoupon(long userId,String couponId){
		return adCouponCodeDao.lockCoupon(userId,couponId);
	}


	/**
	 * 使用限制额度
	 *
	 * @param orderVo
	 * @return
	 */
	public OrderMsg getServiceChargeAndCheckLimit(OrderVo orderVo,AdSelfProductSku sku) {
		AdRechargeConf conf = adRechargeConfDao.getRechargeConf(orderVo.getGroupId());
		logger.info("conf = {}", conf);
		if (conf == null) {
			return new OrderMsg(OrderMsg.failure_code, "没有找到话费充值配置");
		}
		BigDecimal sellPrice = new BigDecimal(sku.getSellPrice());
		//每月积分购买话费的金额
		BigDecimal consumptionAmount = this.getConsumptionAmount(orderVo.getUserId());
		if (consumptionAmount == null) {
			consumptionAmount = new BigDecimal("0");
		}
		//话费充值订单计算手续费
		BigDecimal charges = conf.getCharges();
		if (charges != null) {
            //每月免手续费金额
			BigDecimal discountsMonthLimit = conf.getDiscountsMonthLimit();
			if(discountsMonthLimit == null){
				discountsMonthLimit = new BigDecimal("0");
			}
			//超过免手续部分
			BigDecimal c = charges.divide(new BigDecimal("100"));
			BigDecimal serviceCharge = new BigDecimal("0");
			//大于免手续部分按charges收
			if (consumptionAmount.compareTo(discountsMonthLimit) > 0) {
				serviceCharge = sellPrice.multiply(c).setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				//剩余免手续费部分
				BigDecimal leftDiscount = discountsMonthLimit.subtract(consumptionAmount);
				if (sellPrice.compareTo(leftDiscount) > 0) {
					BigDecimal diff = sellPrice.subtract(leftDiscount);
					serviceCharge = diff.multiply(c).setScale(2, BigDecimal.ROUND_HALF_UP);
				} else {
					serviceCharge = new BigDecimal("0");
				}
			}
			//计算手续费
			logger.info("serviceCharge = {}", serviceCharge);
			Map map = new HashMap();
			map.put("serviceCharge", serviceCharge);
			return new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess, map);
		}
		return new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess, null);
	}

	//设置优惠券并返回优化订单金额
	public OrderMsg getDisAmountAndSetCoupon(OrderVo orderVo,BigDecimal totalMount){
		BigDecimal discountAmount = null;
		BigDecimal couponValue = null;
		String couponId = orderVo.getCouponId();
		long userId = orderVo.getUserId();
		if(StringUtils.isNotEmpty(couponId)){
			AdCouponCode couponCode = adCouponCodeDao.getSelfCoupon(userId,couponId);
			if(couponCode != null){
				if(!AdCouponCode.LOCKED.equals(couponCode.getIsLocked())){
					AdCoupon coupon = couponCode.getAdCoupon();
					if(null != coupon) {
						couponValue = coupon.getCouponValue();
						//券金额不能大于售价
						if(totalMount.compareTo(couponValue) == -1){
							return new OrderMsg("3001", "抵扣券金额大于售价");
						}
						//锁定券
						int row = lockCoupon(userId,couponId);
						if(row == 0){
							return new OrderMsg("3002", "锁定券失败");
						}
						//抵扣后的金额
						discountAmount = totalMount.subtract(couponValue);
					}
				}else{
					return new OrderMsg("3003", "券已被锁定,无法使用");
				}
			}else{
				return new OrderMsg("3004", "无效的券ID");
			}
		}
		Map map = new HashMap();
		map.put("discountAmount", discountAmount);
		map.put("couponValue", couponValue);
		map.put("couponId", couponId);
		return new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess, map);
	}


	/**
	 * 商品配置的活动信息
	 *
	 * @param orderVo
	 * @param product
	 * @return
	 */
	public OrderMsg getActInfo(OrderVo orderVo,ProductSkuVo product){
		int skuId = product.getSkuId();
		int buyQuantity = product.getBuyNum();
		ActivityInfo actInfo = this.getActivityInfo(orderVo.getGroupId(), skuId);
		logger.info("actInfo = {}",actInfo);
		String activityName = null;
		BigDecimal actPrice = null;
		if(actInfo != null){
			//活动类型
			activityName = actInfo.getActivityName();
			if (StringUtils.isEmpty(activityName)) {
				logger.error("activityName = {}", activityName);
				return new OrderMsg(OrderMsg.create_order_failed_code, OrderMsg.create_order_failed_mess);
			}
			//活动价格
			if (actInfo.getSpecialPrice() != null && actInfo.getSpecialPrice().compareTo(BigDecimal.ZERO) == 1) {
				actPrice = actInfo.getSpecialPrice();
			}
			//用户限购数量
			Integer actLimitNum = actInfo.getBuyNumberLimit();
			int buyNum = getBuyNum(orderVo.getUserId(), skuId, activityName);
			logger.info("actLimitNum = {},byNum + buyQuantity = {}", actLimitNum, buyNum + buyQuantity);
			if (actLimitNum != null && actLimitNum < buyNum + buyQuantity) {
				return new OrderMsg(OrderMsg.purchase_limit_code, String.format("此商品每个账号仅限购买%s次", actLimitNum));
			}
			//活动库存校验
			Integer inventory = actInfo.getInventory();
			if (inventory != null) {
				//校验库存
				/*if (inventory <= 0) {
					logger.error("activity.inventory = {}", inventory);
					return new OrderMsg(OrderMsg.out_of_stock_code2, OrderMsg.out_of_stock_mess2);
				}*/
				//扣减库存
				OrderMsg msg = decStock(actInfo.getNumber(), skuId, inventory);
				if (msg != null) {
					return msg;
				}
			}
			Map map = new HashMap();
			map.put("actType", activityName);
			map.put("actPrice", actPrice);
			return new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess, map);
		}
		return new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess, null);
	}


	@Transactional(rollbackFor = Exception.class)
	public OrderMsg decStock(int number,int skuId,int oldNum){
		//扣减活动库存
		int rows = productService.decStock(number, skuId);
		logger.info("decStock() skuId={},inventor={},rows = {}", skuId,oldNum, rows);
		if (rows == 0) {
			return new OrderMsg(OrderMsg.out_of_stock_code2, OrderMsg.out_of_stock_mess2);
		}
		return null;
	}

	/**
	 * 检查是否可以创建订单
	 *
	 * @param orderVo
	 */
	private OrderMsg checkOrderParams(OrderVo orderVo) {
		//检查订单数据
		List<MerchantProdcutVo> merchants = orderVo.getMerchantProduct();
		if(CollectionUtils.isEmpty(merchants)){
			return new OrderMsg(OrderMsg.failure_code, "merchantProduct is empty.");
		}
		for (MerchantProdcutVo merchantProduct : merchants) {
			List<ProductSkuVo> prodcutSkus = merchantProduct.getProductSku();
			if(CollectionUtils.isEmpty(prodcutSkus)){
				return new OrderMsg(OrderMsg.failure_code, "productSku is empty.");
			}
			if(merchantProduct.getMerchantId() == 0){
				return new OrderMsg(OrderMsg.failure_code, "invalid merchantId.");
			}
			for (ProductSkuVo productSkuVo : prodcutSkus) {
				int productId = productSkuVo.getProductId();
				if(productId <= 0){
					return new OrderMsg(OrderMsg.failure_code, "invalid productId.");
				}
				int skuId = productSkuVo.getSkuId();
				if(skuId <= 0){
					return new OrderMsg(OrderMsg.failure_code, "invalid skuId.");
				}
				int buyNum = productSkuVo.getBuyNum();
				if(buyNum <= 0){
					return new OrderMsg(OrderMsg.failure_code, "invalid buyNum.");
				}
			}
		}
		return null;
	}

	private OrderItemVo buildOrderItem(OrderVo order,long userId, String remarks, int buyNum, AdSelfProduct product,
									   AdSelfProductSku sku, BigDecimal sellPrice, BigDecimal marketPrice) {
		String category  = getCategory(product);
		OrderItemVo orderItem = new OrderItemVo();
		orderItem.setOrderReportId(0l);
		orderItem.setCategoryId(category.toString());
		orderItem.setCode(sku.getNumber());
		orderItem.setGoods(product.getName());
		orderItem.setAmount(sellPrice);
		orderItem.setPrice(marketPrice);
		orderItem.setNumber(new BigDecimal(buyNum));
		orderItem.setCreateBy(String.valueOf(userId));
		orderItem.setDelFlag(0);
		orderItem.setRemarks(remarks);
		orderItem.setTax(null);
		orderItem.setUpdateDate(null);
		orderItem.setUpdateBy(null);
		orderItem.setCreateDate(new Date());
		orderItem.setSku(sku.getSpecification());
		orderItem.setProductSkuId(product.getId() + "-" + sku.getId());
		orderItem.setExternalNumber(sku.getExternalNumber());
		orderItem.setDuihuanUrl(product.getDuihuanUrl());
		//摩拜订单号
		if(order.getProductType() == ProductType.MOBIKE_RECHARGE.getCode()){
			orderItem.setCardOid(ExWingsUtils.getOrderId());
		}
		//集享订单号
		if(order.getProductType() == ProductType.NEXUS_RECHARGE.getCode()){
			orderItem.setCardOid(ExWingsUtils.getOrderId());
		}
		AdSelfProductImage image = adSelfProductImageDao.getImageByProductId(product.getId());
		if (image != null) {
			orderItem.setProductImg(image.getImage());
		}
		return orderItem;
	}

	private OrderVo buildOrder(OrderVo orderVo,OrderExtVo orderExt, String orderNumber, int merchantId, BigDecimal totalMount,
							   BigDecimal totalPrice, long userId,String actType,BigDecimal couponValue,String couponId) {
		OrderVo order = new OrderVo();
		Date orderDate = new Date();
		order.setOrderExt(orderExt);
		order.setBussinessId(merchantId);
		order.setUserId(userId);
		order.setOrderNumber(orderNumber);
		order.setStoresId(orderVo.getStoresId());
		order.setTotalMount(totalMount);
		order.setTotalPrice(totalPrice);
		order.setOrderDate(orderDate);
		order.setState(PayState.UNPAID.getCode());
		order.setType(OrderStatus.NEED_TO_PAY.getCode());
		order.setIsUserRebate('0');
		order.setUserRebate(orderVo.getUserRebate());
		order.setUserReturnAmount(new BigDecimal("0"));
		order.setIsBusinessRebate(orderVo.getIsBusinessRebate());
		order.setBusinessRebateAmount(new BigDecimal("0"));
		order.setBillingState('0');
		order.setDelFlag('0');
		order.setDelFlagUser('0');
		order.setCreateBy(String.valueOf(userId));
		order.setIsFirst('0');
		order.setIsSource(DOOOLY_SOURCE);
		order.setFirstCount(0);
		order.setAirSettleAccounts(null);
		order.setRemarks(orderVo.getRemarks());
		order.setUpdateDate(null);
		order.setCreateDate(orderDate);
		order.setConsigneeName(orderVo.getConsigneeName());
		order.setConsigneeAddr(orderVo.getConsigneeAddr());
		order.setConsigneeMobile(orderVo.getConsigneeMobile());
		order.setProductType(orderVo.getProductType());
		order.setActType(actType);
		order.setVoucher(couponValue);
		order.setCouponId(couponId);
		//支持支付方式 ==> 1:积分,2:微信, 3.支付宝; 多个以逗号分割
		if(order.getProductType() == ProductType.NEXUS_RECHARGE.getCode()){
			//全家集享卡只支持积分支付
			order.setSupportPayType("1");
		}else{
			if (BigDecimal.ZERO.compareTo(totalMount) == 0) {
				//0元订单
				order.setSupportPayType("0");
				order.setServiceCharge(BigDecimal.ZERO);
			} else {
				//非0元订单
				String supportPayType = orderVo.getSupportPayType();
				if (StringUtils.isEmpty(orderVo.getSupportPayType())) {
					supportPayType = "all";
				}
				order.setSupportPayType(supportPayType);
				order.setServiceCharge(orderVo.getServiceCharge());
			}
		}
		return order;
	}

	private OrderExtVo buildOrderExt(OrderExtVo orderExtVo) {
		if(orderExtVo != null){
			OrderExtVo orderExt = new OrderExtVo();
			orderExt.setType(orderExtVo.getType());
			orderExt.setApplyCardArea1(orderExtVo.getApplyCardArea1());
			orderExt.setApplyCardStore1(orderExtVo.getApplyCardStore1());
			orderExt.setApplyCardArea2(orderExtVo.getApplyCardArea2());
			orderExt.setApplyCardStore2(orderExtVo.getApplyCardStore2());
			orderExt.setDeliveryName(orderExtVo.getDeliveryName());
			orderExt.setDeliveryTelephone(orderExtVo.getDeliveryTelephone());
			orderExt.setIdentityCard(orderExtVo.getIdentityCard());
			orderExt.setIdentityCardImage(orderExtVo.getIdentityCardImage());
			orderExt.setApplyCardImage(orderExtVo.getApplyCardImage());
			orderExt.setCreateDate(new Date());
			orderExt.setUpdateDate(null);
			orderExt.setEmpCard(orderExtVo.getEmpCard());
			return orderExt;
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public int saveOrder(OrderVo order, OrderExtVo orderExt, List<OrderItemVo> orderItem) {
		int rows = 0;
		long oneOrderId = createOneOrder(order);
		order.setId(oneOrderId);
		order.setOrderId(oneOrderId);
		rows += saveOrder(order);
		logger.info("order.id = {}", order.getId());
		if (orderExt != null) {
			rows += saveOrderExt(order.getId(),orderExt);
		}
		rows += saveOrderItem(order.getId(), orderItem);
        AdOrderSource adOrderSource = new AdOrderSource();
        adOrderSource.setOrderNumber(order.getOrderNumber());
        adOrderSource.setBusinessId(order.getBussinessId());
        adOrderSource.setCashDeskSource("d");
        adOrderSource.setTraceCodeSource("d");
        adOrderSourceDao.insert(adOrderSource);
		return rows;
	}

	/***
	 * 因为要和_order主键一致,这里要先生成_order,然后使用_order的主键.
	 *
	 * @param order
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	private long createOneOrder(OrderVo order) {
		Order o = new Order();
		o.setIsRebate(0);
		o.setBusinessRebate(new BigDecimal("0"));
		o.setUserRebate(new BigDecimal("0"));
		o.setCreateDateTime(new Date());
		o.setState(0);
        o.setSource(3);//兜礼自营
		orderDao.insert(o);
		return o.getId();
	}

	@Transactional
	@Override
	public long saveOrder(OrderVo order) {
		return adOrderReportDao.insert(order);
	}

	@Transactional
	@Override
	public int saveOrderExt(long orderId,OrderExtVo orderExt) {
		return adOrderDeliveryDao.insert(orderId,orderExt);
	}

	@Transactional
	@Override
	public int saveOrderItem(long orderId,List<OrderItemVo> orderItem) {
		return adOrderDetailDao.bantchInsert(orderId,orderItem);
	}


	@Override
	public OrderVo getByOrderNum(String orderNum) {
		return adOrderReportDao.getByOrderNum(orderNum).get(0);
	}

	/**
	 * 为了兼容以后多个订单一起支付的情况
	 */
	@Override
	public List<OrderVo> getByOrdersNum(String orderNum) {
		return adOrderReportDao.getByOrderNum(orderNum);
	}

	@Override
	public OrderVo getById(String id) {
		return adOrderReportDao.getById(id);
	}
	/**
	 * 支付金额
	 * @param orders
	 * @return 以分为单位
	 */
	public BigDecimal getPayAmount(List<OrderVo> orders) {
		BigDecimal amount = new BigDecimal("0");
		for (OrderVo orderVo : orders) {
			amount = amount.add(orderVo.getTotalMount());
		}
		return amount;
	}
	/**
	 * 支付金额
	 * @param orderNum
	 * @return
	 */
	public BigDecimal getPayAmountByNum(String orderNum) {
		List<OrderVo> orders = this.getByOrdersNum(orderNum);
		return getPayAmount(orders);
	}

	/**
	 * 查询订单
	 */
	@Override
	public List<OrderVo> getOrder(OrderVo order) {
		return adOrderReportDao.getOrder(order);
	}
	/**
	 * 修改订单子项
	 */
	@Override
	public int updateOrderItem(OrderItemVo orderItem) {
		return adOrderDetailDao.update(orderItem);
	}

	/**
	 * 修改订单
	 */
	@Override
	public int updateOrder(OrderVo order) {
		return adOrderReportDao.update(order);
	}

	/**
	 * 订单里存储分类格式为 "1级_2级_3级"
	 * @param product
	 */
	private String getCategory(AdSelfProduct product) {
		StringBuffer category = new StringBuffer();
		String firstCategory = product.getFirstCategory();
		if (StringUtils.isNoneEmpty(firstCategory)) {
			// 一级
			category.append(firstCategory);
			String secondCategory = product.getSecondCategory();
			if (StringUtils.isNoneEmpty(secondCategory)) {
				// 二级
				category.append("_" + secondCategory);
				String thirdCategory = product.getThirdCategory();
				if (StringUtils.isNoneEmpty(thirdCategory)) {
					// 三级
					category.append("_" + thirdCategory);
				}
			}
		}
		return category.toString();
	}

	/***
	 * 取消订单
	 */
	@Override
	@Transactional
	public OrderMsg cancleOrder(long userId, String orderNum) {
		logger.info("cancleOrder() orderNum = {},userId = {}", orderNum, userId);
		if (StringUtils.isEmpty(orderNum) || userId <= 0) {
			return new OrderMsg(MessageDataBean.failure_code, "参数错误!");
		}
		OrderVo orderParam = new OrderVo();
		orderParam.setOrderNumber(orderNum);
		orderParam.setUserId(userId);
		//检查订单状态,交易完成的订单不能取消
		List<OrderVo> orders = this.getOrder(orderParam);
		OrderVo order = null;
		if (!CollectionUtils.isEmpty(orders) && (order = orders.get(0)) != null) {
			if (order.getType() == OrderStatus.CANCELLED_ORDER.getCode() || order.getType() == OrderStatus.HAD_FINISHED_ORDER.getCode() || order.getType() == OrderStatus.NEED_TO_DELIVER.getCode()) {
				logger.error("订单不能取消. orderNum = {},type = {}", order.getOrderNumber(), order.getType());
				return new OrderMsg(MessageDataBean.failure_code, "订单不能取消");
			}
		} else {
			logger.info("cancleOrder() orders = {},order = {}", orders, order);
			return new OrderMsg(MessageDataBean.failure_code, "无效的订单号");
		}
		//2018/8/21/021 qing 取消支付订单 混合支付未完成退还积分
        PayMsg payMsg = refundService.autoRefund(userId, orderNum);
        if(!payMsg.getCode().equals(PayMsg.success_code)){
            //退款失败
            return new OrderMsg(MessageDataBean.failure_code, payMsg.getMess());
        }
        //修改为取消状态
		orderParam.setType(OrderStatus.CANCELLED_ORDER.getCode());
		orderParam.setState(PayState.CANCELLED.getCode());
		int updateStatus = adOrderReportDao.cancleOrder(orderParam);
        //删除_order 删掉未null的
        Order order1 = new Order();
        orderDao.delete(order1);
		//恢复活动库存
		OrderItemVo item = order.getItems().get(0);
		String str[] = item.getProductSkuId().split("-");
		int skuId = Integer.valueOf(str[1]);
		if (!ActivityType.COMMON_ORDER.getActType().equals(order.getActType())) {
			AdUser user = adUserDao.getById(order.getUserId().intValue());
			ActivityInfo actInfo = this.getActivityInfo(String.valueOf(user.getGroupNum()), Integer.valueOf(str[1]));
			logger.info("actInfo = {}", actInfo);
			int rows = productService.incStock(actInfo.getNumber(), skuId);
			logger.info("cancleOrder() orderNum={},incStock.rows = {}", orderNum, rows);
		}
		//恢复商品库存
		int incInventoryRows = productService.incInventory(skuId);
		logger.info("cancleOrder() orderNum={},incInventoryRows = {}", orderNum, incInventoryRows);
		//解锁券
		String couponId = order.getCouponId();
		int unlockCouponRows = adCouponCodeDao.unlockCoupon(userId, couponId);
		logger.info("unlockCoupon userId = {},couponId = {},unlockCouponRows={}", userId, couponId, unlockCouponRows);
		//如果是话费优惠订单参与修改记录
		if(order.getProductType() == ProductType.MOBILE_RECHARGE_PREFERENCE.getCode()) {
			int updateStateOrDelFlagRows = adRechargeRecordDao.updateStateOrDelFlag(order.getOrderNumber(), 1);
			logger.info("updateStateOrDelFlag.rows = {}", updateStateOrDelFlagRows);
		}
		//返回取消结果
		if (updateStatus > 0) {
			logger.info("cancleOrder() orderNum={},cancleOrder.updateStatus = {}", orderNum, updateStatus);
			OrderMsg msg = new OrderMsg(MessageDataBean.success_code, MessageDataBean.success_mess, new HashMap<String, Object>());
			msg.data.put("order_status_code", OrderStatus.CANCELLED_ORDER.getCode());
			msg.data.put("order_status_msg", OrderStatus.CANCELLED_ORDER.getStatus());
			return msg;
		}
		return new OrderMsg(MessageDataBean.failure_code, MessageDataBean.failure_mess);
	}

	/**
	 * 更新订单状态-成功
	 * @param orders
	 * @return
	 */
	@Transactional
	public int updateOrderSuccess(List<OrderVo> orders,String updateBy) {
		int rows = 0;
		for (OrderVo order : orders) {
			OrderVo o = new OrderVo();
			o.setId(order.getId());
			o.setType(OrderStatus.HAD_FINISHED_ORDER.getCode());
			o.setState(PayState.PAID.getCode());
			o.setUpdateDate(new Date());
			o.setUpdateBy(updateBy);
			rows += updateOrder(o);
		}
		return rows;
	}

	/**
	 * 更新订单状态-退款
	 * @param order
	 * @return
	 */
	public int updateOrderRefund(OrderVo order,String updateBy) {
		OrderVo o = new OrderVo();
		o.setId(order.getId());
		o.setType(OrderStatus.RETURN_ORDER.getCode());
		o.setUpdateDate(new Date());
		o.setUpdateBy(updateBy);
		return updateOrder(o);
	}

	@Override
	public List<Map<String, Object>> getByUserSku(String userId, String productSku) {
		return adOrderReportDao.getByUserSku(userId, productSku);
	}

	@Override
	public int getBuyNum(long userId,int skuId, String actType) {
		String sku = "-" + skuId;
		return adOrderReportDao.getByNum(userId, sku, actType);
	}

	@Override
	public BigDecimal getConsumptionAmount(long userId) {
		return adOrderReportDao.getConsumptionAmount(userId);
	}

	@Override
	@Transactional
	public int updateByNum(OrderVo order) {
		return adOrderReportDao.updateByNum(order);
	}

	/**
	 * 活动活动价格
	 * @param groupId
	 * @param skuId
	 * @return
	 */
	public ActivityInfo getActivityInfo(String groupId, int skuId){
		return productService.getActivityInfo(groupId, skuId);
	}

}
