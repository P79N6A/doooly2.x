package com.doooly.business.order.service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.vo.OrderExtVo;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.OrderMsg;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {

    MessageDataBean cancleOrderV2(long userId, String bigOrderNumber);

    MessageDataBean cancelMerchantOrder();

    // 活动类型
	public static enum ActivityType {

		COMMON_ORDER("COMMON", "普通订单"),
		JCHD_ORDER("JCHD", "机场活动订单");

		private String actType;
		private String actName;

		private ActivityType(String actType, String actName) {
			this.actType = actType;
			this.actName = actName;
		}

		public String getActType() {
			return actType;
		}

		public void setActType(String actType) {
			this.actType = actType;
		}

		public String getActName() {
			return actName;
		}

		public void setActName(String actName) {
			this.actName = actName;
		}
	}

	// 积分支付需要此参数
	public final String STORESID = PropertiesConstants.dooolyBundle.getString("storesId");
	// 订单来源 - 3表示兜礼自营
	public final static int DOOOLY_SOURCE = 3;

	//商品类型
	public static enum ProductType {
		//参考表: ad_self_product_type
		WILLS_CARD(1, "实体卡券"),
		VIRTUAL_CARD(2, "电子卡券"),
		MOBILE_RECHARGE(3, "话费充值"),
		FLOW_RECHARGE(4, "流量充值"),
		TOURIST_CARD_RECHARGE(5, "旅游卡充值"),
		SELF_CARDPSW_RECHARGE(6, "自营卡券"),
		MOBILE_RECHARGE_PREFERENCE(7, "兜礼话费特惠"),
		MOBIKE_RECHARGE(8, "摩拜充值"),
		NEXUS_RECHARGE(9, "集享积分充值"),
		NEXUS_RECHARGE_ACTIVITY(10, "集享积分充值活动");

		private int code;
		private String productTypeName;

		private ProductType(int code, String productTypeName) {
			this.code = code;
			this.productTypeName = productTypeName;
		}

		public int getCode() {
			return code;
		}

		public String getProductTypeName() {
			return productTypeName;
		}

		public static String getProductTypeName(int code) {
			switch (code) {
				case 1:
					return WILLS_CARD.getProductTypeName();
				case 2:
					return VIRTUAL_CARD.getProductTypeName();
				case 3:
					return MOBILE_RECHARGE.getProductTypeName();
				case 4:
					return FLOW_RECHARGE.getProductTypeName();
				case 5:
					return TOURIST_CARD_RECHARGE.getProductTypeName();
				case 6:
					return SELF_CARDPSW_RECHARGE.getProductTypeName();
				case 7:
					return MOBILE_RECHARGE_PREFERENCE.getProductTypeName();
				case 8:
					return MOBIKE_RECHARGE.getProductTypeName();
				case 9:
					return NEXUS_RECHARGE.getProductTypeName();
				default:
					break;
			}
			return null;
		}
	}

	//订单状态
	public static enum OrderStatus {
		HAD_FINISHED_ORDER(1, "交易完成"),
		RETURN_ORDER(5, "退货订单"),
		NEED_TO_PAY(10, "待付款"),
		NEED_TO_DELIVER(20, "待发货"),
		HAD_DELIVERED_GOODS(30, "已收货"),
		CONFIRM_RECEIVED_GOODS(50, "已收货"),
		CANCELLED_ORDER(99, "交易取消");

		private int code;
		private String status;

		private OrderStatus(int code, String status) {
			this.code = code;
			this.status = status;
		}

		public int getCode() {
			return code;
		}

		public String getStatus() {
			return status;
		}
	}

	// 支付状态
	public static enum PayState {
		UNPAID(0, "未支付"),
		PAID(1, "已支付"),
		CANCELLED(2, "已取消");

		private int code;
		private String state;

		private PayState(int code, String state) {
			this.code = code;
			this.state = state;
		}

		public int getCode() {
			return code;
		}

		public String getStatus() {
			return state;
		}
	}

	/**
	 * 创建订单
	 */
	public OrderMsg createOrder(JSONObject json);
	/**
	 * 创建订单createOrderv2
	 */
	public OrderMsg createOrderv2(JSONObject json);

	/**
	 * 保存订单
	 */
	public int saveOrder(OrderVo order, OrderExtVo orderExt, List<OrderItemVo> orderItem);

	public long saveOrder(OrderVo order);//主订单

	public int saveOrderExt(long orderId, OrderExtVo orderExt);//订单扩展信息

	public int saveOrderItem(long orderId, List<OrderItemVo> orderItem);

	/**
	 * 查询订单
	 */
	public OrderVo getByOrderNum(String orderNum);

	public List<OrderVo> getByOrdersNum(String orderNum);

	public OrderVo getById(String orderId);

	public List<OrderVo> getOrder(OrderVo order);

	/**
	 * 查询订单金额
	 */
	public BigDecimal getPayAmount(List<OrderVo> orders);

	public BigDecimal getPayAmountByNum(String orderNum);

	/**
	 * 修改订单
	 */
	public int updateOrderItem(OrderItemVo orderItem);

	public int updateOrder(OrderVo order);

	public int updateOrderSuccess(List<OrderVo> orders, String updateBy);

	public int updateByNum(OrderVo order);

	public int updateOrderRefund(OrderVo order, String updateBy);


	/**
	 * 取消订单
	 */
	public OrderMsg cancleOrder(long userId, String orderNum);

	/**
	 * 获得用户某种sku的订单
	 */
	public List<Map<String, Object>> getByUserSku(String userId, String productSku);

	/**
	 * 购买数量
	 *
	 * @param userId
	 * @param actType
	 * @return
	 */
	public int getBuyNum(long userId, int skuId, String actType);

	/**
	 * skuId 对应活动信息
	 *
	 * @param groupId
	 * @param skuId
	 * @return
	 */
	public ActivityInfo getActivityInfo(String groupId, int skuId);

	/**
	 * 手机充值消费总金额
	 * @param userId
	 * @return
	 */
	public BigDecimal getConsumptionAmount(long userId);

}